package org.vardb.bio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.vardb.util.CBeanHelper;
import org.vardb.util.CStringHelper;

public class GenbankSequence
{
	protected Integer id;
	protected String genome="";
	protected String chromosome="";
	protected String family="";
	protected String taxon="";
	protected String country="";
	protected String ref="";
	protected String sequenceset="";
	protected String source="";
	protected String identifier;
	protected boolean visible=true;
	protected String user_id;
	protected boolean uploaded=false;
	protected String accession="";
	protected Integer gi=null;
	protected String defline="";
	protected String version="";
	protected String uniprot="";	
	protected String isolate="";
	protected String isolation_source="";
	protected Integer molwt=null;
	protected String subregion="";
	protected String collection_date="";
	protected String locus_tag="";
	protected Integer codon_start=null;
	protected String transl_table="";
	protected String mol_type="";
	protected String strain="";
	protected String segment="";
	protected String serotype="";
	protected String serogroup="";
	protected String serovar=""; // new
	protected String subtype=""; // new
	protected String host=""; // new
	protected String lab_host=""; // new
	protected String specific_host="";
	protected String plasmid="";
	protected String allele="";
	protected String codedby="";
	protected String product="";
	protected String oldid="";
	protected Boolean truncated=false;
	protected Date udate=null;
	protected String division="";
	protected String natype="";
	protected Boolean circular=null;
	protected String clone="";
	protected String comments="";
	protected String notes="";
	protected Date created;
	protected Date updated;
	protected String method=null;
	protected String model=null;
	protected Double score=null;
	protected Double evalue=null;
	protected String hmmloc=null;

	// nucleotide
	protected Boolean pseudogene=false;
	protected Integer start=null;
	protected Integer end=null;
	protected StrandType strand=null;
	protected Integer numexons=null;
	protected String locus="";
	protected String gene="";
	protected Integer geneid=null;
	protected String protein_id="";
	protected Integer protein_gi=null;
	protected String protein="";
	protected String splicing="";
	protected String seg="";
	protected String dust="";
	protected Float gc=null;
	protected Float gc3=null;
	protected Float gc3skew=null;
	
	protected Integer domainnum=0;
	protected Integer totaldomainnum=0;
	protected String domains="";
	protected String architecture="";
	
	// protein
	protected String ec=null;
	protected Boolean conceptual=false;
	
	protected Integer ntlength=0;
	protected Integer aalength=0;
	
	protected String spliced="";
	protected String sequence="";	
	protected String translation=null;
	protected String secondary=null;
	
	public Integer getId(){return this.id;}
	public void setId(final Integer id){this.id=id;}
	
	public String getGenome(){return this.genome;}
	public void setGenome(String genome){this.genome=genome;}

	public String getChromosome(){return this.chromosome;}
	public void setChromosome(String chromosome){this.chromosome=chromosome;}

	public String getFamily(){return this.family;}
	public void setFamily(String family){this.family=family;}

	public String getTaxon(){return this.taxon;}
	public void setTaxon(String taxon){this.taxon=taxon;}

	public String getCountry(){return this.country;}
	public void setCountry(String country){this.country=country;}

	public String getRef(){return this.ref;}
	public void setRef(String ref){this.ref=ref;}
	
	public String getSequenceset(){return this.sequenceset;}
	public void setSequenceset(final String sequenceset){this.sequenceset=sequenceset;}
	
	public String getSource(){return this.source;}
	public void setSource(final String source){this.source=source;}

	public GenbankSequence(){}
	
	public GenbankSequence(GenbankSequence template)
	{
		CBeanHelper beanhelper=new CBeanHelper();
		beanhelper.copyProperties(this,template);
	}
	
	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(final String identifier){this.identifier=identifier;}
	
	public boolean getVisible(){return this.visible;}
	public void setVisible(boolean visible){this.visible=visible;}

	public String getUser_id(){return this.user_id;}
	public void setUser_id(final String user_id){this.user_id=user_id;}
	
