package com.shopifyme.gateway.outbound;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseClient {

	@Value("${shopifyme.provider.wso2.headerName}")
	private String headerName;
	
	@Value("${shopifyme.provider.wso2.headerType}")
	private String headerType;
	
	public Map<String, String> getHeaderMap(String token){
		Map<String, String> map = new HashMap<>();
		map.put(headerName, headerType+" "+token);
		return map;
	}
}
