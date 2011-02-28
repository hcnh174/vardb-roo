package org.vardb.mongo;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class Page {
   
    @Id private String identifier;
    @NotNull private String title;
    @NotNull private String text;
    
    /*
    private List<Feature> features=new ArrayList<Feature>();
    
    public class Feature
    {
    	
    }
    */
}
