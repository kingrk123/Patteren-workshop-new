package com.shopifyme.brands.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.brands.beans.BrandBean;
import com.shopifyme.brands.services.BrandService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductBrandResource {

	@Autowired
	private BrandService productBrandService;

	@PostMapping(value="/product-brands",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> createBrand(@RequestPart String brandData,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		if (brandData != null && !brandData.isEmpty()) {
			log.info("the data coming is:\t"+brandData);
			ObjectMapper mapper = new ObjectMapper();
			JsonParser brandDataJsonParser = mapper.createParser(brandData);
			BrandBean brandBean = mapper.readValue(brandDataJsonParser, BrandBean.class);
			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				System.out.println("file name is :\t" + fileName);
				//brandBean.setLogo(fileName);

				brandBean = productBrandService.save(brandBean, fileName);
				String uploadDir = "brand-logos/" + brandBean.getId();
				System.out.println("upload dir is:\t" + uploadDir);
				// AmazonS3Util.removeFolder(uploadDir);
				// AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			} else {
				brandBean = productBrandService.save(brandBean);
			}
			if (brandBean != null && brandBean.getId() > 0) {
				return ResponseEntity.status(HttpStatus.CREATED).body(brandBean);
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(brandBean);
			}
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(brandData);
		}

	}

	@PutMapping("/product-brands")
	public BrandBean updateBrand(@RequestPart String brandBean, @RequestParam("fileImage") MultipartFile file) {

		return null;
	}

	@GetMapping("/product-brands")
	public ResponseEntity<List<BrandBean>> listAllBrands() {
		List<BrandBean> brandBeanList = productBrandService.listAll();
		if(brandBeanList != null && brandBeanList.size() >0) {
		  return ResponseEntity.status(HttpStatus.OK).body(brandBeanList);
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(brandBeanList);
		}
	}

	@GetMapping("/product-brands/{id}")
	public BrandBean getBrandById(@PathVariable("id") int barndId) {

		return null;
	}

	@DeleteMapping("/product-brands/{id}")
	public BrandBean deleteBrandById(@PathVariable("id") int barndId) {

		return null;
	}
	
	@GetMapping("/product-brands/categories")
	//@CircuitBreaker(name = "product-categories-circuit",fallbackMethod = "getAllCategoriesFallback")
	//@Retry(name = "product-categories-retry", fallbackMethod = "getAllCategoriesFallback")
	@RateLimiter(name = "product-categories-rate-limiter",fallbackMethod = "retryCategoriesFallback")
	public ResponseEntity<Map<String, String>> getAllCategoriesOfBrands() {
		Map<String, String> respMap = new HashMap<>();
		log.info("invoking category service started...");
		String categroies = productBrandService.fetchAllCategories();
		log.info("category service response came");
		if(categroies != null && !categroies.isEmpty()) {
			/**
			 * json string needs to be convert to json object
			 */
			respMap.put("data", categroies);
			return ResponseEntity.ok(respMap);
		}else {
			respMap.put("errorMessage", "No Categories Found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respMap);
		}
		
	}
	
	/**
	 * when any time category service will not be reachable and 
	 * will thrown any exception, then this fall back method
	 * will execute. the fall back will only will give the response
	 * in the case of failure only.
	 * 
	 * @param th
	 * @return
	 */
	private ResponseEntity<Map<String, String>> getAllCategoriesFallback(Throwable th){
		Map<String, String> respMap = new HashMap<>();
		respMap.put("message", "Category Service is down at this moment! please try after some time");
		return ResponseEntity.ok(respMap);
	}
	
	private ResponseEntity<Map<String, String>> retryCategoriesFallback(Throwable th){
		Map<String, String> respMap = new HashMap<>();
		respMap.put("message", "Category Service is overloaded at this moment! please try after some 3 seconds");
		return ResponseEntity.ok(respMap);
	}
	
	/*
	 * @GetMapping("/product-brands/page/{pageNum}") public String listByPage(
	 * 
	 * @PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands")
	 * PagingAndSortingHelper helper,
	 * 
	 * @PathVariable(name = "pageNum") int pageNum ) {
	 * brandService.listByPage(pageNum, helper); return "brands/brands"; }
	 */
}
