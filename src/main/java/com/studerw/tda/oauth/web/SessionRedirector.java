package com.studerw.tda.oauth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionRedirector implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionRedirector.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LOGGER.warn("PreHandle...");
		final HttpSession session = request.getSession(true);
		LOGGER.info("****** SESSION {} ********", session.toString());
		return true;
	}
}