	public boolean getUploaded(){return this.uploaded;}
	public void setUploaded(final boolean uploaded){this.uploaded=uploaded;}

	public String getAccession(){return this.accession;}
	public void setAccession(String accession){this.accession=accession;}

	public Integer getGi(){return this.gi;}
	public void setGi(Integer gi){this.gi=gi;}

	public String getDefline(){return this.defline;}
	public void setDefline(String defline){this.defline=defline;}

	public String getVersion(){return this.version;}
	public void setVersion(String version){this.version=version;}
	
	public String getUniprot(){return this.uniprot;}
	public void setUniprot(String uniprot){this.uniprot=uniprot;}

	public String getIsolate(){return this.isolate;}
	public void setIsolate(String isolate){this.isolate=isolate;}

	public String getIsolation_source(){return this.isolation_source;}
	public void setIsolation_source(String isolation_source){this.isolation_source=isolation_source;}

	public Integer getMolwt(){return this.molwt;}
	public void setMolwt(Integer molwt){this.molwt=molwt;}

	public String getSubregion(){return this.subregion;}
	public void setSubregion(String subregion){this.subregion=subregion;}

	public String getCollection_date(){return this.collection_date;}
	public void setCollection_date(String collection_date){this.collection_date=collection_date;}
	
	public String getLocus_tag(){return this.locus_tag;}
	public void setLocus_tag(String locus_tag){this.locus_tag=locus_tag;}

	public Integer getCodon_start(){return this.codon_start;}
	public void setCodon_start(Integer codon_start){this.codon_start=codon_start;}

	public String getTransl_table(){return this.transl_table;}
	public void setTransl_table(String transl_table){this.transl_table=transl_table;}

	public String getMol_type(){return this.mol_type;}
	public void setMol_type(String mol_type){this.mol_type=mol_type;}

	public String getStrain(){return this.strain;}
	public void setStrain(String strain){this.strain=strain;}

	public String getSegment(){return this.segment;}
	public void setSegment(String segment){this.segment=segment;}

	public String getSerotype(){return this.serotype;}
	public void setSerotype(String serotype){this.serotype=serotype;}

	public String getSerogroup(){return this.serogroup;}
	public void setSerogroup(String serogroup){this.serogroup=serogroup;}

	public String getSerovar(){return this.serovar;}
	public void setSerovar(final String serovar){this.serovar=serovar;}

	public String getSubtype(){return this.subtype;}
	public void setSubtype(final String subtype){this.subtype=subtype;}

	public String getHost(){return this.host;}
	public void setHost(final String host){this.host=host;}

	public String getLab_host(){return this.lab_host;}
	public void setLab_host(final String lab_host){this.lab_host=lab_host;}
	
	public String getSpecific_host(){return this.specific_host;}
	public void setSpecific_host(String specific_host){this.specific_host=specific_host;}

	public String getPlasmid(){return this.plasmid;}
	public void setPlasmid(String plasmid){this.plasmid=plasmid;}

	public String getAllele(){return this.allele;}
	public void setAllele(String allele){this.allele=allele;}

	public String getCodedby(){return this.codedby;}
	public void setCodedby(String codedby){this.codedby=codedby;}

	public String getProduct(){return this.product;}
	public void setProduct(String product){this.product=product;}

	public String getOldid(){return this.oldid;}
	public void setOldid(String oldid){this.oldid=oldid;}

	public Boolean getTruncated(){return this.truncated;}
	public void setTruncated(Boolean truncated){this.truncated=truncated;}

	public Date getUdate(){return this.udate;}
	public void setUdate(Date udate){this.udate=udate;}

	public String getDivision(){return this.division;}
	public void setDivision(String division){this.division=division;}

	public String getNatype(){return this.natype;}
	public void setNatype(String natype){this.natype=natype;}

	public Boolean getCircular(){return this.circular;}
	public void setCircular(Boolean circular){this.circular=circular;}

