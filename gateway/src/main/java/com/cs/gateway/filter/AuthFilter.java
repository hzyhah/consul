package com.cs.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AuthFilter implements GlobalFilter, Ordered {

    /*@Reference
    IAccountProvider accountProvider;*/

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        //解析token
        /*System.out.println("进入    全局filter");
        if (token==null || token.length()==0){
            return exchange.getResponse().setComplete();
        }*/

        String rawPath = exchange.getRequest().getURI().getRawPath();
        System.out.println("@@@@"+rawPath);
        return chain.filter(exchange);
    }

    public int getOrder() {
        return -1;
    }
}
