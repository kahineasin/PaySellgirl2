package com.sellgirl.sellgirlPayWeb.configuration;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProjConfig {

	@PostConstruct
	public void beforeInit() {
	}

    @Value("${service.auth.apiUrl}")
    public String authApiUrl;

	public String getAuthApiUrl() {
		return authApiUrl;
	}

	public void setAuthApiUrl(String authApiUrl) {
		this.authApiUrl = authApiUrl;
	}
}