	public String getClone(){return this.clone;}
	public void setClone(String clone){this.clone=clone;}

	public String getComments(){return this.comments;}
	public void setComments(String comments){this.comments=comments;}

	public String getNotes(){return this.notes;}
	public void setNotes(String notes){this.notes=notes;}

	@Column(insertable=false,updatable=false)
	public Date getCreated(){return this.created;}
	public void setCreated(final Date created){this.created=created;}

	@Column(insertable=false,updatable=false)
	public Date getUpdated(){return this.updated;}
	public void setUpdated(final Date updated){this.updated=updated;}

	public String getMethod(){return this.method;}
	public void setMethod(String method){this.method=method;}

	public String getModel(){return this.model;}
	public void setModel(String model){this.model=model;}

	public Double getScore(){return this.score;}
	public void setScore(Double score){this.score=score;}

	public Double getEvalue(){return this.evalue;}
	public void setEvalue(Double evalue){this.evalue=evalue;}

	public String getHmmloc(){return this.hmmloc;}
	public void setHmmloc(final String hmmloc){this.hmmloc=hmmloc;}
	
	public Boolean getPseudogene(){return this.pseudogene;}
	public void setPseudogene(Boolean pseudogene){this.pseudogene=pseudogene;}

	public Integer getStart(){return this.start;}
	public void setStart(Integer start){this.start=start;}

	@Column(name="finish")
	public Integer getEnd(){return this.end;}
	public void setEnd(Integer end){this.end=end;}

	@Enumerated(EnumType.STRING)
	public StrandType getStrand(){return this.strand;}
	public void setStrand(StrandType strand){this.strand=strand;}

	public Integer getNumexons(){return this.numexons;}
	public void setNumexons(Integer numexons){this.numexons=numexons;}

	public String getLocus(){return this.locus;}
	public void setLocus(final String locus){this.locus=locus;}
	
	public String getGene(){return this.gene;}
	public void setGene(String gene){this.gene=gene;}

	public Integer getGeneid(){return this.geneid;}
	public void setGeneid(Integer geneid){this.geneid=geneid;}

	public String getProtein_id(){return this.protein_id;}
	public void setProtein_id(String protein_id){this.protein_id=protein_id;}

	public Integer getProtein_gi(){return this.protein_gi;}
	public void setProtein_gi(Integer protein_gi){this.protein_gi=protein_gi;}

	public String getProtein(){return this.protein;}
	public void setProtein(String protein){this.protein=protein;}

	public String getSplicing(){return this.splicing;}
	public void setSplicing(String splicing){this.splicing=splicing;}
	
	public String getSeg(){return this.seg;}
	public void setSeg(final String seg){this.seg=seg;}
	
	public String getDust(){return this.dust;}
	public void setDust(final String dust){this.dust=dust;}
	
	public Float getGc(){return this.gc;}
	public void setGc(final Float gc){this.gc=gc;}
	
	public Float getGc3(){return this.gc3;}
	public void setGc3(final Float gc3){this.gc3=gc3;}

	public Float getGc3skew(){return this.gc3skew;}
	public void setGc3skew(final Float gc3skew){this.gc3skew=gc3skew;}
	
	public Integer getDomainnum(){return this.domainnum;}
	public void setDomainnum(final Integer domainnum){this.domainnum=domainnum;}

	public Integer getTotaldomainnum(){return this.totaldomainnum;}
	public void setTotaldomainnum(final Integer totaldomainnum){this.totaldomainnum=totaldomainnum;}

	public String getDomains(){return this.domains;}
	public void setDomains(final String domains){this.domains=domains;}

	public String getArchitecture(){return this.architecture;}
	public void setArchitecture(final String architecture){this.architecture=architecture;}
	
	public String getEc(){return this.ec;}
	public void setEc(String ec){this.ec=ec;}

	public Boolean getConceptual(){return this.conceptual;}
	public void setConceptual(Boolean conceptual){this.conceptual=conceptual;}
	
