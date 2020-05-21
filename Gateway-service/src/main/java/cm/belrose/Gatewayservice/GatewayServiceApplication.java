package cm.belrose.Gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    // @Bean
//        RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
//            return builder.routes()
//                    .route(r->r.path("/customers/**").uri("lb://CUSTOMER-SERVICE").id("r1"))
//                    .route(r->r.path("/products/**").uri("lb://INVENTORY-SERVICE").id("r2"))
//                    .build();
//        }
    @Bean
    DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }

    @Bean
    RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/restcountries/**")
                .filters(f -> f
                .addRequestHeader("x-rapidapi-host", "restcountries-v1.p.rapidapi.com")
                .addRequestHeader("x-rapidapi-key", "e07ef331c1msh07e4e088f3c24afp1743dajsn03e96c5bf98b")
                .rewritePath("/restcountries/(?<segment>.*)", "/${segment}")
                )
                .uri("https://restcountries-v1.p.rapidapi.com/all").id("countries")
                )
                .route(r -> r.path("/world-population/**")
                .filters(f -> f
                .addRequestHeader("x-rapidapi-host", "world-population.p.rapidapi.com")
                .addRequestHeader("x-rapidapi-key", "e07ef331c1msh07e4e088f3c24afp1743dajsn03e96c5bf98b")
                .rewritePath("/world-population/(?<segment>.*)", "/${segment}")
                )
                .uri("https://world-population.p.rapidapi.com/allcountriesname").id("world-population")
                )
                .build();
    }

}
