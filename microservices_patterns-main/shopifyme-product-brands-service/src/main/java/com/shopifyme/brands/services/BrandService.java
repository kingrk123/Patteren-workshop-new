package com.shopifyme.brands.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopifyme.brands.beans.BrandBean;
import com.shopifyme.brands.domains.Brand;
import com.shopifyme.brands.exceptions.BrandNotFoundException;
import com.shopifyme.brands.repos.BrandRepository;

@Service
public class BrandService {
	public static final int BRANDS_PER_PAGE = 10;
	
	@Autowired
	private BrandRepository repo;
	
	public List<BrandBean> listAll() {
		List<BrandBean> brandBeanList = new ArrayList<>();
		List<Brand> brandDomainsList = repo.findAll();
		if(brandDomainsList != null && brandDomainsList.size() >0) {
			brandDomainsList.stream().forEach(brandDomain->{
				BrandBean brandBean = new BrandBean();
				BeanUtils.copyProperties(brandDomain, brandBean);
				brandBeanList.add(brandBean);
			});
		}
		return brandBeanList;
	}
	
	
	/*
	 * public void listByPage(int pageNum, PagingAndSortingHelper helper) {
	 * helper.listEntities(pageNum, BRANDS_PER_PAGE, repo); }
	 */
	 
	
	public BrandBean save(BrandBean brandBean, String logo) {
		Brand brandDomain = new Brand();
		BeanUtils.copyProperties(brandBean, brandDomain);
		brandDomain.setLogo(logo);
		brandDomain = repo.save(brandDomain);
		BeanUtils.copyProperties(brandDomain, brandBean);
		return brandBean;
	}
	
	public BrandBean save(BrandBean brandBean) {
		Brand brandDomain = new Brand();
		BeanUtils.copyProperties(brandBean, brandDomain);
		repo.save(brandDomain);
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
			if (brandByName != null) return "Duplicate";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
}
