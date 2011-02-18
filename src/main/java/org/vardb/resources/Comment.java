package org.vardb.resources;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(table = "comments")
public class Comment
{
	private String username;
	private String type;
	private String identifier;
	private String text;
	private Date date;
	
	public Comment() {}
	
	public Comment(String username, String type, String identifier, String text)
	{
		this.username=username;
		this.type=type;
		this.identifier=identifier;
		this.text=text;
		this.date=new Date();
	}
}