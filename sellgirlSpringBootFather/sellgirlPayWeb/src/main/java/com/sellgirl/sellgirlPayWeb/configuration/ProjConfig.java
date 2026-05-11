package com.sellgirl.sellgirlPayWeb.configuration;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayService.product.model.ResourceType;

@Component
public class ProjConfig {

	@PostConstruct
	public void beforeInit() {
	}

    @Value("${service.auth.apiUrl}")
    public String authApiUrl;
    public ResourceType[] freeResource;
    public Integer signDay;


	public String getAuthApiUrl() {
		return authApiUrl;
	}

	public void setAuthApiUrl(String authApiUrl) {
		this.authApiUrl = authApiUrl;
	}

	public ResourceType[] getFreeResource() {
		return freeResource;
	}

    @Value("${freeResource:}")
	public void setFreeResource(ResourceType[] freeResource) {
		this.freeResource = freeResource;
	}

	public Integer getSignDay() {
		return signDay;
	}

    @Value("${signDay:10}")
	public void setSignDay(Integer signDay) {
		this.signDay = signDay;
	}
}
