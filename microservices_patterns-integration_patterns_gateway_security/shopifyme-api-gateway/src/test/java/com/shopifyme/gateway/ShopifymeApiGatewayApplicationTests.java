package com.shopifyme.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopifyme.gateway.outbound.UserManagementClient;

@SpringBootTest
class ShopifymeApiGatewayApplicationTests {

	@Autowired
	private UserManagementClient userMgmtClient;
	
	
	@Test
	void contextLoads() {
	}

	@Test
	public void validateToken() {
		String accessToken = "eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJOYXJzaSIsImF1dCI6IkFQUExJQ0FUSU9OX1VTRVIiLCJhdWQiOiI2Z3YwZlNsZU05bXVxb2hOQmRtbHhkQ2tiSHNhIiwibmJmIjoxNjQ3MDgxNTI1LCJhenAiOiI2Z3YwZlNsZU05bXVxb2hOQmRtbHhkQ2tiSHNhIiwic2NvcGUiOiJpbnRlcm5hbF9sb2dpbiIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsImV4cCI6MTY0NzA4NTEyNSwiaWF0IjoxNjQ3MDgxNTI1LCJqdGkiOiJjOGFkOGM4MC03MmE5LTQzZmQtOGExMy1kOWE0YjM3NTVmZGUifQ.Qe-s81aYpA-NPMzBObnInPDSFZPiAnnT9tBOCU4FwoipjUBkNHT2faobS274H9YNIQc95Vv37RPa0mGkEEdgWVhVidnrbnmpuB9Dhn1Qt9sqrcl-MSL0iSBMnnSAXRgC_WMESr2cp69kPTifMlFmyCo7amFsfOfLIu5AR7KCx9EIAosuw_8gBiCLdJ4TWMTXkkOPSWlNHmxpeKH7QdXEQd4kC26wUUsJzlp7xr0AhAfrGNjxKfaMKL3VnKWTgmfEBMgttLEMeMtp2kxpK4CJldyo7XYFKj48RkeKbY5XpUshhKGJYiS08NN0ObQoPvVXwkCAJLCcetfjL66wOB1wZA";
		Object userInfo = userMgmtClient.getLoggedInUserInfo(accessToken);
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println("userInfo is:\t"+mapper.writeValueAsString(userInfo));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
