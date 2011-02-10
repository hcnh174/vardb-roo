package org.vardb.tags;

public class CTagView
{
	protected Integer id;
	protected String tagtype_id;
	protected String tagidentifier;
	protected String identifier;
	protected String name;
	protected String description;
	protected String color;
	protected String bgcolor;
	protected Integer numitems;
	protected Integer parent_id;
	protected String parent_tagtype_id;
	protected String parent_identifier;
	protected String parent_name;
	protected String parent_description;

	public Integer getId(){return this.id;}
	public void setId(final Integer id){this.id=id;}

	public String getTagtype_id(){return this.tagtype_id;}
	public void setTagtype_id(final String tagtype_id){this.tagtype_id=tagtype_id;}

	public String getTagidentifier(){return this.tagidentifier;}
	public void setTagidentifier(final String tagidentifier){this.tagidentifier=tagidentifier;}
	
	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(final String identifier){this.identifier=identifier;}

	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}

	public String getDescription(){return this.description;}
	public void setDescription(final String description){this.description=description;}

	public String getColor(){return this.color;}
	public void setColor(final String color){this.color=color;}

	public String getBgcolor(){return this.bgcolor;}
	public void setBgcolor(final String bgcolor){this.bgcolor=bgcolor;}

	public Integer getNumitems(){return this.numitems;}
	public void setNumitems(final Integer numitems){this.numitems=numitems;}

	public Integer getParent_id(){return this.parent_id;}
	public void setParent_id(final Integer parent_id){this.parent_id=parent_id;}

	public String getParent_tagtype_id(){return this.parent_tagtype_id;}
	public void setParent_tagtype_id(final String parent_tagtype_id){this.parent_tagtype_id=parent_tagtype_id;}

	public String getParent_identifier(){return this.parent_identifier;}
	public void setParent_identifier(final String parent_identifier){this.parent_identifier=parent_identifier;}

	public String getParent_name(){return this.parent_name;}
	public void setParent_name(final String parent_name){this.parent_name=parent_name;}

	public String getParent_description(){return this.parent_description;}
	public void setParent_description(final String parent_description){this.parent_description=parent_description;}
}
