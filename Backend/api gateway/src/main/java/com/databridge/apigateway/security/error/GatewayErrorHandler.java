package com.databridge.apigateway.security.error;

import com.databridge.apigateway.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GatewayErrorHandler {

    private final ObjectMapper objectMapper;

    public Mono<Void> handle(ServerWebExchange exchange,
                             HttpStatus status,
                             String message){

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders()
                .set(HttpHeaders.CONTENT_TYPE, "application/json");

        ErrorResponse response =
                new ErrorResponse(status.value(),
                        status.getReasonPhrase(),
                        message);

        try {

            byte[] bytes =
                    objectMapper.writeValueAsBytes(response);

            var buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(bytes);

            return exchange.getResponse()
                    .writeWith(Mono.just(buffer));

        }
        catch (Exception e){

            var buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(message.getBytes());

            return exchange.getResponse()
                    .writeWith(Mono.just(buffer));
        }
    }
}
