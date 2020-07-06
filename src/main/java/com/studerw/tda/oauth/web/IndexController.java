package com.studerw.tda.oauth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String indexRedirect() {
		return "redirect:/oauth";
	}
}
