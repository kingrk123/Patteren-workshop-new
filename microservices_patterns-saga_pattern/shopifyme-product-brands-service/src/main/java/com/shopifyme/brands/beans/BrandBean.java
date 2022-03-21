package com.shopifyme.brands.beans;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class BrandBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	
	//private String logo;
	
	private List<Object> categories;	
}
