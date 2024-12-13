package com.self.filter;

import com.google.gson.Gson;
import com.self.grace.result.GraceJSONResult;
import com.self.grace.result.ResponseStatusEnum;
import com.self.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.self.config.BaseInfoProperties.REDIS_USER_TOKEN;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/13
 */
@Component
@Slf4j
public class SecurityFilterToken implements GlobalFilter, Ordered {
    @Resource
    private ExcludeUrlProperties excludeUrlProperties;
    @Resource
    private RedisOperator redisOperator;

    //路径匹配规则器
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获得当前用户请求路径url
        String url = exchange.getRequest().getURI().getPath();
        log.info("SecurityFilterToken url={}", url);

        //2.获得所有需要排除校验的url List
        List<String> excludeList = excludeUrlProperties.getUrls();

        //3.1校验并排除excludeList
        if (excludeList != null && !excludeList.isEmpty()) {
            for (String excludeUrl : excludeList) {
                if (antPathMatcher.matchStart(excludeUrl, url)) {
                    //如果匹配 直接放行 当前url是不需要别拦截校验的
                    return chain.filter(exchange);
                }
            }
        }
        //4.代码到达此处 表示请求被拦截 需要进行校验
        log.info("当前请求的路径[{}]被拦截...", url);

        //5.从header获取用户的 id以及token
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String userId = headers.getFirst("headerUserId");
        String userToken = headers.getFirst("headerUserToken");
        log.info("userId={}", userId);
        log.info("userToken={}", userToken);
        //6.判断header 中是否有token 对用户请求进行判断拦截
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            String redisToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            //若已经退出 无token 则拦截不放行
            if(StringUtils.isBlank(redisToken)){
                return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
            }
            if (redisToken.equals(userToken)) {
                //匹配放行
                return chain.filter(exchange);
            }
        }
        //默认不放行
        return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);

    }

    /**
     * 重新包装且返回错误的信息
     */
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum statusEnum) {
        //1.获取相应的response
        ServerHttpResponse response = exchange.getResponse();
        //2.构建返回体（R）
        GraceJSONResult jsonResult = GraceJSONResult.exception(statusEnum);
        //3.设置header类型
        if (!response.getHeaders().containsKey("Content-Type")) {
            response.getHeaders().set("Content-Type", "application/json");
        }
        //4.修改response 状态码 500，（接口的响应码，非返回的结构体码）
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        //5.手动转换json 正产接口有@ResponseBody，所以不需要
        String resultJson = new Gson().toJson(jsonResult);
        DataBuffer buffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        Mono<Void> voidMono = response.writeWith(Mono.just(buffer));
        return voidMono;

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
