package com.shopifyme.brands.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.brands.beans.BrandBean;
import com.shopifyme.brands.domains.Brand;
import com.shopifyme.brands.exceptions.BrandNotFoundException;
import com.shopifyme.brands.outbound.CategoriesServiceCommunicator;
import com.shopifyme.brands.outbound.feign.CategoriesServiceFeignClient;
import com.shopifyme.brands.repos.BrandRepository;

@Service
public class BrandService {
	public static final int BRANDS_PER_PAGE = 10;

	@Autowired
	private BrandRepository repo;

	@Autowired
	private CategoriesServiceFeignClient categoriesFeignClient;

	@Autowired
	private CategoriesServiceCommunicator categoriesApiCommunicator;

	public List<BrandBean> listAll() {
		List<BrandBean> brandBeanList = new ArrayList<>();
		List<Brand> brandDomainsList = repo.findAll();
		if (brandDomainsList != null && brandDomainsList.size() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			brandDomainsList.stream().forEach(brandDomain -> {
				BrandBean brandBean = new BrandBean();
				BeanUtils.copyProperties(brandDomain, brandBean);
				String categoriesList = brandDomain.getCategories();
				try {
					List<Object> catList = mapper.readValue(categoriesList, List.class);
					brandBean.setCategories(catList);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				brandBeanList.add(brandBean);
			});
		}
		return brandBeanList;
	}

	public String fetchAllCategories() {
		// return categoriesApiCommunicator.fetchAllCategories();
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		ResponseEntity<String> resp = categoriesFeignClient.fetchAllCatrgies(headerMap, Sort.DEFAULT_DIRECTION.name());
		if (null != resp && resp.getStatusCodeValue() == HttpStatus.OK.value()) {
			return resp.getBody();
		}
		return null;
	}

	/*
	 * public void listByPage(int pageNum, PagingAndSortingHelper helper) {
	 * helper.listEntities(pageNum, BRANDS_PER_PAGE, repo); }
	 */

	public BrandBean save(BrandBean brandBean, String logo) {
		Brand brandDomain = new Brand();
		BeanUtils.copyProperties(brandBean, brandDomain);

		ObjectMapper mapper = new ObjectMapper();
		try {
			String categoryJson = mapper.writeValueAsString(brandBean.getCategories());
			System.out.println("category json is:\t" + categoryJson);
			brandDomain.setCategories(categoryJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		brandDomain.setLogo(logo);
		brandDomain = repo.save(brandDomain);
		BeanUtils.copyProperties(brandDomain, brandBean);
		return brandBean;
	}

	public BrandBean save(BrandBean brandBean) {
		Brand brandDomain = new Brand();
		BeanUtils.copyProperties(brandBean, brandDomain);
		brandDomain = repo.save(brandDomain);
		BeanUtils.copyProperties(brandDomain, brandBean);
		return brandBean;
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}

		repo.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = repo.findByName(name);

		if (isCreatingNew) {
			if (brandByName != null)
				return "Duplicate";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
}
