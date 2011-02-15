package org.vardb.users;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public interface LoginService
{
	String REMEMBER_ME=TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
	String LAST_EXCEPTION=WebAttributes.AUTHENTICATION_EXCEPTION;
	String ACCESS_DENIED=WebAttributes.ACCESS_DENIED_403;
	String LAST_USERNAME=UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY;
	//String SAVED_REQUEST=WebAttributes.SAVED_REQUEST;

	void setUser(UserDetails user);
	boolean isAnonymous();
	UserDetails getUserDetails();
	UserDetails getUserDetails(boolean nullokay);
	//String getUserId(String dflt);
	Collection<GrantedAuthority> getAuthorities(final List<String> roles);
	//String getSavedRequest(HttpServletRequest request);
	AuthenticationException getLastException(HttpServletRequest request);
	String getReason(HttpServletRequest request);
	String getLastUsername(HttpServletRequest request);
	AuthenticationException getUnauthorizedException(HttpServletRequest request);
	Authentication getAuthentication();
	void logout(HttpServletRequest request, HttpServletResponse response);
	//String encodePassword(String password);
	//String encodePassword(String password, String salt);
	void logLogin(LoginListener listener, ApplicationEvent evt);
	
	public enum LoginStatus
	{
		LOGIN,
		LOGOUT,
		CREDENTIALS,
		EXPIRED,
		DISABLED,
		CONCURRENT,
		LOCKED
	};
}
