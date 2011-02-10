package org.vardb.tags;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class CAttributeView
{
	protected Integer attribute_id;
	protected String tagtype_id;
	protected String attributetype_id;
	protected Integer tag_id;
	protected Integer parent_id;
	protected String tag_tagidentifier;
	protected String tag_identifier;
	protected String tag_name;
	protected String tag_description;
	protected Integer tag_numitems;
	protected String name;
	protected String type;
	protected String value;

	public Integer getAttribute_id(){return this.attribute_id;}
	public void setAttribute_id(final Integer attribute_id){this.attribute_id=attribute_id;}
	
	public String getTagtype_id(){return this.tagtype_id;}
	public void setTagtype_id(final String tagtype_id){this.tagtype_id=tagtype_id;}

	public String getAttributetype_id(){return this.attributetype_id;}
	public void setAttributetype_id(final String attributetype_id){this.attributetype_id=attributetype_id;}

	public Integer getTag_id(){return this.tag_id;}
	public void setTag_id(final Integer tag_id){this.tag_id=tag_id;}

	public Integer getParent_id(){return this.parent_id;}
	public void setParent_id(final Integer parent_id){this.parent_id=parent_id;}
	
	public String getTag_tagidentifier(){return this.tag_tagidentifier;}
	public void setTag_tagidentifier(final String tag_tagidentifier){this.tag_tagidentifier=tag_tagidentifier;}
	
	public String getTag_identifier(){return this.tag_identifier;}
	public void setTag_identifier(final String tag_identifier){this.tag_identifier=tag_identifier;}
	
	public String getTag_name(){return this.tag_name;}
	public void setTag_name(final String tag_name){this.tag_name=tag_name;}

	public String getTag_description(){return this.tag_description;}
	public void setTag_description(final String tag_description){this.tag_description=tag_description;}
	
	public Integer getTag_numitems(){return this.tag_numitems;}
	public void setTag_numitems(final Integer tag_numitems){this.tag_numitems=tag_numitems;}
	
	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}

	public String getType(){return this.type;}
	public void setType(final String type){this.type=type;}

	public String getValue(){return this.value;}
	public void setValue(final String value){this.value=value;}


}
