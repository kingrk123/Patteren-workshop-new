package com.shopifyme.categories.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.shopifyme.categories.commands.CategoryCommand;
import com.shopifyme.categories.events.CategoryEvent;

@Aggregate
public class CategoryAggregate {
	
	@AggregateIdentifier
	private int id;

	@CommandHandler
	public CategoryAggregate(CategoryCommand categoryCommand) {
		System.out.println("step-2: Converting the Command to Event By the Aggregator");
		CategoryEvent categoryEvent = new CategoryEvent(categoryCommand.getId(), 
				categoryCommand.getName(),categoryCommand.getAlias(), categoryCommand.isEnabled());
		this.id = categoryEvent.getId();
		System.out.println("Step-3: Publishing the Event");
		AggregateLifecycle.apply(categoryEvent);		
	}
	
	
	/**
	 * Broker Publisher Event Hadler
	 * @param catEvent
	 */
	@EventHandler
	public void onCategoryEventPublishedToBroker(CategoryEvent catEvent) {
		/**
		 * publish this event to broker to take it by the 
		 * other consumers also store this event into event store
		 */
		System.out.println("step-4: in onCategoryEventPublishedToBroker event data is--"+catEvent.toString());
	}
	
	/**
	 * Event store Event Handler
	 * @param catEvent
	 */
	@EventHandler
	public void onCategoryEventPublishedToEventStore(CategoryEvent catEvent) {
		/**
		 * publish this event to broker to take it by the 
		 * other consumers also store this event into event store
		 */
		System.out.println("step-4: in onCategoryEventPublishedToEventStore event data is--"+catEvent.toString());
	}
}
