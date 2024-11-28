package com.self.filter;

import com.google.gson.Gson;
import com.self.grace.result.GraceJSONResult;
import com.self.grace.result.ResponseStatusEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/11/28
 */
@Component
public class IPLimitFilter implements GlobalFilter , Ordered {
    private final Integer timeInterval=20;
    private final Integer limitTimes=30;
    private final Integer continueCounts=3;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(1==1){
            return renderErrorMsg(exchange,ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }
        return chain.filter(exchange);
//        return doLimit(exchange,chain);
    }
//    public Mono<Void> doLimit(ServerWebExchange exchange, GatewayFilterChain chain){
//
//    }

    /**
     * 重新包装且返回错误的信息
     */
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum statusEnum){
        //1.获取相应的response
        ServerHttpResponse response = exchange.getResponse();
        //2.构建返回体（R）
        GraceJSONResult jsonResult = GraceJSONResult.exception(statusEnum);
        //3.设置header类型
        if(!response.getHeaders().containsKey("Content-Type")){
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
