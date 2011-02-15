package org.vardb.users;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vardb.util.CBeanHelper;
import org.vardb.util.CDateHelper;
import org.vardb.util.CDom4jHelper;
import org.vardb.util.CStringHelper;
import org.vardb.util.services.EmailService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService
{
	private static final String EMAIL_PASSWORD_TEMPLATE="templates/password.ftl";
	private static final String NEWUSER_EMAIL_TEMPLATE="templates/newuser.ftl";
	
	@Resource(name="emailService") private EmailService emailService;	
	//@Resource(name="passwordEncoder") private ShaPasswordEncoder passwordEncoder;
	//@Resource(name="saltSource") private ReflectionSaltSource saltSource;

	@Resource(name = "loginService") private LoginService loginService;
	
	@Autowired private UserRepository repository;
		
	//public void setEmailService(final EmailService emailService){this.emailService=emailService;}
	//public void setRecaptcha(final ReCaptchaImpl recaptcha){this.recaptcha=recaptcha;}
	//public void setLoginService(final LoginService loginService){this.loginService=loginService;}
	
	/*
	// if the user exists, return the User object from the database
	// if the user does not exist, return null if the user ID is not stored in the acegi token
	// if the user ID is stored in the token, create a new user based on the token and add it to the database
	public User getUserById(String user_id)
	{
		User user=getDao().getUserById(user_id);
		if (user==null)
		{
			UserDetails details=(UserDetails)this.loginService.getUserDetails();
			if (!user_id.equals(details.getId())) // BREAK?
				return null;			
			//System.out.println("creating user from user details: "+CStringHelper.toString(details));			
			user=new User(details.getId(),details.getUsername(),true);
			user.persist();
		}
		return user;
	}
	*/
	
	@Override
	public UserDetails loadUserByUsername(String username)
	{
		User user=findUserByUsername(username);
		if (user==null)
			throw new UsernameNotFoundException(username);
		return user;
	}
	
	public User findUserByUsername(String username)
	{
		User user=User.findUsersByUsername(username).getSingleResult();
		if (user==null)
			return null;
		return user;
	}
	
	public User findOrCreateUser(String username)
	{
		User user=findUserByUsername(username);
		if (user==null)
		{
			user=new User(username);
			user.persist();			
		}
		return user;
	}
	
	// check if the username either doesn't already exist or already belongs to the current user
	public boolean validateUsername(String username)
	{
		if (!usernameExists(username))
		{
			System.out.println("username does not exist - okay to use: "+username);
			return true;
		}
		System.out.println("username already in use - not okay to use: "+username);
		//User user=getDao().getUserById(user_id);
		// okay if no user with this user_id exists, or the username belongs to this user_id
		//if (user!=null && user.getUsername().equals(username))
		//	return true;
		return false;
	}
	
	public boolean usernameExists(String username)
	{
		User user=User.findUsersByUsername(username).getSingleResult();
		return (user!=null);
	}
	
	public void createAccount(User user)
	{
		//user.setPassword(encodePassword(user.getPassword()));
		user.persist();
		sendNewUserEmail(user);
	}

	private void sendNewUserEmail(User user)
	{
		String subject="varDB new user alert: "+user.getName();
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(user.getEmail());
		//message.setTo(CStringHelper.convertToArray(this.emailAddresses));
		message.setSubject(subject);
		this.emailService.sendEmail(message,NEWUSER_EMAIL_TEMPLATE,"user",user);
	}
	
	public void updateUser(User user)
	{
		user.merge();
	}
	
	public void addUsers(Collection<User> users)
	{
		for (User user : users)
		{
			user.merge();
		}
	}
	
	public List<User> getUsers()
	{
		return User.findAllUsers();
	}
	
	/*
	public void resetPassword(Long id, String password)
	{
		User user=User.findUser(id);
		setPassword(user,password);
		//user.setPassword(encodePassword(password));
		user.merge();
	}	
	
	public void emailPassword(String username)
	{
		User user=getDao().findUserByUsername(username);		
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("varDB account details");
		this.emailService.sendEmail(message,EMAIL_PASSWORD_TEMPLATE,"user","username",username,"password",user.getPassword());
	}
	
	
	// @TODO log password change
	public void changePassword(Long id, String password)
	{
		User user=User.findUser(id);
		setPassword(user,password);
		//user.setPassword(encodePassword(password));
		user.merge();
	}
	*/
	
	//////////////////////////////////////////////////////////////

	/*
	public void setPassword(User user, String password)
	{
		user.setPassword(password, passwordEncoder, saltSource);
	}
	*/
	
	//////////////////////////////////////////////////
	
	public List<User> exportUsers()
	{
		return User.findAllUsers();
	}
	
	public User loadUser(Element node)
	{
		CBeanHelper beanhelper=new CBeanHelper();
		//String user_id=CDom4jHelper.getAttribute(node,"id");
		String username=CDom4jHelper.getAttribute(node,"username");
		User user=findOrCreateUser(username);
		for (Iterator<?> iter=node.elementIterator();iter.hasNext();)
		{
			Element child=(Element)iter.next();
			String name=child.getName();
			String text=CDom4jHelper.getTrimmedText(child);
			if ("created".equals(name))
				user.setCreated(CDateHelper.parse(text,CDateHelper.MMDDYYYY_PATTERN));
			//else if ("password".equals(name))
			//	setPassword(user,CStringHelper.decodeBase64(text));
			else beanhelper.setPropertyFromString(user,name,text);
		}
		user.merge();
		return user;
	}
	
	///////////////////////////////////////////////////////
	

	public void testUserRepository()
	{
		User user;
		
		for (int index=0;index<100;index++)
		{
			user=new User("abc"+index);
			user.setLastname(CStringHelper.getRandomWord(5,10));
			System.out.println("lastname="+user.getLastname());
			repository.save(user);
		}
		user=new User("me");
		user.setLastname("Lastname");
		repository.save(user);
		
		user=new User("me2");
		user.setLastname("Lastname");
		repository.save(user);
		
		//Pageable pageable=new PageRequest(0,2);
		//Page<User> users=repository.findByLastname("Lastname",pageable);
		
		//for (User curuser : repository.findAll())
		for (User curuser : repository.findByLastname("Lastname"))
		{
			System.out.println("user="+user.toString());
		}
		repository.deleteAll();
	}
}