package com.logogenial.gateway.filters;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomTokenAutenticatedFilter extends AbstractGatewayFilterFactory<CustomTokenAutenticatedFilter.Config> {
    public CustomTokenAutenticatedFilter() {
        super(Config.class);
        }

    private boolean isAuthorizationValid(String authorizationHeader) {
        boolean isValid = true;

        // Logic for checking the value

        return isValid;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            request.getPath();
            System.out.println("Filtro aplicado a ruta: "+ request.getPath());
            if (!request.getHeaders().containsKey("Authorization")) {
                System.out.println("Error aplicando filtro a: "+ request.getPath());
                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            };

            String authorizationHeader = request.getHeaders().get("Authorization").get(0);

            if (!this.isAuthorizationValid(authorizationHeader)) {
                return this.onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().
                    header("secret", RandomStringUtils.random(10)).
                    build();
            System.out.println("Filtro ok: "+ request.getPath());
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    public static class Config {
        // Put the
    }
  }
