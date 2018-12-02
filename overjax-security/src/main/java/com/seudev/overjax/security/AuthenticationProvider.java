package com.seudev.overjax.security;

public interface AuthenticationProvider {

	public default String getAuthenticationScheme() {
		return "bearer";
	}

	public String getRealm();

	public default String getToken(String authorizationHeader) {
		if (authorizationHeader != null) {
			authorizationHeader = authorizationHeader.trim();
			String authenticationScheme = getAuthenticationScheme();
			if (authorizationHeader.length() > authenticationScheme.length()) {
				if (authorizationHeader.substring(0, authenticationScheme.length()).equalsIgnoreCase(authenticationScheme)) {
					return authorizationHeader.substring(authenticationScheme.length()).trim();
				}
			}
		}
		return null;
	}

	public TokenAuthentication validate(String tokenString) throws Exception;

}
