package org.vardb.bio;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class SimpleSequence implements ISequence
{
	protected String identifier;
	protected String accession="";
	protected String description="";	
	
	protected String sequence="";
	protected String cds="";
	protected String translation=null;
	
	protected Integer ntlength=0;
	protected Integer cdslength=0;
	protected Integer aalength=0;
	
	protected Integer start=null;
	protected Integer end=null;
	protected StrandType strand=null;
	protected Integer numexons=null;
	protected String splicing="";
	protected Boolean pseudogene=false;
	
	protected String method=null;
	protected String model=null;
	protected Double score=null;
	protected Double evalue=null;
	protected String hmmloc=null;

	protected Integer domainnum=0;
	protected Integer totaldomainnum=0;
	protected String domains="";
	protected String architecture="";
	
	protected Float gc=null;
	protected Float gc3=null;
	protected Float gc3skew=null;

	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(final String identifier){this.identifier=identifier;}

	public String getAccession(){return this.accession;}
	public void setAccession(final String accession){this.accession=accession;}

	public String getDescription(){return this.description;}
	public void setDescription(final String description){this.description=description;}

	public String getSequence(){return this.sequence;}
	public void setSequence(final String sequence){this.sequence=sequence;}

	public String getCds(){return this.cds;}
	public void setCds(final String cds){this.cds=cds;}

	public String getTranslation(){return this.translation;}
	public void setTranslation(final String translation){this.translation=translation;}

	public Integer getNtlength(){return this.ntlength;}
	public void setNtlength(final Integer ntlength){this.ntlength=ntlength;}

	public Integer getCdslength(){return this.cdslength;}
	public void setCdslength(final Integer cdslength){this.cdslength=cdslength;}

	public Integer getAalength(){return this.aalength;}
	public void setAalength(final Integer aalength){this.aalength=aalength;}

	public Integer getStart(){return this.start;}
	public void setStart(final Integer start){this.start=start;}

	@Column(name="finish")
	public Integer getEnd(){return this.end;}
	public void setEnd(final Integer end){this.end=end;}

	@Enumerated(EnumType.STRING)
	public StrandType getStrand(){return this.strand;}
	public void setStrand(final StrandType strand){this.strand=strand;}

	public Integer getNumexons(){return this.numexons;}
	public void setNumexons(final Integer numexons){this.numexons=numexons;}

	public String getSplicing(){return this.splicing;}
	public void setSplicing(final String splicing){this.splicing=splicing;}

	public Boolean getPseudogene(){return this.pseudogene;}
	public void setPseudogene(final Boolean pseudogene){this.pseudogene=pseudogene;}

	public String getMethod(){return this.method;}
	public void setMethod(final String method){this.method=method;}

	public String getModel(){return this.model;}
	public void setModel(final String model){this.model=model;}

	public Double getScore(){return this.score;}
	public void setScore(final Double score){this.score=score;}

	public Double getEvalue(){return this.evalue;}
	public void setEvalue(final Double evalue){this.evalue=evalue;}

	public String getHmmloc(){return this.hmmloc;}
	public void setHmmloc(final String hmmloc){this.hmmloc=hmmloc;}

	public Integer getDomainnum(){return this.domainnum;}
	public void setDomainnum(final Integer domainnum){this.domainnum=domainnum;}

	public Integer getTotaldomainnum(){return this.totaldomainnum;}
	public void setTotaldomainnum(final Integer totaldomainnum){this.totaldomainnum=totaldomainnum;}

	public String getDomains(){return this.domains;}
	public void setDomains(final String domains){this.domains=domains;}

	public String getArchitecture(){return this.architecture;}
	public void setArchitecture(final String architecture){this.architecture=architecture;}

	public Float getGc(){return this.gc;}
	public void setGc(final Float gc){this.gc=gc;}

	public Float getGc3(){return this.gc3;}
	public void setGc3(final Float gc3){this.gc3=gc3;}

	public Float getGc3skew(){return this.gc3skew;}
	public void setGc3skew(final Float gc3skew){this.gc3skew=gc3skew;}

	public SimpleSequence(){}
	
	public SimpleSequence(String identifier)
	{
		this.identifier=identifier;
		this.accession=identifier;
		this.description=identifier;
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
	
	public String getSequence(SequenceType type)
	{
		return (type==SequenceType.NT) ? this.sequence : this.translation;
	}
}