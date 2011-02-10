package org.vardb.tags;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.vardb.util.CAttributeList;
import org.vardb.util.CCounts;
import org.vardb.util.CDataFrame;
import org.vardb.util.CIdList;
import org.vardb.util.IAbstractDao;

@Transactional(readOnly=false)
public interface ITagDao extends IAbstractDao
{
	CTagType getTagType(String id);
	CTag getTag(String identifier);
	
	CTag getTag(int id);
	void deleteTag(int id);
	
	List<CTag> getTagsByType(String tagtype_id);
	List<CTag> getTagsByType(String tagtype_id, Collection<String> identifiers);

	public List<CAttributeType> getAttributeTypes(Collection<String> identifiers);
	
	void tagTags(int tag_id, CIdList ids);
	void untagTags(int tag_id, CIdList ids);
	
	void tagTags(String tagidentifier, Collection<String> identifiers);
	void untagTags(String tagidentifier, Collection<String> identifiers);
	
	void updateCounts();
	void updateItemCounts();
	void updateAttributeCounts();
	
	void setAttributetypes(String tagtype_id, CAttributeList attributetypes);
	//void setAttributes(String tagtype_id, CAttributeList attributes);
	
	CTagDataFrame getTags(CTagQueryParams params);
	CDataFrame joinTags(CTagJoinQueryParams params);
	
	CCounts getCounts(CTagQueryParams params);
}