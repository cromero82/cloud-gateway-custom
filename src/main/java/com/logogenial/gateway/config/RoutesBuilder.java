package com.logogenial.gateway.config;

import com.logogenial.gateway.filters.CustomTokenAutenticatedFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesBuilder {

    @Autowired
    private CustomTokenAutenticatedFilter filterFactory;


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("login", r -> r.path("/api/**")
                        .uri("http://localhost:8090/"))
                .route("alls", r -> r.path("/v1/**")
                        .filters(f ->
                                f.filters(filterFactory.apply(new CustomTokenAutenticatedFilter.Config()))
                                .removeRequestHeader("Cookie"))
                        .uri("http://localhost:8090/"))
                .build();
    }
}
