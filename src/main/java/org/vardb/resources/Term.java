package org.vardb.resources;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(table = "terms")
public class Term
{
	protected String identifier;
	protected String term="";
	protected String definition="";
	protected String pages="";
	
	public Term() {}
	
	public Term(String identifier)
	{
		this.identifier=identifier;
	}
}