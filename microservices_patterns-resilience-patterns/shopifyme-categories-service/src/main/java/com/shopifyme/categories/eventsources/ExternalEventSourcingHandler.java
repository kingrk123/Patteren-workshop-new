/**
 * 
 */
package com.shopifyme.categories.eventsources;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.categories.aggregate.CategoryAggregate;
import com.shopifyme.categories.events.CategoryEvent;

/**
 * @author Narsi
 *
 */
@Component
public class ExternalEventSourcingHandler {
	
	@Autowired
	private EventSourcingRepository<CategoryAggregate> eventStoreRepo;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private RabbitTemplate template;
	
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routing_key;
	
	

	//@EventSourcingHandler
	public void publshEventToAMQPBroker(CategoryEvent catEvent) {
		System.out.println("ExternalEventSourcingHandler:: publshEventToAMQPBroker "+catEvent.toString());
		CategoryAggregate catAggregate = eventStoreRepo.load(catEvent.getAggregateIdentifier()).getWrappedAggregate().getAggregateRoot();
		//if(catAggregate != null && String.valueOf(catAggregate.getId()).equals(catEvent.getAggregateIdentifier())) {
			
			System.out.println("exchange is:\t"+exchange);
			System.out.println("routing key is:\t"+routing_key);
			
			//publish to RabbitMQ Broker
			//amqpTemplate.convertAndSend(exchange, routing_key,catEvent);
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				this.template.convertAndSend(mapper.writeValueAsString(catEvent));
			} catch (JsonProcessingException | AmqpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//}
		
		
	}
	
	//@EventSourcingHandler
	public void publshEventToMQTTBroker(CategoryEvent catEvent) {
		System.out.println("ExternalEventSourcingHandler:: publshEventToMQTTBroker "+catEvent.toString());
	}
	
	//@EventSourcingHandler
	public void publshEventToFileStorage(CategoryEvent catEvent) {
		System.out.println("ExternalEventSourcingHandler:: publshEventToFileStorage "+catEvent.toString());
	}
	
	//@EventSourcingHandler
	public void publshEventToCacheStorage(CategoryEvent catEvent) {
		System.out.println("ExternalEventSourcingHandler:: publshEventToFileStorage "+catEvent.toString());
	}
}
