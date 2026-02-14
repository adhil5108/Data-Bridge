package com.databridge.apigateway.security;

import com.databridge.apigateway.enums.ErrorCode;
import com.databridge.apigateway.exception.InvalidTokenException;
import com.databridge.apigateway.security.error.GatewayErrorHandler;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final GatewayErrorHandler errorHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();


        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return errorHandler.handle(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    ErrorCode.MISSING_AUTH_HEADER.getMessage()
            );
        }

        String token = authHeader.substring(7);

        Claims claims;

        try {
            claims = jwtUtil.validateToken(token);
        }
        catch (InvalidTokenException ex){

            return errorHandler.handle(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    ErrorCode.INVALID_TOKEN.getMessage()
            );
        }

        String role = claims.get("role", String.class);


        if(role == null){
            return errorHandler.handle(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    ErrorCode.ROLE_MISSING.getMessage()
            );
        }


        if(path.startsWith("/admin") && !"ADMIN".equals(role)){

            return errorHandler.handle(
                    exchange,
                    HttpStatus.FORBIDDEN,
                    ErrorCode.FORBIDDEN.getMessage()
            );
        }


        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder
                        .header("X-User-Email", claims.getSubject())
                        .header("X-User-Role", role)
                )
                .build();

        return chain.filter(mutatedExchange);
    }


    private boolean isPublicEndpoint(String path){
        return path.startsWith("/auth/")
                || path.startsWith("/actuator")
                || path.contains("oauth2");
    }


    @Override
    public int getOrder() {
        return -2;
    }

}
