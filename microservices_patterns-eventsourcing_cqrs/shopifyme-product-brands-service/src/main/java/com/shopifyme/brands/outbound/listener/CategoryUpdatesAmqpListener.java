package com.shopifyme.brands.outbound.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.brands.cqrs.commands.BrandsCategoryCommand;
import com.shopifyme.brands.domains.Brand;
import com.shopifyme.brands.repos.BrandRepository;

@Configuration
public class CategoryUpdatesAmqpListener {

	@Autowired
	private RabbitTemplate rabitTemplate;

	@Autowired
	private BrandRepository brandRepo;

	@Autowired
	private CommandGateway commandGateway;

	/*
	 * @RabbitListener(queues = "${amqp.services.category-service-queue}") public
	 * void recieveCategoryUpdates(String categoryEventJsonData) {
	 * System.out.println("event recievd from queue is:\t" + categoryEventJsonData);
	 * List<Brand> updatedBrandsList = new ArrayList<>(); List<String> dbCatsList =
	 * new ArrayList<>(); if (categoryEventJsonData != null &&
	 * !categoryEventJsonData.isEmpty()) { JSONObject json = new
	 * JSONObject(categoryEventJsonData); List<Brand> brandList =
	 * brandRepo.findAll(); if (brandList != null && brandList.size() > 0) {
	 * brandList.parallelStream().forEach(brand -> { String brandCategories =
	 * brand.getCategories(); if (brandCategories != null && json.has("id")) {
	 * String[] brandCats = brandCategories.split("\\,"); if(brandCats != null &&
	 * brandCats.length >0) { for(String brandCat : brandCats) { int catId =
	 * json.getInt("id"); if (brandCat.contains(String.valueOf(catId))) {
	 * dbCatsList.add(categoryEventJsonData); }else { dbCatsList.add(brandCat); }
	 * brand.setCategories(dbCatsList.toString()); updatedBrandsList.add(brand); } }
	 * 
	 * }
	 * 
	 * }); } // save the data to database if (updatedBrandsList != null &&
	 * updatedBrandsList.size() > 0) { brandRepo.saveAll(updatedBrandsList); } }
	 * else { System.out.println("No event found in event queue"); } }
	 */

	@RabbitListener(queues = "${amqp.services.category-service-queue}")
	public void recieveCategoryUpdates(String categoryEventJsonData) {

		// {"id":2,"name":"Cloths_Kids_ENT","alias":"Cloths_Kids_ENT","aggregateIdentifier":"7984611670933942784","enabled":true}
		System.out.println("event recievd from queue is:\t" + categoryEventJsonData);
		ObjectMapper mapper = new ObjectMapper();
		try {
			BrandsCategoryCommand brandsCommand = mapper.readValue(categoryEventJsonData, BrandsCategoryCommand.class);

			CompletableFuture<String> futureResp = commandGateway.send(brandsCommand);
			if (futureResp.isDone()) {
				System.out.println("commmand successfully taken by the aggregate");
			}

		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
