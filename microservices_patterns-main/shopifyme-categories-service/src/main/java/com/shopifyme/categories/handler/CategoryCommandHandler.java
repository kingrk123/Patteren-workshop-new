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
}
