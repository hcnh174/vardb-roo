package org.vardb.mongo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class Sequence {
   
	@Id @NotNull private String accession;
    @NotNull private String sequence;
    @NotNull private Integer ntlength;
    //private Integer taxid;
    
    private List<Feature> features=new ArrayList<Feature>();
    
    
    public Sequence(){}
    
    public Sequence(String accession)
    {
    	this.accession=accession;
    }
    
    public void addFeature(Feature feature)
    {
    	features.add(feature);
    }
    
    public static class Feature
    {
    	private String type;
    	
    	public Feature(){}
    	
    	public Feature(String type)
    	{
    		this.type=type;
    		System.out.println("creating feature of type: "+type);
    	}
    }
}