	public Integer getNtlength(){return this.ntlength;}
	public void setNtlength(final Integer ntlength){this.ntlength=ntlength;}

	public Integer getAalength(){return this.aalength;}
	public void setAalength(final Integer aalength){this.aalength=aalength;}

	public String getSequence(){return this.sequence;}
	public void setSequence(final String sequence){this.sequence=sequence;}

	public String getSpliced(){return this.spliced;}
	public void setSpliced(String spliced){this.spliced=spliced;}
	
	public String getTranslation(){return this.translation;}
	public void setTranslation(final String translation){this.translation=translation;}
	
	public String getSecondary(){return this.secondary;}
	public void setSecondary(String secondary){this.secondary=secondary;}
	
	@PreUpdate
	protected void onUpdate()
	{
		this.updated = new Date();
		System.out.println("onUpdate called: "+this.updated.toString());
	}
	
	@Transient
	public String getName()
	{
		return this.accession;
	}
	
	@Transient
	public String getDescription()
	{
		return this.defline;
	}
	
	@Transient
	public String getAbbreviation()
	{
		return this.accession;
	}
	
	@Transient
	public String getLocation()
	{
		if (this.start==null && this.end==null)
			return "";
		return this.start+".."+this.end;
	}
	
	@Transient
	public CompoundLocation getPfamdomains()
	{
		return new CompoundLocation(domains);
	}

	@Transient
	public String getCodingSequence()
	{
		return SequenceHelper.getCodingSequence(this.accession, this.spliced, this.sequence);
	}
	
	public String getSequence(SequenceType type)
	{
		return (type==SequenceType.NT) ? this.sequence : this.translation;
	}
	
	public void addNote(String note)
	{
		if (!CStringHelper.hasContent(note))
			return;
		if (CStringHelper.hasContent(this.notes))
			this.notes+="|";
		this.notes+=note.trim();
	}

