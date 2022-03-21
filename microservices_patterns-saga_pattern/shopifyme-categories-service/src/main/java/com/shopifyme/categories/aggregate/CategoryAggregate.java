package com.shopifyme.categories.aggregate;

import org.apache.commons.lang3.RandomUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.core.annotation.Order;

import com.shopifyme.categories.commands.CategoryCommand;
import com.shopifyme.categories.events.CategoryEvent;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Aggregate
public class CategoryAggregate {
	
	@AggregateIdentifier
	private long id;

	@CommandHandler
	public CategoryAggregate(CategoryCommand categoryCommand) {
		System.out.println("step-2: Converting the Command to Event By the Aggregator");
		
		this.id = RandomUtils.nextLong();
		
		CategoryEvent categoryEvent = new CategoryEvent(categoryCommand.getId(), 
				categoryCommand.getName(),categoryCommand.getAlias(), categoryCommand.isEnabled(), String.valueOf(id));
		
		
		
		System.out.println("Step-3: Publishing the Event");
		AggregateLifecycle.apply(categoryEvent);		
	}
	
	
	/**
	 * Broker Publisher Event Hadler
	 * @param catEvent
	 */
	@EventHandler
	public void primaryEventSourceHandler(CategoryEvent catEvent) {
		/**
		 * publish this event to broker to take it by the 
		 * other consumers also store this event into event store
		 */
		System.out.println("step-4: in primaryEventSourceHandler event data is--"+catEvent.toString());
		
	}
	
	/**
	 * Event store Event Handler
	 * @param catEvent
	 */
	@EventHandler
	public void secondaryEventSourceHandler(CategoryEvent catEvent) {
		/**
		 * publish this event to broker to take it by the 
		 * other consumers also store this event into event store
		 */
		System.out.println("step-4: in secondaryEventSourceHandler event data is--"+catEvent.toString());
	}
}
