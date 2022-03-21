package com.shopifyme.brands.resources;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.brands.beans.BrandBean;
import com.shopifyme.brands.services.BrandService;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductBrandResource {

	@Autowired
	private BrandService productBrandService;

	@PostMapping("/product-brands")
	public ResponseEntity<Object> createBrand(@RequestPart String brandData,
			@RequestParam("fileImage") MultipartFile multipartFile) {

		if (brandData != null && !brandData.isEmpty()) {
			log.info("the data coming is:\t"+brandData);
			JSONObject json = new JSONObject(brandData);
			BrandBean brandBean = new BrandBean();
			if(json.has("name")) {
				String name = json.getString("name");
				brandBean.setName(name);
			}
			if(json.has("categories")) {
				List<String> catList = new ArrayList<>();
				JSONArray catArray = json.getJSONArray("categories");
				catArray.forEach(catObj->{
					catList.add((String) catObj);
				});
				brandBean.setCategories(catList);
			}
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
