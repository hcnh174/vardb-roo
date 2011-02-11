package org.vardb.users;

import java.util.Collection;
import java.util.List;

import org.dom4j.Element;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=false)
public interface UserService extends UserDetailsService
{
	//User getUserByUserId(String user_id);
	User findUserByUsername(String username);
	User findOrCreateUser(String username);
	boolean validateUsername(String username);
	boolean usernameExists(String username);
	void createAccount(User user);
	void updateUser(User user);
	void addUsers(Collection<User> users);
	List<User> getUsers();
	
	//void setPassword(User user, String password);
	//void resetPassword(String user_id, String password);
	//void changePassword(String user_id, String password);
	
	User loadUser(Element node);
}
