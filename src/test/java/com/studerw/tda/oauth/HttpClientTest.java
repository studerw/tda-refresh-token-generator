package com.studerw.tda.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import com.studerw.tda.oauth.web.LoggingInterceptor;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientTest.class);

	@Test
	public void testMediaType(){
		final MediaType mediaType = MediaType.get("application/x-www-form-urlencoded");
		LOGGER.info("{}", mediaType.toString());
		assertThat(mediaType).isNotNull();
	}

//	@Test
//	public void testHttpClient() throws IOException {
//		final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor("TDA_HTTP", -1)).build();
//		String clientId = "EXAMPLE@AMER.OAUTHAP";
//		RequestBody formBody = new FormBody.Builder()
//				.add("grant_type", "authorization_code")
//				.add("access_type", "offline")
//				.add("client_id", clientId)
//				.add("redirect_uri", "")
//				.build();
//
//		Request request = new Request.Builder().url("https://api.tdameritrade.com/v1/oauth2/token").post(formBody).build();
//
//		Response response = client.newCall(request).execute();
//		if (!response.isSuccessful())
//			throw new IOException("Unexpected code " + response);
//		LOGGER.info(response.body().string());
//
//	}
}
