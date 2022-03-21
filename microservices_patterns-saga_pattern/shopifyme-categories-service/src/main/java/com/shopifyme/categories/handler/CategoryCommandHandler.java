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
	CommandGateway commandGateway;
	
	public void publishCategoryUpdates(CategoryBean catBean) {
		CategoryCommand event = new CategoryCommand(catBean.getId(), catBean.getName(), catBean.getAlias(), catBean.isEnabled());
		CompletableFuture<String> futureResp = commandGateway.send(event);
		if(futureResp.isDone()) {
			System.out.println("comand deleivered successfully");
		}
		
	}

}
