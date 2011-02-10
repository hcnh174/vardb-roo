package org.vardb.tags;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.vardb.util.CDataType;
import org.vardb.util.CException;
import org.vardb.util.CStringHelper;

@Entity
@Table(name="tagtypes")
public class CTagType
{	
	public static final String ATTRIBUTE_DELIMITER=":";
	public static final String NAME_DELIMITER=":";
	public static final String NAME="name";
	public static final String IDENTIFIER="identifier";
	public static final String DESCRIPTION="description";
	
	protected String id="";
	protected String tablename="tags";
	protected String color="";
	protected String bgcolor="";
	protected Set<CAttributeType> attributeTypes=new LinkedHashSet<CAttributeType>();
	
	@Id
	public String getId(){return this.id;}
	public void setId(final String id){this.id=id;}

	public String getTablename(){return this.tablename;}
	public void setTablename(final String tablename){this.tablename=tablename;}
	
	public String getColor(){return this.color;}
	public void setColor(final String color){this.color=color;}

	public String getBgcolor(){return this.bgcolor;}
	public void setBgcolor(final String bgcolor){this.bgcolor=bgcolor;}
	
	@OneToMany(mappedBy="tagtype_id",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@OrderBy("id")
	public Set<CAttributeType> getAttributeTypes(){return this.attributeTypes;}
	public void setAttributeTypes(final Set<CAttributeType> attributeTypes){this.attributeTypes=attributeTypes;}
	
	public CTagType(){}
	
	public CTagType(String id)
	{
		this.id=id;
	}

	public void addAttributeType(CAttributeType attributeType)
	{
		this.attributeTypes.add(attributeType);
	}
	
	public CTag createTag(String identifier)
	{
		return new CTag(this,identifier);
	}
	
	/*
	public CTag createTag(String name)
	{
		CTag tag=new CTag(this.id,name);
		tag.setTagType(this);
		tag.setIdentifier(id+":"+name); // hack!
		return tag;
	}
	*/
	
	public CAttribute createAttribute(String name, String value)
	{
		if (isReservedName(name))
			throw new CException("Cannot create attribute with reserved name: "+name);
		CAttributeType attributeType=findAttributeType(name);
		if (attributeType==null) //hack!
		{
			System.err.println("cannot find attribute type: "+name);
			return null;
		}
		return attributeType.createAttribute(value);
	}
	
	private boolean isReservedName(String name)
	{
		return (NAME.equals(name) || IDENTIFIER.equals(name) || DESCRIPTION.equals(name));
	}
	
	////////////////////////////////////////////////////////
	
	public CAttributeType findAttributeType(String name)
	{
		for (CAttributeType attributeType : attributeTypes)
		{
			if (attributeType.getName().equals(name))
				return attributeType;
		}
		return null;
	}
	
	public CAttributeType getAttributeType(String name)
	{
		CAttributeType attributeType=findAttributeType(name);
		if (attributeType==null)
			throw new CException("attribute is not defined: ["+name+"]");
		return attributeType;
	}
	
	public CAttributeType findOrCreateAttributeType(String name, CDataType datatype)
	{
		CAttributeType attributeType=findAttributeType(name);
		if (attributeType==null)
		{
			attributeType=new CAttributeType(this.id,name,datatype);
			addAttributeType(attributeType);
		}
		else if (attributeType.getType()!=datatype)
			throw new CException("found attribute type but data type does not match: "+datatype);
		return attributeType;
	}
	
	/////////////////////////////////
	
	public void setAttribute(CTag tag, String name, Object value)
	{
		setAttribute(tag,name,value,false);
	}
	
	public void setAttribute(CTag tag, String name, Object value, boolean createIfNotExists)
	{
		if (!CStringHelper.hasContent(value))
			return;		
		if (IDENTIFIER.equals(name))
		{
			tag.setIdentifier(value.toString());
			return;
		}
		else if (NAME.equals(name))
		{
			tag.setName(value.toString());
			return;
		}
		else if (DESCRIPTION.equals(name))
		{
			tag.setDescription(value.toString());
			return;
		}
		//CAttributeType attributeType=findOrCreateAttributeType(name);
		CAttributeType attributeType;
		if (createIfNotExists)
			attributeType=findOrCreateAttributeType(name,CDataType.STRING);
		else attributeType=getAttributeType(name);
		CAttribute attribute=tag.getAttributeByTypeId(attributeType.getId());
		if (attribute==null)
			tag.addAttribute(attributeType.createAttribute(value.toString()));
		else attribute.setValue(value.toString());
	}
}