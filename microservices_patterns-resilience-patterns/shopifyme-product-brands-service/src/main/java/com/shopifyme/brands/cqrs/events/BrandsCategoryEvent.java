/**
 * 
 */
package com.shopifyme.brands.cqrs.events;

import java.io.Serializable;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

/**
 * @author IM-LP-1763
 *
 */
@Data
public class BrandsCategoryEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4016036255856105580L;
	
	private int id;
	private String name;
	private String alias;
	
	@TargetAggregateIdentifier
	private long aggregateIdentifier;
	
	private boolean enabled; 

}
