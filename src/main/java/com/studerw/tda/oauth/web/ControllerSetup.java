package com.studerw.tda.oauth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class ControllerSetup {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerSetup.class);

	public ControllerSetup() {
		LOGGER.warn("**** Controller Setup constructor() - should only see me once ****");
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		LOGGER.warn("Initializing StringTrimmerEditor...");
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
