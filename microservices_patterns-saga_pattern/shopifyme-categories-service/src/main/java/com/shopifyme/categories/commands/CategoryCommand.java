package com.shopifyme.categories.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class CategoryCommand {

	@TargetAggregateIdentifier
	private final int id;

	private final String name;
	
	private final String alias;
	
	//private MultipartFile fileImage;
	
	private final boolean enabled;

	public CategoryCommand(int id, String name, String alias, boolean enabled) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.enabled = enabled;
	}
	
	
}
