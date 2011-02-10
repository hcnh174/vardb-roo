package org.vardb.bio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.vardb.util.CDom4jHelper;
import org.vardb.util.CStringHelper;

public final class RefParser
{
	private RefParser(){}
	
	@SuppressWarnings("unchecked")
    public static List<Ref> parseRefs(String xml)
	{
		Document document=CDom4jHelper.parse(xml);
		Element root=document.getRootElement();
		List<Ref> refs=new ArrayList<Ref>();
		for (Iterator iter=root.selectNodes("/PubmedArticleSet/PubmedArticle").iterator();iter.hasNext();)
		{
			Element element=(Element)iter.next();
			Ref ref=parseRef(element);
			refs.add(ref);
		}
		return refs;
	}
	
	private static Ref parseRef(Element refnode)
	{	
		Ref ref=new Ref();
		ref.setPmid(Integer.valueOf(CDom4jHelper.getValue(refnode,"MedlineCitation/PMID")));
		Element article=(Element)refnode.selectSingleNode("MedlineCitation/Article");
		ref.setAuthors(getAuthors(article));
    	ref.setTitle(CDom4jHelper.getValue(article,"ArticleTitle"));
    	ref.setJournal(CDom4jHelper.getValue(article,"Journal/Title"));
    	ref.setVolume(CDom4jHelper.getValue(article,"Journal/JournalIssue/Volume"));
    	ref.setYear(CDom4jHelper.getValue(article,"Journal/JournalIssue/PubDate/Year"));
    	ref.setPages(CDom4jHelper.getValue(article,"Pagination/MedlinePgn"));
    	ref.setAbstract(CDom4jHelper.getValue(article,"Abstract/AbstractText"));
    	return ref;
	}
	
	private static String getAuthors(Element article)
	{
    	List<String> authors=new ArrayList<String>();
		for (Iterator<?> iter=article.selectNodes("AuthorList/Author").iterator();iter.hasNext();)
		{
			Element element=(Element)iter.next();
			String lastname=CDom4jHelper.getValue(element,"LastName");
			String initials=CDom4jHelper.getValue(element,"Initials");
			authors.add(lastname+" "+initials);
		}
		return CStringHelper.join(authors,", ").trim();
	}
}