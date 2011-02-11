package org.vardb.bio;

public interface ISequence
{
	String getIdentifier();
	void setIdentifier(final String identifier);

	String getAccession();
	void setAccession(final String accession);

	String getDescription();
	void setDescription(final String description);

	String getSequence();
	void setSequence(final String sequence);

	String getCds();
	void setCds(final String cds);

	String getTranslation();
	void setTranslation(final String translation);

	Integer getNtlength();
	void setNtlength(final Integer ntlength);

	Integer getCdslength();
	void setCdslength(final Integer cdslength);

	Integer getAalength();
	void setAalength(final Integer aalength);

	Integer getStart();
	void setStart(final Integer start);

	Integer getEnd();
	void setEnd(final Integer end);

	StrandType getStrand();
	void setStrand(final StrandType strand);

	Integer getNumexons();
	void setNumexons(final Integer numexons);

	String getSplicing();
	void setSplicing(final String splicing);

	Boolean getPseudogene();
	void setPseudogene(final Boolean pseudogene);

	String getMethod();
	void setMethod(final String method);

	String getModel();
	void setModel(final String model);

	Double getScore();
	void setScore(final Double score);

	Double getEvalue();
	void setEvalue(final Double evalue);

	String getHmmloc();
	void setHmmloc(final String hmmloc);

	Integer getDomainnum();
	void setDomainnum(final Integer domainnum);

	Integer getTotaldomainnum();
	void setTotaldomainnum(final Integer totaldomainnum);

	String getDomains();
	void setDomains(final String domains);

	String getArchitecture();
	void setArchitecture(final String architecture);

	Float getGc();
	void setGc(final Float gc);

	Float getGc3();
	void setGc3(final Float gc3);

	Float getGc3skew();
	void setGc3skew(final Float gc3skew);

	///Date getCreated();
	//void setCreated(final Date created);

	//Date getUpdated();
	//void setUpdated(final Date updated);
}
