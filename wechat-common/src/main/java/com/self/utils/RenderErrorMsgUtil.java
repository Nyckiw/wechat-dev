package com.self.utils;

import com.google.gson.Gson;
import com.self.grace.result.GraceJSONResult;
import com.self.grace.result.ResponseStatusEnum;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class RenderErrorMsgUtil {

    public static Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum responseStatusEnum){
        ServerHttpResponse response = exchange.getResponse();
        GraceJSONResult jsonResult = GraceJSONResult.exception(responseStatusEnum);
        HttpHeaders headers = response.getHeaders();
        final String jsonMimeType = "Content-Type";
        if (!headers.containsKey(jsonMimeType)){
            headers.add(jsonMimeType, MimeTypeUtils.APPLICATION_JSON_VALUE);
        }

        // 设置状态码为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        String jsonStr = new Gson().toJson(jsonResult);
        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonStr.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }
}
