package com.shopifyme.brands.interceptor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryLookupRepository extends JpaRepository<CategoryLookupEntity, String> {
	CategoryLookupEntity findByProductIdOrTitle(String productId, String title);
}
