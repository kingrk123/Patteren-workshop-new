package com.shopifyme.gateway.outbound;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.shopifyme.gateway.FeignConfiguration;

import feign.Headers;


@FeignClient(name = "user-mgment-feign",url = "${shopifyme.provider.wso2.base-uri}",configuration = FeignConfiguration.class)
public interface UserMgmtFeignClient {


	@GetMapping(value = "/scim2/Me")
	@Headers("${headerMap}")
	public Object  fetchLoggedInUserDetails(@RequestHeader final Map<String, String> headerMap);
}
