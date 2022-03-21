package com.shopifyme.categories.events;

import java.io.Serializable;

import lombok.Data;

@Data
public class CategoryEvent implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4334028088600955112L;

	private final int id;

	private final String name;
	
	private final String alias;
	
	private final String aggregateIdentifier;
	
	//private MultipartFile fileImage;
	
	private final boolean enabled;

	public CategoryEvent(int id, String name, String alias, boolean enabled, String aggregateIdentifier) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.enabled = enabled;
		this.aggregateIdentifier = aggregateIdentifier;
	}
	
	
}
