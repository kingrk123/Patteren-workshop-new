package com.shopifyme.categories.aggregate;

<<<<<<< HEAD
import org.apache.commons.lang3.RandomUtils;
=======
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
<<<<<<< HEAD
import org.springframework.core.annotation.Order;
=======
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d

import com.shopifyme.categories.commands.CategoryCommand;
import com.shopifyme.categories.events.CategoryEvent;

<<<<<<< HEAD
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
=======
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
@Aggregate
public class CategoryAggregate {
	
	@AggregateIdentifier
<<<<<<< HEAD
	private long id;
=======
	private int id;
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d

	@CommandHandler
	public CategoryAggregate(CategoryCommand categoryCommand) {
		System.out.println("step-2: Converting the Command to Event By the Aggregator");
<<<<<<< HEAD
		
		this.id = RandomUtils.nextLong();
		
		CategoryEvent categoryEvent = new CategoryEvent(categoryCommand.getId(), 
				categoryCommand.getName(),categoryCommand.getAlias(), categoryCommand.isEnabled(), String.valueOf(id));
		
		
		
=======
		CategoryEvent categoryEvent = new CategoryEvent(categoryCommand.getId(), 
				categoryCommand.getName(),categoryCommand.getAlias(), categoryCommand.isEnabled());
		this.id = categoryEvent.getId();
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
		System.out.println("Step-3: Publishing the Event");
		AggregateLifecycle.apply(categoryEvent);		
	}
	
	
	/**
	 * Broker Publisher Event Hadler
	 * @param catEvent
	 */
	@EventHandler
<<<<<<< HEAD
	public void primaryEventSourceHandler(CategoryEvent catEvent) {
=======
	public void onCategoryEventPublishedToBroker(CategoryEvent catEvent) {
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
		/**
		 * publish this event to broker to take it by the 
		 * other consumers also store this event into event store
		 */
<<<<<<< HEAD
		System.out.println("step-4: in primaryEventSourceHandler event data is--"+catEvent.toString());
		
=======
		System.out.println("step-4: in onCategoryEventPublishedToBroker event data is--"+catEvent.toString());
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
	}
	
	/**
	 * Event store Event Handler
	 * @param catEvent
	 */
	@EventHandler
<<<<<<< HEAD
	public void secondaryEventSourceHandler(CategoryEvent catEvent) {
=======
	public void onCategoryEventPublishedToEventStore(CategoryEvent catEvent) {
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
		/**
		 * publish this event to broker to take it by the 
		 * other consumers also store this event into event store
		 */
<<<<<<< HEAD
		System.out.println("step-4: in secondaryEventSourceHandler event data is--"+catEvent.toString());
=======
		System.out.println("step-4: in onCategoryEventPublishedToEventStore event data is--"+catEvent.toString());
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
	}
}
