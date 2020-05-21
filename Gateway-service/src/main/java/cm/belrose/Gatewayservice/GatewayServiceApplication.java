package cm.belrose.Gatewayservice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableHystrix
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
                .hystrix(h -> h.setName("restcountries")
                .setFallbackUri("forward:/restcountriesFallback"))
                )
                .uri("https://restcountries-v1.p.rapidapi.com/all").id("countries")
                )
                .route(r -> r.path("/world-population/**")
                .filters(f -> f
                .addRequestHeader("x-rapidapi-host", "world-population.p.rapidapi.com")
                .addRequestHeader("x-rapidapi-key", "e07ef331c1msh07e4e088f3c24afp1743dajsn03e96c5bf98b")
                .rewritePath("/world-population/(?<segment>.*)", "/${segment}")
                .hystrix(h -> h.setName("world-population")
                .setFallbackUri("forward:/restworlpopulationFallback"))
                )
                .uri("https://world-population.p.rapidapi.com/allcountriesname").id("world-population")
                )
                .build();
    }

}

@RestController
class FallBackRestController {
    @GetMapping("/restcountriesFallback")
    public Map<String,String> restcountriesFallback(){
        Map<String,String> map= new HashMap<>();
        map.put("Message", "Default rest countries fallback service");
        map.put("Countries", "Cameroun, Tchad");
        return map;
    }
    @GetMapping("/restworlpopulationFallback")
    public Map<String,String> restworlpopulationFallback(){
        Map<String,String> map= new HashMap<>();
        map.put("Message", "Default rest wolrd population fallback service");
        map.put("Countries", "Cameroun");
        return map;
    }
}
