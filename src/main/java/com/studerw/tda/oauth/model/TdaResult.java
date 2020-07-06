package com.studerw.tda.oauth.model;

import java.io.Serializable;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TdaResult implements Serializable {

	private final static long serialVersionUID = -7035699616977210547L;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("scope")
	private String scope;

	@JsonProperty("expires_in")
	private Integer expiresIn;

	@JsonProperty("refresh_token_expires_in")
	private Integer refreshTokenExpiresIn;

	@JsonProperty("token_type")
	private Integer tokenType;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", TdaResult.class.getSimpleName() + "[", "]")
				.add("accessToken='" + accessToken + "'")
				.add("refreshToken='" + refreshToken + "'")
				.add("scope='" + scope + "'")
				.add("expiresIn=" + expiresIn)
				.add("refreshTokenExpiresIn=" + refreshTokenExpiresIn)
				.add("tokenType=" + tokenType)
				.toString();
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Integer getRefreshTokenExpiresIn() {
		return refreshTokenExpiresIn;
	}

	public void setRefreshTokenExpiresIn(Integer refreshTokenExpiresIn) {
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}

	public Integer getTokenType() {
		return tokenType;
	}

	public void setTokenType(Integer tokenType) {
		this.tokenType = tokenType;
	}
}
