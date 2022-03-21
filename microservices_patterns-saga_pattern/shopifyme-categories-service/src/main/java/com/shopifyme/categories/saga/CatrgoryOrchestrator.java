package com.shopifyme.categories.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.shopifyme.categories.events.CategoryEvent;
import com.shopifyme.saga.commands.CategoryUpdateSagaCommand;
import com.shopifyme.saga.events.CategoryUpdateSagaEvent;

@Saga
public class CatrgoryOrchestrator {

	@Autowired
	private  CommandGateway commandGateway;
	
	@StartSaga
	@SagaEventHandler(associationProperty = "aggregateIdentifier")
	public void updateCategoryTransactionalEvent(CategoryEvent event) {
		System.out.println("i am in category orchestrator");
		System.out.println("associate :\t"+event.getAggregateIdentifier());
		
		SagaLifecycle.associateWith("aggregateIdentifier", event.getAggregateIdentifier());
		
		CategoryUpdateSagaCommand updateCategoryCommand = new CategoryUpdateSagaCommand(event.getId(),event.getName(),event.getAlias(),
				event.isEnabled(),event.getAggregateIdentifier());
		
		commandGateway.send(updateCategoryCommand);
		
		System.out.println("Dispacted CategoryUpdateSagaCommand...");
	}
	
	@SagaEventHandler(associationProperty = "aggregateIdentifier")
	public void updateCategoryTransactionalEvent(CategoryUpdateSagaEvent event) {
		System.out.println("event recieved from shopify-me-product-brand-service");
		System.out.println("associate :\t"+event.getAggregateIdentifier());
		
		SagaLifecycle.associateWith("aggregateIdentifier", event.getAggregateIdentifier());
		
		CategoryUpdateSagaCommand updateCategoryCommand = new CategoryUpdateSagaCommand(event.getId(),event.getName(),event.getAlias(),
				event.isEnabled(),event.getAggregateIdentifier());
		
		commandGateway.send(updateCategoryCommand);
		
		System.out.println("Dispacted CategoryUpdateSagaCommand...");
	}
	
	
	
	
}