	/*
	public static CTable getTable()
	{
		CTable table=new CTable();
		for (CSequenceProperties.Property property : PROPERTIES)
		{
			table.getHeader().add(property.name());
		}
		return table;
	}
	
	public void append(CTable table)
	{
		CTable.Row row=table.addRow();
		for (CSequenceProperties.Property property : PROPERTIES)
		{
			String value=getValue(property);
			row.add(value);
		}
	}
	
	
	public static String getHeader()
	{
		return getHeader(PROPERTIES);
	}
	
	public static String getHeader(List<CSequenceProperties.Property> properties)
	{
		return CStringHelper.join(properties,"\t")+"\n";
	}
	
	public String getData()
	{
		return getData(PROPERTIES);
	}
	
	public void setValue(CSequenceProperties.Property property, Object value)
	{
		CBeanHelper beanhelper=new CBeanHelper();
		beanhelper.setPropertyFromString(this,property.name(),value.toString());
	}
	
	public String getValue(CSequenceProperties.Property property)
	{
		CBeanHelper beanhelper=new CBeanHelper();
		String value=null;
		if (property==CSequenceProperties.Property.udate && this.udate!=null)
			value=CDateHelper.format(this.udate,CConstants.UDATE_PATTERN);
		else value=(String)beanhelper.getProperty(this,property.name());
		if (value==null)
			value="";
		return value;
	}
	
	public String getData(List<CSequenceProperties.Property> properties)
	{
		List<String> values=new ArrayList<String>();
		for (CSequenceProperties.Property property : properties)
		{
			String value=getValue(property);
			values.add(value);
		}
		return CStringHelper.join(values,"\t")+"\n";
	}
	
	public String getSequenceFasta()
	{
		return CSequenceHelper.getFastaChunked(this.accession, this.sequence)+"\n";
	}
	
	public String getTranslationFasta()
	{
		if (this.translation==null)
			return "";
		return CSequenceHelper.getFastaChunked(this.accession, this.translation)+"\n";
	}
	
	public void updateAalength()
	{
		if (CStringHelper.hasContent(this.translation))
			this.aalength=this.translation.length();			
	}

	public static CTable createTable(List<CGenbankSequence> sequences)
	{
		CTable table=new CTable();
		for (CSequenceProperties.Property property : PROPERTIES)
		{
			table.getHeader().add(property.name());
		}
		for (CGenbankSequence sequence : sequences)
		{
			CTable.Row row=table.addRow();
			for (CSequenceProperties.Property property : PROPERTIES)
			{
				row.add(sequence.getValue(property));
			}
		}
		//table=table.condense();
		return table;
	}
	
	public static CTable createTable(Collection<CGenbankSequence> sequences, List<CSequenceProperties.Property> properties)
	{
		return createTable(sequences,properties,true);
	}
	
	public static CTable createTable(Collection<CGenbankSequence> sequences, List<CSequenceProperties.Property> properties, boolean condense)
	{
		//System.out.println("createTable: sequences="+sequences.size()+", properties="+properties.size());
		CTable table=new CTable();
		if (properties.size()==1) //skip header row
			table.setShowHeader(false);
		for (CSequenceProperties.Property property : properties)
		{
			table.getHeader().add(property.name());
		}
		for (CGenbankSequence sequence : sequences)
		{
			//System.out.println("next sequence: "+sequence.getAccession());
			CTable.Row row=(sequence.getId()!=null) ? table.addRow(sequence.getId()) : table.addRow();
			for (CSequenceProperties.Property property : properties)
			{
				String value=sequence.getValue(property);
				row.add(value);
				//System.out.println("--adding property "+property+"="+value);
			}
		}
		if (condense && properties.size()>1)
			table=table.condense();
		return table;
	}
	
	public static void setProperty(List<CGenbankSequence> sequences, CSequenceProperties.Property property, Object value)
	{
		for (CGenbankSequence sequence : sequences)
		{
			sequence.setValue(property,value);
		}
	}
	
	public static void setProperties(List<CGenbankSequence> sequences, CTable table, boolean overwrite)
	{
		Map<String,CGenbankSequence> map=new HashMap<String,CGenbankSequence>();
		for (CGenbankSequence sequence : sequences)
		{
			map.put(sequence.getAccession(),sequence);
		}
		for (int index=1;index<table.getHeader().getCells().size();index++)
		{
			String name=table.getHeader().getValue(index);
			CSequenceProperties.Property property=CSequenceProperties.findProperty(name);
			for (CTable.Row row : table.getRows())
			{
				String accession=row.getValue(0);
				Object value=row.getCell(index).getValue();
				CGenbankSequence sequence=map.get(accession);
				if (!overwrite)
				{
					Object curvalue=sequence.getValue(property);
					if (!CStringHelper.hasContent(curvalue))
						sequence.setValue(property, value);
				}
				else sequence.setValue(property, value);
			}
		}
	}
	
	
	public static Map<String,String> createSequenceMap(List<CGenbankSequence> sequences)
	{
		Map<String,String> map=new LinkedHashMap<String,String>();
		for (CGenbankSequence sequence : sequences)
		{
			if (CStringHelper.hasContent(sequence.getSequence()))
				map.put(sequence.getIdentifier(),sequence.getSequence());
		}
		return map;
	}
	
	public static Map<String,String> createSplicedMap(List<CGenbankSequence> sequences)
	{
		Map<String,String> map=new LinkedHashMap<String,String>();
		for (CGenbankSequence sequence : sequences)
		{
			if (CStringHelper.hasContent(sequence.getSpliced()))
				map.put(sequence.getIdentifier(),sequence.getSpliced());
		}
		return map;
	}
	
	public static Map<String,String> createTranslationMap(List<CGenbankSequence> sequences)
	{
		Map<String,String> map=new LinkedHashMap<String,String>();
		for (CGenbankSequence sequence : sequences)
		{
			if (CStringHelper.hasContent(sequence.getTranslation()))
				map.put(sequence.getIdentifier(),sequence.getTranslation());
		}
		return map;
	}
	*/
}
