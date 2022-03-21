package com.shopifyme.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ShopifymeServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopifymeServiceRegistryApplication.class, args);
	}

}
