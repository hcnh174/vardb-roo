package org.vardb.tags;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="attributes")
public class CAttribute
{	
	protected Integer id;
	protected String attributetype_id;
	protected Integer tag_id;
	protected String value="";
	protected CTag tag;
	protected CAttributeType attributeType;
	
	@Id	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	public Integer getId(){return this.id;}
	public void setId(Integer id){this.id=id;}

	@Column(insertable=false,updatable=false)
	public String getAttributetype_id(){return this.attributetype_id;}
	public void setAttributetype_id(final String attributetype_id){this.attributetype_id=attributetype_id;}
	
	@Column(insertable=false,updatable=false)
	public Integer getTag_id(){return this.tag_id;}
	public void setTag_id(final Integer tag_id){this.tag_id=tag_id;}

	public String getValue(){return this.value;}
	public void setValue(final String value){this.value=value;}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tag_id")
	public CTag getTag(){return this.tag;}
	public void setTag(final CTag tag){this.tag=tag;}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="attributetype_id")
	public CAttributeType getAttributeType(){return this.attributeType;}
	public void setAttributeType(final CAttributeType attributeType){this.attributeType=attributeType;}
	
	public CAttribute(){}

	public CAttribute(CAttributeType attributeType, String value)
	{
		this.value=value;
		this.attributeType=attributeType;
	}
}