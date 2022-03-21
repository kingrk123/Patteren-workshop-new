/**
 * 
 */
package com.shopifyme.categories.config;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shopifyme.categories.aggregate.CategoryAggregate;

/**
 * @author Narsi
 *
 */
@Configuration
public class EventSourcingConfig {

	@Bean
	public EventSourcingRepository<CategoryAggregate>  buildEventSourceStore(EventStore eventStore){
		
		return EventSourcingRepository.builder(CategoryAggregate.class)
		                              .eventStore(eventStore)
		                              .build();
	}
}
