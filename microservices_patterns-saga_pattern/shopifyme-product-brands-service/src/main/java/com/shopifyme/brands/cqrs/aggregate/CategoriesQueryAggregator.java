/**
 * 
 */
package com.shopifyme.brands.cqrs.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.brands.cqrs.events.BrandsCategoryEvent;
import com.shopifyme.saga.commands.CategoryUpdateSagaCommand;
import com.shopifyme.saga.events.CategoryUpdateSagaEvent;

/**
 * @author IM-LP-1763
 *
 */
@Aggregate(snapshotTriggerDefinition = "productBrandsSnapshotTriggerDefinition")
public class CategoriesQueryAggregator {

	@AggregateIdentifier
	private String aggregateIdentifier;
	
	@CommandHandler
	public CategoriesQueryAggregator(CategoryUpdateSagaCommand command) {
		System.out.println("********************** Command recievd from AXON Server is:\t"+command.getAggregateIdentifier());
		CategoryUpdateSagaEvent event = new CategoryUpdateSagaEvent(command.getId(), command.getName(), 
				command.getAlias(), command.getAggregateIdentifier(),command.isEnabled());
		this.aggregateIdentifier = command.getAggregateIdentifier();
		//BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}
	
	@EventHandler
	public void brandsCategoryUpdatedEvent(CategoryUpdateSagaEvent event) {
		System.out.println("brands event recievd is:\t"+event.getAggregateIdentifier());
		//List<Brand> brandsList = repo.findAll();
		ObjectMapper mapper = new ObjectMapper();
		/*
		 * if(brandsList != null && brandsList.size() > 0) {
		 * brandsList.stream().forEach(brand->{ String categories =
		 * brand.getCategories(); List<Map> mapList = (List<Map>)
		 * mapper.convertValue(categories, Object.class); if(mapList != null) {
		 * mapList.stream().forEach(map->{ int id = (int) map.get("id"); if(id ==
		 * event.getId()) { map.put("name", event.getName()); } }); } try { String
		 * updatedCategories = mapper.writeValueAsString(mapList);
		 * System.out.println("updatedCategories are:\t"+updatedCategories); } catch
		 * (JsonProcessingException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * }); }
		 */
		
	}
	
}
