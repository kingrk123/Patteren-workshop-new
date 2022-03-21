package com.shopifyme.categories.handler;

import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopifyme.categories.bean.CategoryBean;
import com.shopifyme.categories.commands.CategoryCommand;

@Component
public class CategoryCommandHandler {

	@Autowired
<<<<<<< HEAD
	CommandGateway commandGateway;
	
	public void publishCategoryUpdates(CategoryBean catBean) {
		CategoryCommand event = new CategoryCommand(catBean.getId(), catBean.getName(), catBean.getAlias(), catBean.isEnabled());
		CompletableFuture<String> futureResp = commandGateway.send(event);
		if(futureResp.isDone()) {
			System.out.println("comand deleivered successfully");
		}
		
	}

=======
	private CommandGateway commandGateway;

	public void publishCategoryUpdates(CategoryBean categoryBean) {

		CategoryCommand catCommand = new CategoryCommand(categoryBean.getId(), categoryBean.getName(),
				categoryBean.getAlias(), categoryBean.isEnabled());
		CompletableFuture<String> future = commandGateway.send(catCommand);
		if (future != null && future.isDone()) {
			System.out.println("command converted to event-->event published to broker");
		} else if (future.isCompletedExceptionally()) {
			System.out.println("command converted to event-->event published to broker exceptionally");
		} else if (future.isCancelled()) {
			System.out.println("command converted to event might failed or event published to broker might failed");
		}

	}
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
}
