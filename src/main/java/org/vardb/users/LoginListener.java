package org.vardb.users;

public interface LoginListener
{
	void logLogin(String username, LoginService.LoginStatus status);
}
