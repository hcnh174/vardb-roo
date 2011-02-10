package org.vardb.tags;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;
import org.vardb.util.CDataType;

@Entity
@Table(name="attributetypes")
public class CAttributeType
{	
	protected String id;
	protected String tagtype_id;
	protected Boolean inline=false;
	protected String name;
	protected CDataType type=CDataType.STRING;
	protected String description="";
	protected Boolean sortable=true;
	protected Boolean show=true;
	
	@Id @JsonProperty
	public String getId(){return this.id;}
	public void setId(String id){this.id=id;}

	@JsonProperty
	public String getTagtype_id(){return this.tagtype_id;}
	public void setTagtype_id(final String tagtype_id){this.tagtype_id=tagtype_id;}

	public Boolean getInline(){return this.inline;}
	public void setInline(final Boolean inline){this.inline=inline;}
	
	@JsonProperty
	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}
	
	@JsonProperty
	@Enumerated(EnumType.STRING)
	public CDataType getType(){return this.type;}
	public void setType(final CDataType type){this.type=type;}

	@JsonProperty
	public String getDescription(){return this.description;}
	public void setDescription(final String description){this.description=description;}

	@JsonProperty
	public Boolean getSortable(){return this.sortable;}
	public void setSortable(final Boolean sortable){this.sortable=sortable;}

	@JsonProperty
	public Boolean getShow(){return this.show;}
	public void setShow(final Boolean show){this.show=show;}
	
	public CAttributeType(){}
	
	public CAttributeType(String tagtype_id, String name)
	{
		this.tagtype_id=tagtype_id;
		this.id=tagtype_id+CTagType.NAME_DELIMITER+name;
		this.name=name;
		this.description=name;
	}
	
	public CAttributeType(String tagtype_id, String name, CDataType type)
	{
		this(tagtype_id,name);
		this.type=type;
	}
	
	public CAttribute createAttribute(String value)
	{
		CAttribute attribute=new CAttribute(this,value);
		return attribute;
	}
}