package com.studerw.tda.oauth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	SessionRedirector sessionRedirector;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionRedirector);
	}
}
