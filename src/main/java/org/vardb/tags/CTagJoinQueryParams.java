package org.vardb.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vardb.util.CException;
import org.vardb.util.CIdList;
import org.vardb.util.CIdentifierListHelper;
import org.vardb.util.CPaging;
import org.vardb.util.CSqlBuilder;
import org.vardb.util.CStringHelper;

public class CTagJoinQueryParams extends CPaging
{
	public static void main(String[] args)
	{
		CTagJoinQueryParams params=new CTagJoinQueryParams();
		TagSet sequencetype=params.addTagSet("sequence").addAttributes("accession,ntlength,aalength,gc3");
		TagSet familytype=params.addTagSet("family").addAttributes("familysize,switchingrate");
		params.addSort("sequence:accession");
		params.addSort("family:familysize");
		params.addJoin(sequencetype,familytype);	
	}
	
	protected Map<String,TagSet> tagsets=new LinkedHashMap<String,TagSet>();
	protected List<Join> joins=new ArrayList<Join>();
	protected List<Sort> sorts=new ArrayList<Sort>();
	protected CIdList ids=new CIdList();
	protected List<String> identifiers=new ArrayList<String>();
	
	public TagSet addTagSet(String name)
	{
		TagSet tagtype=new TagSet(name);
		tagsets.put(name,tagtype);
		return tagtype;
	}
	
	public CTagJoinQueryParams addJoin(TagSet tagset1, TagSet tagset2)
	{
		Join join=new Join(tagset1,tagset2);
		this.joins.add(join);
		return this;
	}
	
	public CTagJoinQueryParams addId(Integer id)
	{
		this.ids.addId(id);
		return this;
	}
	
	public CTagJoinQueryParams addIds(Collection<Integer> ids)
	{
		this.ids.addIds(ids);
		return this;
	}
	
	public CTagJoinQueryParams addIdentifier(String identifier)
	{
		this.identifiers.add(identifier);
		return this;
	}
	
	public CTagJoinQueryParams addIdentifiers(Collection<String> identifier)
	{
		this.identifiers.addAll(identifier);
		return this;
	}
	
	public CTagJoinQueryParams addSort(String sort)
	{
		String tagtype=CTagHelper.getTagType(sort);
		String name=CTagHelper.getName(sort);
		if (!tagsets.containsKey(tagtype))
			throw new CException("tagtype is not added as a tagset: "+tagtype+" for sort attribute "+sort);
		TagSet tagset=tagsets.get(tagtype);
		tagset.addSort(name);
		this.sorts.add(new Sort(tagset,name));
		return this;
	}
	
	public CTagJoinQueryParams addSorts(Collection<String> sorts)
	{
		for (String sort : sorts)
		{
			addSort(sort);
		}
		return this;
	}
	
	public void setPaging(int start, int pagesize)
	{
		this.paged=true;
		this.start=start;
		this.pagesize=pagesize;
	}
	
	public List<String> getTagtypes()
	{
		List<String> tagtypes=new ArrayList<String>();
		for (TagSet tagset : tagsets.values())
		{
			tagtypes.add(tagset.getTagtype());
		}
		return tagtypes;
	}
	
	public Collection<TagSet> getTagsets()
	{
		return tagsets.values();
	}
	
	public String getTagSql()
	{
		CSqlBuilder builder=new CSqlBuilder();
		for (TagSet tagset : tagsets.values())
		{
			builder.with(tagset.getTagtype()+" as ("+tagset.toSql()+")");
		}
		for (TagSet tagset : tagsets.values())
		{
			builder.select(tagset.getTagtype()+".tag_id as "+tagset.getTagtype());
		}		
		for (TagSet tagset : tagsets.values())
		{
			builder.from(tagset.getTagtype());
		}
		builder.from("tags_tags");
		for (Join join : joins)
		{
			builder.where(join.toSql());
		}
		addConditions(builder);
		for (Sort sort : sorts)
		{
			builder.orderby(sort.toSql());
		}
		if (paged)
		{
			builder.offset(this.start);
			builder.limit(this.pagesize);
		}
		return builder.toString();
	}
	
	private void addConditions(CSqlBuilder builder)
	{
		if (!this.ids.isEmpty())
			builder.where(this.ids.toSql("tag_id"));
		else if (!this.identifiers.isEmpty())
			builder.where(CIdentifierListHelper.toSql("tag_identifier",this.identifiers));
	}
	

	public class TagSet
	{
		protected String tagtype;
		protected List<String> attributes=new ArrayList<String>();
		protected List<String> sorts=new ArrayList<String>();
		
		public String getTagtype(){return this.tagtype;}
		public void setTagtype(final String tagtype){this.tagtype=tagtype;}

		public List<String> getAttributes(){return this.attributes;}
		public void setAttributes(final List<String> attributes){this.attributes=attributes;}
		
		public TagSet(String tagtype)
		{
			this.tagtype=tagtype;
		}
		
		public TagSet addAttributes(Collection<String> attributes)
		{
			this.attributes.addAll(attributes);
			return this;
		}
		
		public TagSet addAttributes(String attributes)
		{
			return addAttributes(CStringHelper.splitAsList(attributes));
		}
		
