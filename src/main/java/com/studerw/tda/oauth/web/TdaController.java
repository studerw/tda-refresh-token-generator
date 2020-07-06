package com.studerw.tda.oauth.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studerw.tda.oauth.model.Credentials;
import com.studerw.tda.oauth.model.TdaResult;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TdaController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TdaController.class);

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final Environment environment;

	private final String redirectTemplate;

	private final String clientIdSuffix;

	public TdaController(Environment environment) {
		LOGGER.warn("**** TdaController constructor() - you should only see me once.*****");
		this.environment = environment;

		this.redirectTemplate = this.environment.getRequiredProperty("tda.redirect.template");
		LOGGER.info("RedirectTemplate: {}", this.redirectTemplate);

		this.clientIdSuffix = this.environment.getRequiredProperty("tda.redirect.client_id.suffix");
		LOGGER.info("clientIdSuffix: {}", this.clientIdSuffix);
	}


	@GetMapping(value = "/oauth", produces = MediaType.TEXT_HTML_VALUE)
	public String getOauth(Model model, HttpServletResponse response, HttpSession httpSession) {
		if (!model.containsAttribute("credentials")) {
			model.addAttribute("credentials", new Credentials());
		}
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		LOGGER.debug("{}", attr);
		return "oauth";
	}

	@PostMapping(value = "/oauth", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView postOauth(@Valid Credentials credentials, BindingResult result, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info("Posting credentials: {}", credentials);

		if (result.hasErrors()) {
			return new ModelAndView("oauth");
		}
		String strippedClientId = StringUtils.removeEndIgnoreCase(credentials.getClientId(), this.clientIdSuffix);

		httpSession.setAttribute("client_id", strippedClientId);

		String redirectUrl = getRedirectUrl(request, strippedClientId);
		return new ModelAndView("redirect:" + redirectUrl);
	}

	@GetMapping(value = "/oauth/redirected", produces = MediaType.TEXT_HTML_VALUE)
	public String getRedirect(Model model, HttpServletRequest request, HttpSession httpSession,
			@RequestParam(name = "code", required = true) String code) throws IOException {
		if (StringUtils.isBlank(code)) {
			throw new RuntimeException("Unable to obtain the code from TDA...");
		}

		String clientId = (String) httpSession.getAttribute("client_id");
		if (StringUtils.isBlank(clientId)) {
			throw new RuntimeException("Unable to obtain client id from the session...");
		}

		try {
			TdaResult tdaResult = postCode(request, code.trim(), clientId);
			model.addAttribute("globalMessage", "A new refresh token was generated. Any old ones cannot be used anymore.");
			model.addAttribute("tdaResult", tdaResult);
			return "response";
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@ResponseBody
	@GetMapping(value = "/help/redirectUrl", produces = MediaType.APPLICATION_JSON_VALUE)
	private String getRedirect(HttpServletRequest request, HttpServletResponse response) {
		return getRedirectUrl(request, "1234567890");
	}

	@ResponseBody
	@GetMapping(value = "/help/postRedirectUrl", produces = MediaType.APPLICATION_JSON_VALUE)
	private String getPostRedirect(HttpServletRequest request, HttpServletResponse response) {
		return getPostRedirectUrl(request);
	}

	private String getRedirectUrl(HttpServletRequest request, String clientId) {
		try {
			String currentUrl = getCurrentUrl(request);
			currentUrl += "/oauth/redirected";
			String redirectUrl = this.redirectTemplate.replace("{%redirect_uri%}", URLEncoder.encode(currentUrl, StandardCharsets.UTF_8.toString()));

			String clientIdSuffixed = StringUtils.appendIfMissingIgnoreCase(clientId, this.clientIdSuffix);
			redirectUrl = redirectUrl.replace("{%client_id%}", URLEncoder.encode(clientIdSuffixed, StandardCharsets.UTF_8.toString()));

			LOGGER.info("Creating redirectUrl: {}", redirectUrl);
			return redirectUrl;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getPostRedirectUrl(HttpServletRequest request) {
		String currentUrl = getCurrentUrl(request);
		currentUrl += "/oauth/redirected";
		return currentUrl;
	}

	private String getCurrentUrl(HttpServletRequest request) {
		try {
			String currentUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			if (currentUrl.contains(":443")) {
				currentUrl = currentUrl.replace(":443", "");
			}
			return currentUrl;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private TdaResult postCode(HttpServletRequest request, String code, String clientId) throws Exception {

		final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor("TDA_HTTP", -1)).build();
		RequestBody formBody = new FormBody.Builder()
				.add("grant_type", "authorization_code")
				.add("access_type", "offline")
				.add("client_id", clientId)
				.add("code", code)
				.add("redirect_uri", getPostRedirectUrl(request))
				.build();

		Request okReq = new Request.Builder().url("https://api.tdameritrade.com/v1/oauth2/token").post(formBody).build();

		Response okResp = client.newCall(okReq).execute();
		if (okResp.isSuccessful()) {
			try (BufferedInputStream bIn = new BufferedInputStream(okResp.body().byteStream())) {
				final TdaResult tdaResult = objectMapper.readValue(bIn, TdaResult.class);
				return tdaResult;
			}
			catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("Error obtaining refresh token: " + okResp.body().string());
	}
}
