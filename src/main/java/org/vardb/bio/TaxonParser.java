package org.vardb.bio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.vardb.util.CDom4jHelper;

public final class TaxonParser
{
	private TaxonParser(){}
	
	@SuppressWarnings("unchecked")
	public static List<Taxon> parseTaxa(String xml)
	{
		//CFileHelper.writeFile("D:\\temp\\taxa.xml",xml);
		Document document=CDom4jHelper.parse(xml);
		Element root=document.getRootElement();
		Map<Integer,Taxon> map=new LinkedHashMap<Integer,Taxon>();
		for (Iterator iter=root.selectNodes("/TaxaSet/Taxon").iterator();iter.hasNext();)
		{
			Element element=(Element)iter.next();
			parseTaxon(element,map);
		}
		List<Taxon> taxa=new ArrayList<Taxon>();
		taxa.addAll(map.values());
		return taxa;
	}
	
	@SuppressWarnings("unchecked")
	private static void parseTaxon(Element taxonnode, Map<Integer,Taxon> taxa)
	{
		Taxon taxon=new Taxon(CDom4jHelper.getValue(taxonnode,"TaxId"));
		taxon.setName(CDom4jHelper.getValue(taxonnode,"ScientificName"));
		taxon.setLevel(Taxon.TaxonomicLevel.lookup(CDom4jHelper.getValue(taxonnode,"Rank")));
		taxon.setParent_id(Integer.parseInt(CDom4jHelper.getValue(taxonnode,"ParentTaxId")));
		taxa.put(taxon.getId(),taxon);
		
		Taxon parent=null;
		for (Iterator iter=taxonnode.selectNodes("LineageEx/Taxon").iterator();iter.hasNext();)
		{
			Element element=(Element)iter.next();
			int taxid=Integer.parseInt(CDom4jHelper.getValue(element,"TaxId"));
			if (!taxa.containsKey(taxid))
			{
				Taxon ancestor=new Taxon(taxid);
				ancestor.setName(CDom4jHelper.getValue(element,"ScientificName"));
				ancestor.setLevel(Taxon.TaxonomicLevel.lookup(CDom4jHelper.getValue(element,"Rank")));
				taxa.put(taxid,ancestor);
				if (parent!=null)
					ancestor.setParent_id(parent.getId());
			}
			parent=taxa.get(taxid);
		}
	}
}