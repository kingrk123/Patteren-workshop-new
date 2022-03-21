package com.shopifyme.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.shopifyme.gateway.outbound.UserManagementClient;

import reactor.core.publisher.Mono;

@Component
public class GatewayAuthzFilter implements GlobalFilter {

	@Autowired
	private UserManagementClient userMgmtClient;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders headers = exchange.getRequest().getHeaders();
		String headerWithBearer = headers.get("Authorization").get(0);
		//Bearer eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJOYXJzaSIsImF1dCI6IkFQUExJQ0FUSU9OX1VTRVIiLCJhdWQiOiI2Z3YwZlNsZU05bXVxb2hOQmRtbHhkQ2tiSHNhIiwibmJmIjoxNjQ2OTI2NDg0LCJhenAiOiI2Z3YwZlNsZU05bXVxb2hOQmRtbHhkQ2tiSHNhIiwic2NvcGUiOiJpbnRlcm5hbF9sb2dpbiIsImlzcyI6Imh0dHBzOlwvXC9sb2NhbGhvc3Q6OTQ0M1wvb2F1dGgyXC90b2tlbiIsImV4cCI6MTY0NjkzMDA4NCwiaWF0IjoxNjQ2OTI2NDg0LCJqdGkiOiI0ZjQ5MDZmZC00YWQ0LTRjMDUtYWI1Yy0wY2ZjODdiMzliMDAifQ.TRvuf2KHmaUck4x8hRfcbEiDbNioYwrI74pjo4EtqytwT3DvNAf7c2nNQDN_dKC8V-RyWOrwGXztS5h_p7ORTxh2pJoIUUJWA3CGEbuERs1CtoJP238nVRuFDgXI_RjjFjHhV7yo6ilcrmk42niHcMK9JHmyfYkxMlPMpFTW37I3ryn1WX5azlaJru-BMMyUVMVdh-DvK3fYc0jxKSyhPL2Ez5TQsFbR1KKSNK5t3MuiXcmcdei2TYgO6D3ggh1gG_GPuBAqFP647TCauP_1I8pLc4vQF5g8H_-GqgvXi1lxdce_ExcE6W1B-t9bHM_DNlcPTa8Pv70x-mJxDohPBw
		if(headerWithBearer != null && !headerWithBearer.isEmpty() && headerWithBearer.contains("Bearer")) {
			String accessToken = headerWithBearer.replace("Bearer ", "");
			System.out.println("access Token:\t"+accessToken);
			if(accessToken != null && !accessToken.isEmpty()) {
				try {
					Object profile = userMgmtClient.getLoggedInUserInfo(accessToken);
					if(profile != null) {
						return chain.filter(exchange);
					}else {
						return onError(exchange,HttpStatus.FORBIDDEN);
					}
				}catch (Exception e) {
					return onError(exchange,HttpStatus.FORBIDDEN);
				}
				
				
			}else {
				//forbidded
				return onError(exchange,HttpStatus.FORBIDDEN);
			}
			
		}else{
			//forbidden
			return onError(exchange,HttpStatus.FORBIDDEN);
		}
	}
	
	private Mono<Void> onError(ServerWebExchange exchange,HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

}
