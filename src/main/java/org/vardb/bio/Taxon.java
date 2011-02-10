package org.vardb.bio;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.vardb.util.CStringHelper;

public class Taxon
{
	public final static int BACTERIA=2;
	public final static int EUKARYOTES=2759;
	public final static int VIRUSES=10239;
	
	protected Integer id;
	protected String identifier;
	protected String name="";
	protected String description="";
	protected Integer parent_id;
	protected TaxonomicLevel level=TaxonomicLevel.UNKNOWN;
	protected Integer taxid;
	protected Boolean initialized=false;
	protected Integer left=0;
	protected Integer right=0;
	
	protected Taxon parent;
	protected Set<Taxon> taxa=new LinkedHashSet<Taxon>();
	protected List<Taxon> lineage=new ArrayList<Taxon>();
	
	public Integer getId(){return this.id;}
	public void setId(final Integer id){this.id=id;}

	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(final String identifier){this.identifier=identifier;}

	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}

	public String getDescription(){return this.description;}
	public void setDescription(final String description){this.description=description;}

	public Integer getParent_id(){return this.parent_id;}
	public void setParent_id(final Integer parent_id){this.parent_id=parent_id;}

	public TaxonomicLevel getLevel(){return this.level;}
	public void setLevel(final TaxonomicLevel level){this.level=level;}

	public Integer getTaxid(){return this.taxid;}
	public void setTaxid(final Integer taxid){this.taxid=taxid;}

	public Boolean getInitialized(){return this.initialized;}
	public void setInitialized(final Boolean initialized){this.initialized=initialized;}

	public Integer getLeft(){return this.left;}
	public void setLeft(final Integer left){this.left=left;}

	public Integer getRight(){return this.right;}
	public void setRight(final Integer right){this.right=right;}

	public String getXml()
	{			
		StringBuilder buffer=new StringBuilder();
		getXml(buffer);
		return buffer.toString();
	}
	
	public Taxon(){}

	public Taxon(int taxid)
	{
		this.id=taxid;
		this.taxid=taxid;
		this.identifier=Integer.toString(taxid);
		this.name="taxid:"+taxid;
	}
	
	public Taxon(String identifier)
	{
		this(Integer.parseInt(identifier));
	}

	public boolean getHaschildren()
	{
		return (this.right-this.left==1);			
	}
	
	public List<Taxon> getLineage(){return this.lineage;}
	public void setLineage(final List<Taxon> lineage){this.lineage=lineage;}
	
	public Taxon getParent(){return this.parent;}
	public void setParent(Taxon parent){this.parent=parent;}

	public Set<Taxon> getTaxa(){return this.taxa;}
	public void setTaxa(Set<Taxon> taxa){this.taxa=taxa;}
	
	public void add(Taxon taxon)
	{
		taxon.setParent(this);
		this.taxa.add(taxon);
	}
	
	public void getXml(StringBuilder buffer)
	{
		getXml(buffer,0);
	}
	
	public void getXml(StringBuilder buffer, int indent)
	{
		String padding=CStringHelper.repeatString("\t", indent);
		buffer.append(padding+"<taxon");
		buffer.append(" identifier=\""+getIdentifier()+"\"");
		if (this.parent!=null)
			buffer.append(" parent=\""+this.parent.getIdentifier()+"\"");
		buffer.append(">\n");
		buffer.append(padding+"\t<name>"+this.name+"</name>\n");
		buffer.append(padding+"\t<level>"+this.level.name()+"</level>\n");
		buffer.append(padding+"\t<taxid>"+this.taxid+"</taxid>\n");
		buffer.append(padding+"</taxon>\n");
	}
	
	private static Integer counter=0;
	
	public void index()
	{
		for (Taxon taxon : this.taxa)
		{
			index(taxon);
		}
	}

	private void index(Taxon taxon)
	{
		taxon.setLeft(Taxon.counter++);
		for (Taxon child : taxon.getTaxa())
		{
			index(child);
		}
		taxon.setRight(Taxon.counter++);
		taxon.setInitialized(true);
	}
	
	public enum TaxonomicLevel
	{
		SUPERKINGDOM,KINGDOM,SUBKINGDOM,
		SUPERPHYLUM,PHYLUM,SUBPHYLUM,
		SUPERCLASS,CLASS,SUBCLASS,INFRACLASS,
		SUPERORDER,ORDER,SUBORDER,INFRAORDER,
		SUPERFAMILY,FAMILY,SUBFAMILY,
		GENUS(true),SUBGENUS(true),
		SPECIES_GROUP(true),SPECIES_SUBGROUP(true),SPECIES(true),SUBSPECIES(true),
		SUBTYPE(true),STRAIN(true),NO_RANK(true),
		VARIETAS,TRIBE,SUBTRIBE,FORMA,PARVORDER,
		NONE,
		OTHER,
		UNRECOGNIZED,
		UNKNOWN;
		
		protected boolean italicized=false;
		
		TaxonomicLevel()
		{
			this(false);
		}
		
		TaxonomicLevel(final boolean italicized)
		{
			this.italicized=italicized;
		}
		
		public boolean getItalicized(){return this.italicized;}
		
		public static TaxonomicLevel lookup(final String name)
		{
			String rank=name.toUpperCase().replace(" ","_");
			try
			{
				return TaxonomicLevel.valueOf(rank);
			}
			catch(IllegalArgumentException e)
			{
				System.err.println("UNRECOGNIZED rank: "+name);
				return UNRECOGNIZED;
			}
		}
	}
}
