package org.vardb.resources;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(table="resources")
public abstract class Resource {

    @NotNull
    private String identifier;

    @NotNull
    private String name;

    @NotNull
    private String description;
    
    @ElementCollection
    private Map<String,String> attributes = new LinkedHashMap<String,String>();
    
    public void setAttribute(String name, String value)
    {
    	attributes.put(name, value);
    }
    
    public String getAttribute(String name)
    {
    	return attributes.get(name);
    }
}
