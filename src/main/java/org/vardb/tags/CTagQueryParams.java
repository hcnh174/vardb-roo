package org.vardb.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.vardb.util.CIdList;
import org.vardb.util.CIdentifierListHelper;
import org.vardb.util.CPaging;
import org.vardb.util.CSqlBuilder;
import org.vardb.util.CStringHelper;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class CTagQueryParams extends CPaging
{
	protected String tagtype;
	protected CIdList ids=new CIdList();
	protected List<String> identifiers=new ArrayList<String>();
	protected List<String> attributes=new ArrayList<String>();
	protected List<String> sorts=new ArrayList<String>();
	protected List<String> extracols=CStringHelper.splitAsList("tag_identifier,tag_name,tag_description,tag_numitems");
	protected List<String> extracoltypes=CStringHelper.splitAsList("INTEGER,TEXT,TEXT,INTEGER");
	
	public String getTagtype(){return this.tagtype;}
	public void setTagtype(final String tagtype){this.tagtype=tagtype;}
	
	public List<String> getAttributes(){return this.attributes;}
	public void setAttributes(final List<String> attributes){this.attributes=attributes;}
	
	public CTagQueryParams(String tagtype)
	{
		this.tagtype=tagtype;
	}
	
	public CTagQueryParams(String tagtype, CIdList ids)
	{
		this(tagtype);
		this.ids.addIds(ids);
	}
	
	public CTagQueryParams(String tagtype, Collection<String> identifiers)
	{
		this(tagtype);
		this.identifiers.addAll(identifiers);
	}
	
	public void setPaging(int start, int pagesize)
	{
		this.paged=true;
		this.start=start;
		this.pagesize=pagesize;
	}
	
	public CTagQueryParams addId(Integer id)
	{
		this.ids.addId(id);
		return this;
	}
	
	public CTagQueryParams addIds(Collection<Integer> ids)
	{
		this.ids.addIds(ids);
		return this;
	}

	public CTagQueryParams addIds(CIdList ids)
	{
		this.ids.addIds(ids);
		return this;
	}
	
	public CTagQueryParams addIdentifier(String identifier)
	{
		this.identifiers.add(identifier);
		return this;
	}
	
	public CTagQueryParams addIdentifiers(Collection<String> identifier)
	{
		this.identifiers.addAll(identifier);
		return this;
	}
	
	public CTagQueryParams addAttribute(String attribute)
	{
		this.attributes.add(attribute);
		return this;
	}
	
	public CTagQueryParams addAttributes(Collection<String> attributes)
	{
		this.attributes.addAll(attributes);
		return this;
	}
	
	public CTagQueryParams addAttributes(String str)
	{
		return addAttributes(CStringHelper.splitAsList(str));
	}
	
	public CTagQueryParams addSort(String sort)
	{
		this.sorts.add(sort);
		return this;
	}
	
	public CTagQueryParams addSorts(Collection<String> sorts)
	{
		this.sorts.addAll(sorts);
		return this;
	}
	
	public CTagQueryParams addSorts(Collection<String> sorts, Collection<String> dirs)
	{
		Iterator<String> diriter=dirs.iterator();
		for (String sort : sorts)
		{
			String dir=diriter.next();
			addSort(sort+" "+dir);
		}
		return this;
	}
	
	public String getCountSql()
	{
		//select count(*) from vw_tags
		//where tagtype_id='patient'
		CSqlBuilder builder=new CSqlBuilder();
		builder.select("count(*) as total");
		builder.from("vw_tags");
		builder.where("tagtype_id='"+this.tagtype+"'");
		getWhereList(builder);
		return builder.toString();
	}
	
	public String getCountsSql()
	{
		//select distinct name, value, count(*) as count
		//from vw_attributes
		//where tag_id in (select ...) 
		//group by name, value
		//order by name, count desc
		CSqlBuilder builder=new CSqlBuilder();
		builder.select("distinct name").select("value").select("count(*) as count");
		builder.from("vw_attributes");
		builder.where("tagtype_id='"+this.tagtype+"'");
		builder.where("name in ("+escapeList(this.attributes,false)+")");
		getWhereList(builder);
		builder.groupby("name").groupby("value");
		builder.orderby("name").orderby("count desc");
		return builder.toString();
	}
	
	public String getSql()
	{
		/*
	 	select temppatients.*
		from crosstab
		(
			'select tag_id, tag_identifier, tag_name, tag_description, tag_numitems, name, value from vw_attributes where tagtype_id=''patient'' and name in (''DBno'',''æ‚£è€…å��'',''ãƒ•ãƒªã‚¬ãƒŠ'',''ç”Ÿå¹´æœˆæ—¥'')',
			'select * from unnest(array[''DBno'',''æ‚£è€…å��'',''ãƒ•ãƒªã‚¬ãƒŠ'',''ç”Ÿå¹´æœˆæ—¥'']) i'
		) as temppatients(tag_id integer, tag_identifier text, tag_name text, tag_description text, tag_numitems INTEGER, DBno text, æ‚£è€…å�� text, ãƒ•ãƒªã‚¬ãƒŠ text, ç”Ÿå¹´æœˆæ—¥ text)
		*/
		CSqlBuilder builder=new CSqlBuilder();
		builder.select("patient.*");
		builder.from("crosstab(\n'"+getCrosstab1()+"',\n'"+getCrosstab2()+"'\n) as "+getRowdef());
		getWhereList(builder);
		getSortList(builder);
		getPaging(builder);
		return builder.toString();
	}
	
	private String getRowdef()
	{
		List<String> defs=new ArrayList<String>();
		defs.add("tag_id INTEGER");
		for (int index=0;index<this.extracols.size();index++)
		{
			defs.add(this.extracols.get(index)+" "+this.extracoltypes.get(index));
		}
		for (String attribute : attributes)
		{
			defs.add(attribute+" TEXT");
		}
		return " "+this.tagtype+"("+CStringHelper.join(defs,", ")+")";
	}
	
	private String getCrosstab1()
	{	
		CSqlBuilder builder=new CSqlBuilder();
		builder.select("tag_id");
		for (String extracol : extracols)
		{
			builder.select(extracol);
		}
		builder.select("name");
		builder.select("value");
		builder.from("vw_attributes");
		builder.where("tagtype_id=''"+this.tagtype+"''");
		builder.where("name in ("+escapeList(this.attributes,true)+")");
		return builder.toString();
	}
	
	private String getCrosstab2()
	{
		CSqlBuilder builder=new CSqlBuilder();
		builder.select("*");
		builder.from("unnest(array["+escapeList(this.attributes,true)+"]) i");
		return builder.toString();
	}
	
	private String escapeList(Collection<String> attributes, boolean nested)
	{
		final String quotes=nested ? "''" : "'";
		Collection<String> list=Collections2.transform(this.attributes, new Function<String,String>()
		{
			@Override
			public String apply(String value)
			{
				return quotes+value+quotes;
			}
		});
		return CStringHelper.join(list,",");
	}
	
	
	private void getWhereList(CSqlBuilder builder)
	{
		if (!this.ids.isEmpty())
			builder.where(this.ids.toSql("tag_id"));
		else if (!this.identifiers.isEmpty())
			builder.where(CIdentifierListHelper.toSql("tag_identifier",this.identifiers));
	}
	
	private void getSortList(CSqlBuilder builder)
	{
		for (String sort : sorts)
		{
			builder.orderby(sort);
		}
	}
	
	private void getPaging(CSqlBuilder builder)
	{
		if (this.paged)
		{
			builder.offset(this.start);
			builder.limit(this.pagesize);
		}
	}
	
	/*
	public String getCountSql()
	{
		CSqlBuilder builder=new CSqlBuilder();
		builder.select("count(*)/"+this.attributes.size()+" as total");
		builder.from("vw_attributes");
		getWhereList(builder);
		return builder.toString();
	}
	
	public String getSql()
	{
		CSqlBuilder builder=new CSqlBuilder();
		getSelectList(builder);
		builder.from("vw_attributes");
		getWhereList(builder);
		getSortList(builder);
		
		CSqlBuilder builder2=new CSqlBuilder();
		builder2.select("attribute_id,tagtype_id,attributetype_id,tag_id,tag_identifier,tag_name,name,type,value");
		builder2.from("("+builder.toString()+") as temptable");
		getPaging(builder2);
		return builder2.toString();
	}
	
	private void getSelectList(CSqlBuilder builder)
	{
		builder.select("*");
		for (String sort : sorts)
		{
			builder.select("case when name='"+sort+"' then value else null end AS sort"+sort);
		}	
	}

	*/
}