package org.vardb.bio;

import java.util.List;

import org.vardb.util.CMathHelper;
import org.vardb.util.CStringHelper;

public class Ref
{
	public enum ReferenceType{JOURNAL,BOOK,CHAPTER};
	
	protected Integer id;
	protected String identifier;
	protected String name;
	protected ReferenceType type=ReferenceType.JOURNAL;
	protected Integer pmid=null;
	protected String authors="";
	protected String year="";
	protected String title="";
	protected String journal="";
	protected String volume="";
	protected String pages="";
	protected String publisher="";
	protected String city="";
	protected String abstrct="";
	
	public Integer getId(){return this.id;}
	public void setId(Integer id){this.id=id;}

	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(String identifier){this.identifier=identifier;}

	public String getName(){return this.name;}
	public void setName(String name){this.name=name;}
	
	public ReferenceType getType(){return this.type;}
	public void setType(ReferenceType type){this.type=type;}

	public Integer getPmid(){return this.pmid;}
	public void setPmid(Integer pmid){this.pmid=pmid;}

	public String getAuthors(){return this.authors;}
	public void setAuthors(String authors){this.authors=authors;}

	public String getYear(){return this.year;}
	public void setYear(String year){this.year=year;}

	public String getTitle(){return this.title;}
	public void setTitle(String title){this.title=title;}

	public String getJournal(){return this.journal;}
	public void setJournal(String journal){this.journal=journal;}

	public String getVolume(){return this.volume;}
	public void setVolume(String volume){this.volume=volume;}

	public String getPages(){return this.pages;}
	public void setPages(String pages){this.pages=pages;}

	public String getPublisher(){return this.publisher;}
	public void setPublisher(String publisher){this.publisher=publisher;}

	public String getCity(){return this.city;}
	public void setCity(String city){this.city=city;}
	
	public String getAbstract(){return this.abstrct;}
	public void setAbstract(String abstrct){this.abstrct=abstrct;}
	
	public Ref() {}
	
	public Ref(String identifier, boolean visible)
	{
		this();
		this.identifier=identifier;
		this.name=identifier;
		if (CMathHelper.isInteger(identifier))
			this.pmid=Integer.parseInt(identifier);
	}
	
	public Ref(int pmid, boolean visible)
	{
		this(String.valueOf(pmid),visible);
		this.pmid=pmid;
	}

	public String getAbbreviation()
	{
		return this.name;
	}
	
	public String createAbbreviation()
	{
		if (CStringHelper.isEmpty(this.authors))
			return this.identifier;
		List<String> authors=CStringHelper.splitAsList(this.authors,",");
		String firstauthor=authors.get(0);
		if (!CStringHelper.isEmpty(firstauthor) && firstauthor.indexOf(' ')!=-1)
			firstauthor=firstauthor.substring(0,firstauthor.indexOf(' '));
		return firstauthor+" et al., "+this.year;
	}

	public String getCitation()
	{
		return getCitation(this.pages);
	}
	
	public String getCitation(String pages)
	{
		if (CStringHelper.isEmpty(this.title))
			return "";
		StringBuilder buffer=new StringBuilder();
		if (this.type==ReferenceType.JOURNAL)
		{
			buffer.append(this.authors).append(". ").append(this.year).append(". ").append(this.title).append(" ");
			buffer.append(this.journal).append(" ").append(this.volume).append(":").append(this.pages).append(".");
		}
		else if (this.type==ReferenceType.BOOK)
		{
			buffer.append(this.authors).append(". ").append(this.year).append(". ").append(this.title).append(". ");
			buffer.append(this.publisher).append(", ").append(this.city).append(".");
			if (CStringHelper.hasContent(pages))
				 buffer.append(" (pp. "+pages+").");
		}
		else if (this.type==ReferenceType.CHAPTER)
		{
			buffer.append(this.authors).append(". ").append(this.year).append(". ").append(this.title).append(". ");
			buffer.append(this.publisher).append(", ").append(this.city).append(". ").append(this.pages).append(".");
		}
		return buffer.toString();
	}
	
	public void dump()
	{
		System.out.println("ID: "+this.pmid);
        System.out.println("Journal: "+this.journal);
    	System.out.println("Volume: "+this.volume);
    	System.out.println("Pages: "+this.pages);
    	System.out.println("Year: "+this.year);
    	System.out.println("Title: "+this.title);
    	System.out.println("Authors: "+this.authors);
        System.out.println("Abstract: "+this.abstrct);
        System.out.println("--------------------------\n");
	}

	public String getHtml()
	{
		StringBuilder buffer=new StringBuilder();
		buffer.append("(<a href=\"javascript:void(0)\" onclick=\"vardb.VarDB.refPopup('").append(this.identifier).append("')\">");
		buffer.append(this.name);
		buffer.append("</a>)");
		return buffer.toString();
	}
}