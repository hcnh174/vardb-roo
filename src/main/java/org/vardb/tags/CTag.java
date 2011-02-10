package org.vardb.tags;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="tags")
public class CTag
{
	protected Integer id;
	protected String tagtype_id;
	protected Integer parent_id;
	protected String tagidentifier="";
	protected String identifier="";
	protected String name="";
	protected String description="";
	protected String color="";
	protected String bgcolor="";
	protected Integer numitems=0;
	protected Integer left;//=0;
	protected Integer right;//=0;
	
	protected CTagType tagType;
	protected CTag parent;
	protected Set<CAttribute> attributes=new LinkedHashSet<CAttribute>();
	protected Set<CTag> tags=new LinkedHashSet<CTag>();
	
	@Id	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	public Integer getId(){return this.id;}
	public void setId(Integer id){this.id=id;}

	@Column(insertable=false,updatable=false)
	public String getTagtype_id(){return this.tagtype_id;}
	public void setTagtype_id(final String tagtype_id){this.tagtype_id=tagtype_id;}
	
	@Column(insertable=false,updatable=false)
	public Integer getParent_id(){return this.parent_id;}
	public void setParent_id(final Integer parent_id){this.parent_id=parent_id;}
	
	public String getTagidentifier(){return this.tagidentifier;}
	public void setTagidentifier(final String tagidentifier){this.tagidentifier=tagidentifier;}
	
	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(final String identifier){this.identifier=identifier;}
	
	public String getName(){return this.name;}
	public void setName(String name){this.name=name;}
	
	public String getDescription(){return this.description;}
	public void setDescription(final String description){this.description=description;}
	
	public String getColor(){return this.color;}
	public void setColor(final String color){this.color=color;}

	public String getBgcolor(){return this.bgcolor;}
	public void setBgcolor(final String bgcolor){this.bgcolor=bgcolor;}

	public Integer getNumitems(){return this.numitems;}
	public void setNumitems(final Integer numitems){this.numitems=numitems;}

	@Column(name="lft")
	public Integer getLeft(){return this.left;}
	public void setLeft(Integer left){this.left=left;}

	@Column(name="rght")
	public Integer getRight(){return this.right;}
	public void setRight(Integer right){this.right=right;}

	@Transient
	public boolean getHaschildren()
	{
		return (this.right-this.left==1);			
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent_id")
	public CTag getParent(){return this.parent;}
	public void setParent(final CTag parent){this.parent=parent;}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tagtype_id")
	public CTagType getTagType(){return this.tagType;}
	public void setTagType(final CTagType tagType){this.tagType=tagType;}
	
	@OneToMany(mappedBy="tag_id",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@OrderBy("id")
	public Set<CAttribute> getAttributes(){return this.attributes;}
	public void setAttributes(final Set<CAttribute> attributes){this.attributes=attributes;}

	@OneToMany(mappedBy="parent_id",cascade=CascadeType.ALL)
	@OrderBy("name")
	public Set<CTag> getTags(){return this.tags;}
	public void setTags(final Set<CTag> tags){this.tags=tags;}
	
	public CTag(){}
	
	public CTag(CTagType tagtype, String identifier)
	{
		this.tagType=tagtype;
		this.tagidentifier=tagtype.getId()+CTagType.NAME_DELIMITER+identifier;
		this.identifier=identifier;
		this.name=identifier;
	}
	
	@Transient
	public CAttribute getAttributeByTypeId(String atttype_id)
	{
		for (CAttribute attribute : this.attributes)
		{
			if (attribute.getAttributetype_id()!=null && attribute.getAttributetype_id().equals(atttype_id))
				return attribute;
		}
		return null;
	}
	
	public void addAttribute(CAttribute attribute)
	{
		if (attribute==null)
			return; //hack!
		if (!this.attributes.contains(attribute))
		{
			attribute.setTag(this);
			this.attributes.add(attribute);
		}	
	}
		
	public CTag getTag(String name)
	{
		for (CTag tag : this.tags)
		{
			if (tag.getName().equals(name))
				return tag;
		}
		return null;
	}
	
	public void addTag(CTag tag)
	{
		tag.setParent(this);
		this.tags.add(tag);
	}

	
	private static Integer counter=0;
	
	public void index()
	{
		for (CTag tag : this.tags)
		{
			index(tag);
		}
	}

	private void index(CTag tag)
	{
		tag.setLeft(CTag.counter++);
		for (CTag child : tag.getTags())
		{
			index(child);
		}
		tag.setRight(CTag.counter++);
		//taxon.setInitialized(true);
	}
}