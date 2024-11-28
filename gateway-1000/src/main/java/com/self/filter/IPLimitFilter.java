package com.self.filter;

import com.google.gson.Gson;
import com.self.grace.result.GraceJSONResult;
import com.self.grace.result.ResponseStatusEnum;
import com.self.utils.IPUtil;
import com.self.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/28
 */
@Component
@Slf4j
@RefreshScope
public class IPLimitFilter implements GlobalFilter, Ordered {
    @Resource
    private RedisOperator redis;
    @Value("${blackIp.timeInterval}")
    private Integer timeInterval;
    @Value("${blackIp.limitTimes}")
    private Integer limitTimes;
    @Value("${blackIp.continueCounts}")
    private Integer continueCounts;
//    private final Integer timeInterval = 20;//统计连续访问的时间，在这个时间内超过continueCounts就拉黑
//    private final Integer limitTimes = 30;//冻结时间
//    private final Integer continueCounts = 3;//连续访问数

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        if (1 == 1) {
//            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
//        }

        log.info("continueCounts: {}", continueCounts);
        log.info("timeInterval: {}", timeInterval);
        log.info("limitTimes: {}", limitTimes);
        return doLimit(exchange, chain);
//        return chain.filter(exchange);
    }

    /**
     * 限值ip请求次数
     */
    public Mono<Void> doLimit(ServerWebExchange exchange, GatewayFilterChain chain) {
        //根据request获取请求ip
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIP(request);
        //正常的ip定义
        String ipRedisKey = "gateway-ip:" + ip;
        //被拦截的黑名单ip ，如果在redis中存在，则表示目前被关小黑屋
        String ipRedisLimitKey = "gateway-ip:limit:" + ip;

        //获取当前ip并查询还剩下多少时间，如果时间存在（大于0），则表示当前仍然处在黑名单中
        long limitLeftTimes = redis.ttl(ipRedisLimitKey);//默认redis内过期，key不存在，但是ttl方法会返回-2，不会报空指针
        if (limitLeftTimes > 0) {
            //黑名单生效中  终止请求 返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        //在redis中获取ip的累加次数[到此处说明不在黑名单]
        long requestCounts = redis.increment(ipRedisKey, 1);//increment如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。

        /**
         * 判断如果是第一次进来，也就是从0开始计数，则初期访问就是1
         * 需要设置间隔的时间，也就是连续请求的次数的间隔时间
         * [本质上是给上面key添加过期时间，若过期了就不存在，不过期就在此时间内统计加1次数，就是上面的的累加increment]
         */
        if (requestCounts == 1) {
            redis.expire(ipRedisKey, timeInterval);
        }

        /**
         * 如果还能获得请求的正常次数，证明key存在 还未过期，接着统计次数 [如果不存在，则之前的记录作废，代表正常访问，重新记录]
         * 一旦请求次数超过限定的连续访问次数[continueCounts]，则需要限制当前的ip
         */
        if (requestCounts > continueCounts) {
            //限制ip访问的时间[limitTimes]
            redis.set(ipRedisLimitKey, ipRedisLimitKey, limitTimes);
            //终止请求 返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }
        //正常放行
        return chain.filter(exchange);

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
        return 1;
    }
}
