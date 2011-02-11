package org.vardb.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.vardb.util.CException;

@Service("loginService")
public class LoginServiceImpl implements LoginService
{
	private static final String EMPTY_PASSWORD="";
	
	public void setUser(UserDetails user)
	{
		Collection<GrantedAuthority> authorities=copyAuthorities(user);
		UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(user,EMPTY_PASSWORD,authorities);
		SecurityContextHolder.getContext().setAuthentication(token);
	}

	private Collection<GrantedAuthority> copyAuthorities(UserDetails user)
	{
		Collection<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		for (GrantedAuthority authority : user.getAuthorities())
		{
			authorities.add(new GrantedAuthorityImpl(authority.getAuthority()));
		}
		return authorities;
	}
	
	public boolean isAnonymous()
	{
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		//System.out.println("auth class="+auth.getClass().getName());
		return (auth instanceof AnonymousAuthenticationToken);
	}
	
	private String getDebugInfo()
	{
		StringBuilder buffer=new StringBuilder();
		Authentication authentication=getAuthentication();
		if (authentication!=null)
		{
			buffer.append("Authentication class="+getAuthentication().getClass().getName()+"\n");
			Object principal=authentication.getPrincipal();
			if (principal!=null)
				buffer.append("principal class="+principal.getClass().getName()+"\n");
		}
		return buffer.toString(); 
	}
	
	public UserDetails getUserDetails()
	{
		//System.out.println("getUserDetails");
		Authentication authentication=getAuthentication();
		if (authentication==null)
		{
			//System.out.println("authentication is null");
			return null;
		}
		//System.out.println("Authentication class="+authentication.getClass().getName());
		Object principal=authentication.getPrincipal();
		//System.out.println("principal class="+principal.getClass().getName());
		if (principal instanceof UserDetails)
		{
			//System.out.println("found instance of CUserDetails");
			return (UserDetails)principal;
		}
		else
		{
			//System.out.println("principal is not an instance of CUserDetails. returning null");
			return null;
		}
	}
	
	public UserDetails getUserDetails(boolean nullokay)
	{
		UserDetails details=getUserDetails();
		if (details==null)
		{
			if (nullokay)
				return null;
			throw new CException("user ID is null\n"+getDebugInfo());
		}
		return details;
	}
	
	/*
	public String getUserId(String dflt)
	{
		UserDetails details=getUserDetails();
		if (details==null)
			return dflt;
		if (!(details instanceof User))
			throw new CException("UserDetails cannot be cast to class CUserDetails: "+details.getClass().getName());
		User user=(User)details;
		if (user==null || user.getId()==null)
			return dflt;
		else return user.getId();
	}
	*/
	
	
	public Collection<GrantedAuthority> getAuthorities(final List<String> roles)
	{
		Collection<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		for (String role : roles)
		{
			authorities.add(new GrantedAuthorityImpl(role));
		}
		return authorities;
	}
	
	public String getSavedRequest(HttpServletRequest request)
	{
		HttpSession session=request.getSession(false);
		if (session==null)
			return null;
		SavedRequest savedrequest=(SavedRequest)session.getAttribute(SAVED_REQUEST);
		if (savedrequest==null)
			return null;
		return savedrequest.getRedirectUrl();//.getFullRequestUrl();
	}
	
	public AuthenticationException getLastException(HttpServletRequest request)
	{
		HttpSession session=request.getSession(false);
		if (session==null)
			return null;
		return (AuthenticationException)session.getAttribute(LAST_EXCEPTION);
	}
	
	public String getReason(HttpServletRequest request)
	{
		AuthenticationException e=getLastException(request);
		String reason=e.getMessage();
		return reason.substring(reason.lastIndexOf(':')+1);
	}
	
	public String getLastUsername(HttpServletRequest request)
	{
		HttpSession session=request.getSession(false);
		if (session==null)
			return null;
		return (String)session.getAttribute(LAST_USERNAME);
	}
	