		public TagSet addSort(String sort)
		{
			this.sorts.add(sort);
			return this;
		}
		
		public TagSet addSorts(Collection<String> sorts)
		{
			this.sorts.addAll(sorts);
			return this;
		}
		
		public String toSql()
		{
			CSqlBuilder builder=new CSqlBuilder();
			builder.select("tag_id");
			for (String sort : sorts)
			{
				builder.select("max("+getSortname(sort)+") as "+getSortname(sort));
			}		
			builder.from("("+getInnerTable()+") temp"+tagtype);
			builder.groupby("tag_id");
			return builder.toString();
		}
		
		private String getInnerTable()
		{
			CSqlBuilder builder=new CSqlBuilder();
			builder.select("tag_id");
			for (String sort : sorts)
			{
				builder.select(getSortCondition(sort));
			}
			builder.from("tagtypes").from("tags").from("attributetypes").from("attributes");
			builder.where("tagtypes.id=tags.tagtype_id\n");
			builder.where("tags.id=attributes.tag_id\n");
			builder.where("attributetypes.id=attributes.attributetype_id\n");
			builder.where("tagtypes.id='"+tagtype+"'\n");
			builder.where("attributetypes.name in ('"+CStringHelper.join(this.sorts,"','")+"')\n");
			return builder.toString();
		}
		
		private String getSortCondition(String sort)
		{
			StringBuilder buffer=new StringBuilder();
			buffer.append("case when");
			buffer.append(" attributetypes.name='").append(sort).append("'");
			buffer.append(" and attributes.value is not null");
			buffer.append(" and attributes.value<>'' ");
			buffer.append("then value ");
			buffer.append("else null end AS "+getSortname(sort));
			return buffer.toString();
		}
		
		private String getSortname(String attribute)
		{
			return "sort_"+tagtype+"_"+attribute;
		}
	}
	
	public class Join
	{
		protected TagSet tagset1;
		protected TagSet tagset2;
		
		public Join(TagSet tagset1, TagSet tagset2)
		{
			this.tagset1=tagset1;
			this.tagset2=tagset2;
		}
		
		public String toSql()
		{
			StringBuilder buffer=new StringBuilder();
			buffer.append("(");
				buffer.append("(");
					buffer.append(tagset1.getTagtype()+".tag_id=tags_tags.tag_id1");
					buffer.append(" and ");
					buffer.append(tagset2.getTagtype()+".tag_id=tags_tags.tag_id2");
				buffer.append(")");
				buffer.append(" or ");
				buffer.append("(");
					buffer.append(tagset1.getTagtype()+".tag_id=tags_tags.tag_id2");
					buffer.append(" and ");
					buffer.append(tagset2.getTagtype()+".tag_id=tags_tags.tag_id1");
				buffer.append(")");
			buffer.append(")");
			return buffer.toString();
		}
	}
	
	public class Sort
	{
		protected TagSet tagset;
		protected String attribute;
		protected String sortname;
		
		public Sort(TagSet tagset, String attribute)
		{
			this.tagset=tagset;
			this.attribute=attribute;
			this.sortname=tagset.getSortname(attribute);
		}
		
		public String toSql()
		{
			StringBuilder buffer=new StringBuilder();
			buffer.append(tagset.getTagtype()+"."+sortname);
			return buffer.toString();
		}
	}
}


/*

SELECT
	tag_id,
	max(sort_sequence_aalength) as sort_sequence_aalength,
	max(sort_sequence_ntlength) as sort_sequence_ntlength
FROM
(
	SELECT 
		tags.id AS tag_id,
		case when attributetypes.name='aalength' and attributes.value is not null and attributes.value<>'' then attributes.value else null end AS sort_sequence_aalength,
		case when attributetypes.name='ntlength' and attributes.value is not null and attributes.value<>'' then attributes.value else null end AS sort_sequence_ntlength
	FROM tagtypes, tags, attributetypes, attributes
	WHERE tagtypes.id=tags.tagtype_id
	AND tags.id=attributes.tag_id
	AND attributetypes.id=attributes.attributetype_id
	AND tagtypes.id='sequence'
	AND attributetypes.name in ('aalength','ntlength')
) temp1
group by tag_id
*/

/*
with
families as (select family.*
from crosstab(
	'select tag_id, name, value
	from vw_attributes
	where tagtype_id=''family''
	and name in (''abbr'',''description'',''familysize'')
	order by 1,2'
	) as family(tag_id INTEGER, abbr text, description text, familysize text)
),
sequences as (select sequence.*
from crosstab(
	'select tag_id, name, value::integer
	from vw_attributes
	where tagtype_id=''sequence''
	and name in (''ntlength'',''cdslength'',''aalength'')
	order by 1,2'
	) as sequence(tag_id INTEGER, ntlength INTEGER, cdslength INTEGER, aalength INTEGER)
)
select sequences.tag_id, sequences.ntlength, sequences.cdslength, sequences.aalength, families.abbr, families.description, families. familysize
from families, sequences, tags_tags
where (families.tag_id=tags_tags.tag_id1 and sequences.tag_id=tags_tags.tag_id2)
or (families.tag_id=tags_tags.tag_id2 and sequences.tag_id=tags_tags.tag_id1)
order by sequences.aalength desc
limit 100
*/