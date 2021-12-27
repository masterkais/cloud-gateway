package com.iit.imprimerie;

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
//cercuit breaker,dashbording et monotoring , configurer la qualiter de service de mon system qui reponde toujours ==> en cas d'echec redirection vers une reponse par defaut 
@EnableHystrix
@SpringBootApplication
public class CloudGatewayApplication {
	//un bean qui s'occupe de faire les routes d'une maniére dstatic route configuration with discovery service
	//ou bien des configs dans application.yml
	@Bean
	RouteLocator routes(RouteLocatorBuilder builder) {
		//http://localhost:8989/GeAllEnseignant
		return builder.routes()
				.route(r -> r.path("/administration/**")
						.filters(
                         f->f
                         //pour transformer le path
                          .rewritePath("/administration/(?<segment>.*)","/${segment}")
						  //cercuit breaker 
                          .hystrix(h->h.setName("administration")
						             .setFallbackUri("forward:/defaultEnseignant")
								  )
								)
						.uri("lb://service-administration")
						.id("r1"))
				.route(r -> r.path("/documents/**")
						.filters(
                         f->f
                         //pour transformer le path
                          .rewritePath("/documents/(?<segment>.*)","/${segment}")
						  //cercuit breaker 
                          .hystrix(h->h.setName("documents")
						             .setFallbackUri("forward:/defaultDocument")
								  )
								)
						.uri("lb://microservice-document")
						.id("r2"))
				.route(r -> r.path("/demandeTirages/**")
						.filters(
                         f->f
                         //pour transformer le path
                          .rewritePath("/demandeTirages/(?<segment>.*)","/${segment}")
						  //cercuit breaker 
                          .hystrix(h->h.setName("demandeTirages")
						             .setFallbackUri("forward:/defaultDemandeTirages")
								  )
								)
						.uri("lb://service-DemandeTirage")
						.id("r3"))
				.route(r -> r.path("/bonTirages/**")
						.filters(
                         f->f
                         //pour transformer le path
                          .rewritePath("/bonTirages/(?<segment>.*)","/${segment}")
						  //cercuit breaker 
                          .hystrix(h->h.setName("bonTirages")
						             .setFallbackUri("forward:/defaultBonTirages")
								  )
								)
						.uri("lb://service-BonTirage")
						.id("r4"))
				.build();
	}
	// un bean qui s'occupe de faire les routes d'une maniére dynamique
	 @Bean
	 DiscoveryClientRouteDefinitionLocator dynamicRoutes (ReactiveDiscoveryClient
	 rdc, DiscoveryLocatorProperties dlp) {
	 return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
	 }

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}
@RestController
	class HystrixController{
	@GetMapping("/defaultEnseignant")
public Map<String, String> EnseignantFallBack(){
	Map<String, String> enseignants=new HashMap<>();
	enseignants.put("Ma","Maroc");
	return enseignants;
}
	}
}
