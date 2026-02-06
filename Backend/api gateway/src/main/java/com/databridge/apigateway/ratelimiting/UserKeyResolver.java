package com.databridge.apigateway.ratelimiting;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {

        String email = exchange.getRequest()
                .getHeaders()
                .getFirst("X-User-Email");

        if(email != null){
            return Mono.just(email);
        }

        return Mono.just("anonymous");
    }
}
