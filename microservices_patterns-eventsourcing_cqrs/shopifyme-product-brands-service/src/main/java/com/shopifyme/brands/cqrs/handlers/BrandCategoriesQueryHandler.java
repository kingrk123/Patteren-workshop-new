package com.shopifyme.brands.cqrs.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.brands.cqrs.events.BrandsCategoryEvent;
import com.shopifyme.brands.domains.Brand;
import com.shopifyme.brands.repos.BrandRepository;

@Service
public class BrandCategoriesQueryHandler {

	@Autowired
	private QueryGateway queryGateway;

	@Autowired
	private BrandRepository repo;

	// @QueryHandler
	@EventSourcingHandler
	public void getCategoryEventFromEventStore(BrandsCategoryEvent event) {
		// queryGateway.
		System.out.println("query handler to recieve the event :\t" + event.getAggregateIdentifier());
		ObjectMapper mapper = new ObjectMapper();
		List<Brand> updatedBrands = new ArrayList<>();
		List<Brand> brandsList = repo.findAll();
		List<JSONObject> jsonObjList = new ArrayList<JSONObject>();
		if (brandsList != null && brandsList.size() > 0) {
			brandsList.stream().forEach(brand -> {
				String categories = brand.getCategories();
				System.out.println("categories are:\t" + categories);
				JSONArray jsonArray = new JSONArray(categories);
				jsonArray.forEach(jsonObj -> {
					System.out.println("json obj is :\t" + jsonObj);
					JSONObject json = (JSONObject) jsonObj;
					int id = json.getInt("id");
					if (id == event.getId()) {
						json.put("name", event.getName());
						jsonObjList.add(json);
					} else {
						// un mateched json
						jsonObjList.add(json);
					}
				});
				System.out.println("jsonObjectList is:\t" + jsonObjList.toString());
				brand.setCategories(jsonObjList.toString());
				updatedBrands.add(brand);

			});

			repo.saveAll(updatedBrands);
		}
	}
}
