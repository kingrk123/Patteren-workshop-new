package com.shopifyme.gateway.outbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManagementClient extends BaseClient{

	@Autowired
	private UserMgmtFeignClient userMgmtFeignClient;
	
	public Object getLoggedInUserInfo(String token) {
		return userMgmtFeignClient.fetchLoggedInUserDetails(this.getHeaderMap(token));
	}
}
