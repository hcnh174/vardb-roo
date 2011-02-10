package org.vardb.tags;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.vardb.util.CAbstractDaoImpl;
import org.vardb.util.CAttributeList;
import org.vardb.util.CCountData;
import org.vardb.util.CCounts;
import org.vardb.util.CDataFrame;
import org.vardb.util.CIdList;
import org.vardb.util.CIdentifierListHelper;
import org.vardb.util.CStringHelper;

import com.google.common.base.Preconditions;

public class CTagDaoImpl extends CAbstractDaoImpl implements ITagDao
{	
	//@Resource(name="freemarkerService") private IFreemarkerService freemarkerService;
	
	public CTagType getTagType(String id)
	{
		return (CTagType)getSession().get(CTagType.class,id);
	}
	
	public CTag getTag(int id)
	{
		return (CTag)getSession().get(CTag.class,id);
	}
	
	public CTag getTag(String tagidentifier)
	{
		StringBuilder sql=new StringBuilder();
		sql.append("select tag from CTag tag where tag.tagidentifier=:tagidentifier");
		return (CTag)super.findUnique(sql,"tagidentifier",tagidentifier);
	}

	public void deleteTag(int id)
	{
		CTag tag=getTag(id);
		delete(tag);
	}
	
	@SuppressWarnings("unchecked")
	public List<CTag> getTagsByType(String tagtype_id)
	{
		StringBuilder sql=new StringBuilder();
		sql.append("select tag from CTag tag where tag.tagtype_id=:tagtype_id");
		return super.findByNamedParam(sql,"tagtype_id",tagtype_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CTag> getTagsByType(String tagtype_id, Collection<String> identifiers)
	{
		StringBuilder sql=new StringBuilder();
		sql.append("select tag from CTag tag\n");
		sql.append("where tag.tagtype_id=:tagtype_id\n");
		sql.append("and "+CIdentifierListHelper.toSql("tag.identifier",identifiers)+"\n");
		return super.findByNamedParam(sql,"tagtype_id",tagtype_id);
	}
	
	/////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public List<CAttributeType> getAttributeTypes(Collection<String> identifiers)
	{
		StringBuilder sql=new StringBuilder();
		sql.append("select atttype from CAttributeType atttype\n");
		sql.append("where "+CIdentifierListHelper.toSql("id",identifiers)+"\n");
		return super.find(sql);
	}
	
	/////////////////////////////////
	
	public void tagTags(int tag_id, CIdList ids)
	{
		String sql="SELECT tag_tags("+tag_id+",tags.*) FROM tags "+
		"WHERE "+ids.toSql("id")+";\n";
		System.out.println("sql="+sql);
		getJdbcTemplate().execute(sql);
	}
	
	public void untagTags(int tag_id, CIdList ids)
	{
		String sql="SELECT untag_tags("+tag_id+",tags.*) FROM tags "+
		"WHERE "+ids.toSql("id")+";\n";
		System.out.println("sql="+sql);
		getJdbcTemplate().execute(sql);
	}
	
	////////////////////////////////////////////////////////////////
	
	public void tagTags(String tagidentifier, Collection<String> tagidentifiers)
	{
		String sql="SELECT tag_tags('"+CStringHelper.escapeSql(tagidentifier)+"',tags.*) FROM tags "+
		"WHERE "+CIdentifierListHelper.toSql("tagidentifier",tagidentifiers)+";\n";
		System.out.println("sql="+sql);
		getJdbcTemplate().execute(sql);
	}
	
	public void untagTags(String tagidentifier, Collection<String> tagidentifiers)
	{
		String sql="SELECT untag_tags('"+CStringHelper.escapeSql(tagidentifier)+"',tags.*) FROM tags "+
		"WHERE "+CIdentifierListHelper.toSql("tagidentifier",tagidentifiers)+";\n";
		System.out.println("sql="+sql);
		getJdbcTemplate().execute(sql);
	}
	
	/////////////////////////////////
	
	public void updateCounts()
	{
		updateItemCounts();
		updateAttributeCounts();
	}
	
	public void updateItemCounts()
	{
		getJdbcTemplate().execute("SELECT update_numitems();");
	}
	
	public void updateAttributeCounts()
	{
		getJdbcTemplate().execute("SELECT update_numattributes();");
	}
	
	///////////////////////////////////////////////////////////////////////
	
	public void setAttributetypes(String tagtype_id, CAttributeList attributetypes)
	{
		if (attributetypes.isEmpty())
			return;
		String alias="attlist";
		String sql="select set_attributetypes('"+tagtype_id+"',"+alias+".*) from\n"+attributetypes.getSql(alias);
		//System.out.println("sql="+sql);
		getJdbcTemplate().execute(sql);
	}
	
	/*
	public void setAttributes(String tagtype_id, CAttributeList attributes)
	{
		if (attributes.isEmpty())
			return;
		String template=CFileHelper.getResource("/org/vardb/util/tags/dao/setattributes.ftl",this.getClass());
		String attlist=attributes.getSql("attlist");
		String sql=this.freemarkerService.formatStringTemplate(template,"tagtype_id",tagtype_id,"attlist",attlist);
		//System.out.println("sql="+sql);
		CFileHelper.writeFile("c:/temp/"+tagtype_id+CFileHelper.getTimestamp()+".sql",sql);
		getJdbcTemplate().execute(sql);
	}
	*/
	
	//////////////////////////////////////////////////////////////////	
	
	@SuppressWarnings("unchecked")
	public CTagDataFrame getTags(CTagQueryParams params)
	{
		CTagType tagtype=getTagType(params.getTagtype());
		Preconditions.checkNotNull(tagtype,"can't find tagtype: %s", params.getTagtype());
		CTagDataFrame dataframe=new CTagDataFrame(tagtype,params);
		Query query=getSession().createSQLQuery(params.getSql()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String,Object>> list=query.list();
		int rownum=0;
		for (Map<String,Object> row : list)
		{	
			for (String colname : row.keySet())
			{
				dataframe.setValue(colname,rownum,row.get(colname));
			}			
			System.out.println("another row: "+row.toString());
			rownum++;
		}
		if (params.getPaged())
			getTotal(params.getCountSql(),dataframe,params);
		else params.setTotal(dataframe.getNumRows());		
		return dataframe;
	}
	
	private void getTotal(String sql, CTagDataFrame dataframe, CTagQueryParams params)
	{
		if (!params.getPaged())
			return;
		Query query=getSession().createSQLQuery(sql);
		BigInteger total=(BigInteger)(query.uniqueResult());
		dataframe.setTotalCount(Integer.valueOf(total.toString()));
		System.out.println("total="+dataframe.getTotalCount());
	}
	
	@SuppressWarnings("unchecked")
	public CDataFrame joinTags(CTagJoinQueryParams params)
	{
		CDataFrame dataframe=new CDataFrame(true); // autoAddColumns
		System.out.println(params.getTagSql());
		Query query=getSession().createSQLQuery(params.getTagSql()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);//aliasToBean(CTagView.class));
		List<Map<String,Object>> list=query.list();
		int rownum=0;
		for (Map<String,Object> row : list)
		{	
			for (String tagtype : params.getTagtypes())
			{
				dataframe.setValue(tagtype,rownum,row.get(tagtype));
			}			
			//System.out.println("another row: "+row.toString());
			rownum++;
		}
		for (CTagJoinQueryParams.TagSet tagset : params.getTagsets())
		{
			CIdList ids=dataframe.getUniqueIds(tagset.getTagtype());
			System.out.println("unique ids: tagset="+tagset.getTagtype()+", ids="+ids);
			CTagQueryParams tagparams=new CTagQueryParams(tagset.getTagtype());
			tagparams.addAttributes(tagset.getAttributes());
			tagparams.addIds(ids);
			tagparams.setPaged(false);
			CDataFrame tags=getTags(tagparams);
			//System.out.println("tags json="+CWebHelper.json(tags));
			dataframe.appendForeignColumns(tagset.getTagtype(),tags);
		}
		return dataframe;
	}
	
	
	@SuppressWarnings("unchecked")
	public CCounts getCounts(CTagQueryParams params)
	{
		Query query=getSession().createSQLQuery(params.getCountsSql()).setResultTransformer(Transformers.aliasToBean(CCountData.class));
		List<CCountData> items=query.list();
		return new CCounts(items); 
	}
}