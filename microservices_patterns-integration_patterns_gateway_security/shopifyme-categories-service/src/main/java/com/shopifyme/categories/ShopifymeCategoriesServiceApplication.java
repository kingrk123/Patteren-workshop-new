package com.shopifyme.categories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class ShopifymeCategoriesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopifymeCategoriesServiceApplication.class, args);
	}

}
