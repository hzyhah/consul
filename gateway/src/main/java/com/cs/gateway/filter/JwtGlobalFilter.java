package com.cs.gateway.filter;

import com.cs.common.CodeMsg;
import com.cs.common.Result;
import com.cs.common.WebUtil;
import com.cs.common.json.JsonUtils;
import com.cs.gateway.config.SysConstants;
import com.cs.gateway.feign.JwtFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    /*@Reference
    IAccountProvider accountProvider;*/

    @Autowired
    JwtFeign jwtFeign;


    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("JwtGlobalFilter =======");
        String rawPath = exchange.getRequest().getURI().getRawPath();
        System.out.println("@@@@"+rawPath);

        if (SysConstants.urls.contains(rawPath)){
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst(SysConstants.ACCESS_TOKEN);
        ServerHttpResponse originalResponse = exchange.getResponse();
        if (WebUtil.checkEmpty(token)){
            DataBuffer buffer = setResponseInfo(originalResponse,Result.fail(CodeMsg.NO_ACCESS_AUTH));
            return exchange.getResponse().writeWith(Flux.just(buffer));
        }

        //验证token
        String msg = jwtFeign.check(token);
        System.out.println(msg);

        return chain.filter(exchange);
    }

    private DataBuffer setResponseInfo(ServerHttpResponse response, String body) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        byte[] responseByte = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(responseByte);
        return buffer;
    }

    public int getOrder() {
        return -1;
    }
}
