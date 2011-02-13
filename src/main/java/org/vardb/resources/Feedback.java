package org.vardb.resources;

import java.util.Date;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.vardb.users.User;
import org.vardb.util.CBeanHelper;
import org.vardb.util.CStringHelper;

@RooJavaBean
@RooToString
@RooEntity(table = "feedback")
public class Feedback
{
	protected String name;
	protected String affiliation;
	protected String email;
	protected String purpose;
	protected String comments;
	protected Date date;
	
	public Feedback() {}
	
	public Feedback(String name, String affiliation, String email, String purpose, String comments)
	{
		this.name=name.trim();
		this.affiliation=affiliation.trim();
		this.email=email.trim();
		this.purpose=purpose.trim();
		this.comments=comments.trim();
		this.date=new Date();
		if (CStringHelper.isEmpty(this.name))
			this.name="varDB user";
	}
	
	public Feedback(User user)
	{
		CBeanHelper beanhelper=new CBeanHelper();
		beanhelper.copyProperties(this,user);
		this.purpose="COMMENT";
		this.comments="";
	}
}