	public AuthenticationException getUnauthorizedException(HttpServletRequest request)
	{	
		return (AuthenticationException)request.getAttribute(ACCESS_DENIED);
	}
	
	public Authentication getAuthentication()
	{	
		final SecurityContext context=SecurityContextHolder.getContext();
		if (context==null)
			return null;
		final Authentication authentication=context.getAuthentication();
		if (authentication==null)
			return null;
		return authentication;
	}
	
	public void logout(HttpServletRequest request, HttpServletResponse response)
	{
		//System.out.println("logging out user: "+request.getRemoteUser());
		forgetMe(response,"/vardb");
		endSession(request);
		clearContext();		
	}
	
	// invalidate session if there is one
	private void endSession(HttpServletRequest request)
	{
		HttpSession session=request.getSession(false);
		if (session!=null)
			session.invalidate();
	}
	
	// erase the remember me cookie
	private void forgetMe(HttpServletResponse response, String webapp)
	{
		Cookie cookie = new Cookie(REMEMBER_ME, null);
		cookie.setMaxAge(0);
		cookie.setPath(webapp); //You need to add this!!!!!
		response.addCookie(cookie);
	}
	
	private void clearContext()
	{
		SecurityContextHolder.clearContext(); //invalidate authentication
	}

	////////////////////////////////////////////////////////////////
	
	/*
	public String encodePassword(String password)
	{
		return encodePassword(password,null);
	}
	
	public String encodePassword(String password, String salt)
	{
		int strength=256;
		PasswordEncoder encoder=new ShaPasswordEncoder(strength);
		return encoder.encodePassword(password,salt);
	}
	*/
	
	/////////////////////////////////////////////////////////////////////////////////
	
	public void logLogin(LoginListener listener, ApplicationEvent evt)
	{
		if (!isAuthenticationEvent(evt))
			return;		
		AbstractAuthenticationEvent event=(AbstractAuthenticationEvent)evt;
		System.out.println("LOGIN LISTENER: "+getAuthenticationMessage(event));
		String username=getUsername(event);
		LoginService.LoginStatus status=getLoginStatus(event);
		listener.logLogin(username,status);
	}

	private LoginService.LoginStatus getLoginStatus(AbstractAuthenticationEvent event)
	{
		LoginService.LoginStatus status=LoginService.LoginStatus.LOGIN;
		if (isFailure(event))
			status=LoginService.LoginStatus.CREDENTIALS;
		return status;
	}
	
	private boolean isAuthenticationEvent(ApplicationEvent evt)
	{
		if (!(evt instanceof AbstractAuthenticationEvent))
			return false;
		if (evt instanceof InteractiveAuthenticationSuccessEvent) // the login is already logged as AuthenticationSuccessEvent
			return false;
		return true;
	}
	
	private String getAuthenticationMessage(AbstractAuthenticationEvent event)
	{
		String username=getUsername(event);
		String cls=ClassUtils.getShortName(event.getClass());
		WebAuthenticationDetails details=getDetails(event);
		String ipaddress=details.getRemoteAddress();
		String sessionid=details.getSessionId();
		
		StringBuilder buffer=new StringBuilder();
		buffer.append("User "+username+" login result: "+cls+"; ipaddress="+ipaddress+"; sessionid="+sessionid+";");
		if (isFailure(event))
			buffer.append(" exception="+getException(event).getMessage());
        return buffer.toString();
	}
	
	private AuthenticationException getException(AbstractAuthenticationEvent evt)
	{
		AbstractAuthenticationFailureEvent failure=(AbstractAuthenticationFailureEvent)evt;
		return failure.getException();
	}
	
	private boolean isFailure(AbstractAuthenticationEvent event)
	{
		return (event instanceof AbstractAuthenticationFailureEvent);
	}
	
	private String getUsername(AbstractAuthenticationEvent event)
	{
        return event.getAuthentication().getName();
	}
	
	private WebAuthenticationDetails getDetails(AbstractAuthenticationEvent event)
	{
		return (WebAuthenticationDetails)event.getAuthentication().getDetails();
	}
}
