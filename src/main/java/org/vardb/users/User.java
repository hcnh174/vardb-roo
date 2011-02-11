package org.vardb.users;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.vardb.util.CMathHelper;

import com.google.common.collect.Lists;

@RooJavaBean
@RooToString
@RooEntity(table = "users", finders = { "findUsersByUsername" })
public class User implements UserDetails {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private Boolean enabled;
    
	protected Boolean administrator=false;

    private String firstname;

    private String lastname;

    private String email;

    private String affiliation;

    private Date created;

    private Date updated;

    public User(String username)
	{
		this.username=username;
		//this.salt=createSalt();
		this.created=new Date();
		this.updated=new Date();
	}
    
    @Transient
    public String getName() {
        String name = this.firstname + " " + this.lastname;
        return name.trim();
    }

    private String createSalt() {
        String salt = String.valueOf(CMathHelper.randomInteger(1000000));
        System.out.println("salt=" + salt);
        return salt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = new Date();
        System.out.println("onUpdate called: " + this.updated.toString());
    }
    
public boolean isEnabled(){return this.enabled;}
	
	@Transient
	public boolean isAccountNonExpired()
	{
		return true;
	}
	
	@Transient
	public boolean isAccountNonLocked()
	{
		return true;
	}
	
	@Transient
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Transient
	public List<String> getRoles()
	{
		List<String> roles=Lists.newArrayList("ROLE_USER","ROLE_LOGIN_USER");
		if (this.administrator)
			roles.add("ROLE_ADMIN");
		return roles;
	}
	
	@Transient
	public Collection<GrantedAuthority> getAuthorities()
	{
		LoginService loginService=new LoginServiceImpl();
		return loginService.getAuthorities(getRoles());
	}

	/*
	public String encodePassword(String password, PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource)
	{
		String encoded=passwordEncoder.encodePassword(password, saltSource.getSalt(this));
		System.out.println("password="+password+", encoded="+encoded);
		return encoded;
	}

	public void setPassword(String password, PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource)
	{
		setPassword(encodePassword(password,passwordEncoder,saltSource));
	}
	*/
}
