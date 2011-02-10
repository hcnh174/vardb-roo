package org.vardb.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.vardb.util.CBeanHelper;
import org.vardb.util.CDataFrame;
import org.vardb.util.CDataType;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;

public class CTagServiceImpl implements ITagService
{
	private static final String NAME="name";
	private static final String TYPE="type";
	private static final String DESCRIPTION="description";
	private static final String VALUE="value";
	private static final String INLINE="inline";
	private static final String TAGTYPE_EXTENSION=".txt";
	
	protected ITagDao dao;
	private CBeanHelper beanhelper=new CBeanHelper();
	
	public ITagDao getDao(){return this.dao;}
	@Required public void setDao(ITagDao dao){this.dao=dao;}
	
	public CTagType getTagType(String tagtype_id)
	{
		CTagType tagtype=getDao().getTagType(tagtype_id);
		if (tagtype==null)
			throw new CException("tagtype not found: "+tagtype_id);
		return tagtype;
	}
	
	public CTagType findOrCreateTagType(String tagtype_id)
	{
		CTagType tagtype=getDao().getTagType(tagtype_id);
		if (tagtype==null)
		{
			tagtype=new CTagType(tagtype_id);
			dao.saveOrUpdate(tagtype);
		}
		return tagtype;
	}
	
	/*
	public CTagType createTagType(String tagtypename)
	{
		CTagType tagtype=new CTagType(tagtypename);
		dao.saveOrUpdate(tagtype);
		return tagtype;
	}
	*/
	
	public CTagType createTagTypeFromTags(String tagtypename, CDataFrame dataframe)
	{
		CTagType tagtype=createTagType(tagtypename,dataframe.getColTypes());
		dao.saveOrUpdate(tagtype);
		List<CTag> tags=new ArrayList<CTag>();
		for (Object rowname : dataframe.getRowNames())
		{
			CTag tag=tagtype.createTag(rowname.toString());
			tags.add(tag);
			for (String colname : dataframe.getColNames())
			{
				String value=dataframe.getStringValue(colname, rowname);
				tagtype.setAttribute(tag,colname,value);
			}
		}
		dao.saveOrUpdateAll(tags);
		return tagtype;
	}
	
	public CTagType createTagTypeFromFile(String filename)
	{
		String tagtypename=CFileHelper.getIdentifierFromFilename(filename,TAGTYPE_EXTENSION);
		CTagType tagtype=findOrCreateTagType(tagtypename);
		CDataFrame dataframe=CDataFrame.parseTabFile(filename);
		for (Object property : dataframe.getRowNames())
		{
			String name=dataframe.getValue(NAME,property).toString();
			String value=dataframe.getValue(VALUE,property).toString();
			System.out.println("setting tagtype property: "+name+"="+value);
			beanhelper.setPropertyFromString(tagtype,name,value);
		}
		dao.saveOrUpdate(tagtype);
		return tagtype;
	}	
	
	public CTagType createTagType(String tagtypename, Map<String,CDataType> atttypes)
	{
		CTagType tagtype=findOrCreateTagType(tagtypename);
		for (String attname : atttypes.keySet())
		{
			CDataType datatype=atttypes.get(attname);
			CAttributeType atttype=tagtype.findOrCreateAttributeType(attname,datatype);
			//CAttributeType atttype=new CAttributeType(tagtypename,attname,datatype);
			tagtype.addAttributeType(atttype);			
		}
		dao.saveOrUpdate(tagtype);
		return tagtype;
	}
	
	public CTagType createTagTypeFromAttributeFile(String filename)
	{
		String tagtypename=CFileHelper.getIdentifierFromFilename(filename,TAGTYPE_EXTENSION);
		CDataFrame dataframe=CDataFrame.parseTabFile(filename);
		return createTagTypeFromAttributes(tagtypename, dataframe);	
	}	
	
	public CTagType createTagTypeFromAttributes(String tagtypename, CDataFrame dataframe)
	{
		System.out.println("creating tag type: "+tagtypename);
		//CFileHelper.writeFile("c:/temp/dataframe-"+tagtypename+".txt",dataframe.toString());
		//CTagType tagtype=new CTagType(tagtypename);
		CTagType tagtype=findOrCreateTagType(tagtypename);
		//dao.saveOrUpdate(tagtype);
		for (Object attname : dataframe.getRowNames())
		{
			String name=dataframe.getValue(NAME,attname).toString();
			CDataType datatype=CDataType.valueOf(dataframe.getValue(TYPE, attname,CDataType.STRING).toString());
			String description=dataframe.getValue(DESCRIPTION,attname,"").toString();
			Boolean inline=Boolean.parseBoolean(dataframe.getValue(INLINE,attname,"FALSE").toString());
			CAttributeType atttype=new CAttributeType(tagtypename,name,datatype);
			atttype.setDescription(description);
			atttype.setInline(inline);
			tagtype.addAttributeType(atttype);
			//System.out.println("creating attribute: "+CStringHelper.toString(atttype));
		}
		dao.saveOrUpdate(tagtype);
		return tagtype;
	}
	
	public Map<String,CTag> findOrCreateTags(String tagtype_id, Collection<String> identifiers)
	{
		CTagType tagtype=getDao().getTagType(tagtype_id);
		Map<String,CTag> map=new HashMap<String,CTag>();
		List<CTag> tags=getDao().getTagsByType(tagtype_id,identifiers);
		for (CTag tag : tags)
		{
			map.put(tag.getIdentifier(),tag);
		}
		List<CTag> added=new ArrayList<CTag>();
		for (String identifier : identifiers)
		{
			CStringHelper.checkIdentifier(identifier);
			if (map.containsKey(identifier))
				continue;
			CTag tag=tagtype.createTag(identifier);
			map.put(identifier,tag);
			added.add(tag);
		}
		getDao().saveOrUpdateAll(added);
		return map;
	}
	
	public CDataFrame getTags(CTagQueryParams params)
	{
		return getDao().getTags(params);
	}
}