package com.studerw.tda.oauth.model;

import java.util.StringJoiner;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Credentials {

	@NotBlank @Size(min = 10, max=50)
	private String clientId;

	public Credentials() {
	}

	public Credentials(String clientId) {
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Credentials.class.getSimpleName() + "[", "]")
				.add("clientId='" + clientId + "'")
				.toString();
	}
}
