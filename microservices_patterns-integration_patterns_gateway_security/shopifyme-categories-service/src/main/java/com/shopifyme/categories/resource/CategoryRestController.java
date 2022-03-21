package com.shopifyme.categories.resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.categories.bean.CategoryBean;
import com.shopifyme.categories.domain.Category;
import com.shopifyme.categories.exceptions.CategoryNotFoundException;
import com.shopifyme.categories.service.CategoryService;
import com.shopifyme.categories.utilities.CategoryCsvExporter;
import com.shopifyme.categories.utilities.CategoryPageInfo;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CategoryRestController {

	@Value("${file.storage.location}")
	private String fileStorageLocation;
	
	@Autowired
	private CategoryService service;
	
	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name,
			@Param("alias") String alias) {
		return service.checkUnique(id, name, alias);
	}
	
	@GetMapping(value="/categories")
	public List<Category> listFirstPage(String sortDir) {
		log.info("get all categories is triggered and fetching....");
		return listByPage(1, sortDir, null);
	}
	
	@GetMapping(value="/categories/page/{pageNum}") 
	public List<Category> listByPage(@PathVariable(name = "pageNum") int pageNum, 
			String sortDir,	String keyword) {
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = service.listByPage(pageInfo, pageNum, sortDir, keyword);
		
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		/*
		 * model.addAttribute("totalPages", pageInfo.getTotalPages());
		 * model.addAttribute("totalItems", pageInfo.getTotalElements());
		 * model.addAttribute("currentPage", pageNum); model.addAttribute("sortField",
		 * "name"); model.addAttribute("sortDir", sortDir);
		 * model.addAttribute("keyword", keyword); model.addAttribute("startCount",
		 * startCount); model.addAttribute("endCount", endCount);
		 * 
		 * model.addAttribute("listCategories", listCategories);
		 * model.addAttribute("reverseSortDir", reverseSortDir);
		 * model.addAttribute("moduleURL", "/categories");
		 */
		
		return listCategories;		
	}
	
	@GetMapping("/categories/new")
	public List<Category> newCategory() {
		List<Category> listCategories = service.listCategoriesUsedInForm();
		
		
		
		return listCategories;
	}
	
	@PostMapping(value="/categories",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public String saveCategory(@RequestPart String category, final @RequestParam("file") MultipartFile file) throws IOException {
		Category categoryEntity = new Category();
		if (!file.isEmpty()) {
			
			Path filePath = Paths.get(fileStorageLocation);
			  File f1 = new File(filePath+"/"+file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'))
		    		  + UUID.randomUUID()+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.')));
			  file.transferTo(f1);
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			ObjectMapper mapper = new ObjectMapper();
			JsonParser jsonParser = mapper.createParser(category);
			CategoryBean catbean = mapper.readValue(jsonParser, CategoryBean.class);
			System.out.println(catbean.toString());
			BeanUtils.copyProperties(catbean, categoryEntity);
			categoryEntity.setImage(fileName);
			Category savedCategory = service.save(categoryEntity);
			String uploadDir = "category-images/" + savedCategory.getId();
			
			//AmazonS3Util.removeFolder(uploadDir);
			//AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
		} else {
			
			BeanUtils.copyProperties(category, categoryEntity);
			service.save(categoryEntity);
		}
		
		//ra.addFlashAttribute("message", "The category has been saved successfully.");
		BeanUtils.copyProperties(categoryEntity, category);
		return category;
	}
	
	@PutMapping(value="/categories",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public CategoryBean updateCategory(@RequestPart String category, final @RequestParam("file") MultipartFile file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jsonParser = mapper.createParser(category);
		CategoryBean catBean = mapper.readValue(jsonParser, CategoryBean.class);
		System.out.println(catBean.toString());
		
		if (!file.isEmpty()) {
			
			Path filePath = Paths.get(fileStorageLocation);
			  File f1 = new File(filePath+"/"+file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'))
		    		  + UUID.randomUUID()+file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.')));
			  file.transferTo(f1);
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			//categoryEntity.setImage(fileName);
			//catbean = service.updateCategory(catbean);
			//String uploadDir = "category-images/" + savedCategory.getId();
			
			//AmazonS3Util.removeFolder(uploadDir);
			//AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			//return catBean;
		}
		catBean = service.updateCategory(catBean);
		return catBean;
	}
	
	@PutMapping("/categories/{id}")
	public List<Category> editCategory(@PathVariable(name = "id") Integer id) {
		try {
			Category category = service.get(id);
			List<Category> listCategories = service.listCategoriesUsedInForm();
			
			/*
			 * model.addAttribute("category", category);
			 * model.addAttribute("listCategories", listCategories);
			 * model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
			 */
			
			return listCategories;			
		} catch (CategoryNotFoundException ex) {
			//ra.addFlashAttribute("message", ex.getMessage());
			//return "redirect:/categories";
		}
		return null;
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public List<Category> updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled) {
		service.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status;
		//redirectAttributes.addFlashAttribute("message", message);
		List<Category> listCategories = service.listCategoriesUsedInForm();
		return listCategories;
	}
	
	@DeleteMapping("/categories/{id}")
	public List<Category> deleteCategory(@PathVariable(name = "id") Integer id) {
		try {
			service.delete(id);
			String categoryDir = "category-images/" + id;
			//AmazonS3Util.removeFolder(categoryDir);
			
			List<Category> listCategories = service.listCategoriesUsedInForm();
			return listCategories;
		} catch (CategoryNotFoundException ex) {
			//redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return null;
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listCategoriesUsedInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
}
