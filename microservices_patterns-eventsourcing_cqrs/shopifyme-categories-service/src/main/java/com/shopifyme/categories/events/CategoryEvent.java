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
	
<<<<<<< HEAD
	private final String aggregateIdentifier;
	
=======
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
	//private MultipartFile fileImage;
	
	private final boolean enabled;

<<<<<<< HEAD
	public CategoryEvent(int id, String name, String alias, boolean enabled, String aggregateIdentifier) {
=======
	public CategoryEvent(int id, String name, String alias, boolean enabled) {
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.enabled = enabled;
<<<<<<< HEAD
		this.aggregateIdentifier = aggregateIdentifier;
=======
>>>>>>> 919217352a57094ed65d88e3e538e7af00dfaf8d
	}
	
	
}
