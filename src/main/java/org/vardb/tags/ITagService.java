package org.vardb.tags;

import java.util.Collection;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.vardb.util.CDataFrame;
import org.vardb.util.CDataType;

@Transactional(readOnly=false)
public interface ITagService
{
	CTagType getTagType(String tagtype_id);
	
	CTagType findOrCreateTagType(String tagtypename);
	//CTagType createTagType(String tagtypename);
	CTagType createTagTypeFromFile(String filename);
	CTagType createTagTypeFromAttributeFile(String filename);
	CTagType createTagTypeFromAttributes(String tagtypename, CDataFrame dataframe);
	CTagType createTagTypeFromTags(String tagtypename, CDataFrame dataframe);
	CTagType createTagType(String tagtypename, Map<String,CDataType> atttypes);
	
	Map<String,CTag> findOrCreateTags(String tagtype_id, Collection<String> names);
	
	CDataFrame getTags(CTagQueryParams params);
	
	ITagDao getDao();
}
