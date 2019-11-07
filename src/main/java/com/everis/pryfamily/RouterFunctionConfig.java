package com.everis.pryfamily;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.everis.pryfamily.controller.FamilyController;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {


	
	@Bean
	public RouterFunction<ServerResponse> routesf(FamilyController controller	){
		return RouterFunctions.route(GET("/api/family"), controller::listar)
				.andRoute(GET("/api/family/{name}"), controller::ver)
				.andRoute(GET("/api/family/fecha/{start}/{end}"), controller::verfec)
				.andRoute(POST("/api/family"), controller::crear)
				.andRoute(PUT("/api/family/{id}"), controller::editar)
				.andRoute(DELETE("/api/family/{numberDocument}"), controller::eliminar);
	}
}
