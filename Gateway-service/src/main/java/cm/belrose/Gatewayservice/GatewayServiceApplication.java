package cm.belrose.Gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
        
        @Bean
        RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
            return builder.routes()
                    .route(r->r.path("/customers/**").uri("lb://CUSTOMER-SERVICE").id("r1"))
                    .route(r->r.path("/products/**").uri("lb://INVENTORY-SERVICE").id("r2"))
                    .build();
        }

}
