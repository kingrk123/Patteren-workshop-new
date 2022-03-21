package com.shopifyme.brands.outbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CategoriesServiceCommunicator {

	@Autowired
	private RestTemplate categoriesTemplate;
	
	@Value("${outbound.api.categories.uri}")
	private String categories_api_uri;
	
	/**
	 *All categories wil fetch from the categories service api
	 * @return
	 */
	public String fetchAllCategories() {
		String categoriesList = null;
		ResponseEntity<String> resp = categoriesTemplate.getForEntity(categories_api_uri, String.class);
		if(null != resp && resp.getStatusCodeValue() == HttpStatus.OK.value()) {
			categoriesList = resp.getBody();
		}
		return categoriesList;
	}
}
