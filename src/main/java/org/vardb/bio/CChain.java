package org.vardb.bio;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

public class CChain
{
	protected String identifier;
	protected String name;
	protected String swissprot=null;
	protected String sequence="";
	protected String secondaryStructure="";
	protected CStructure structure;
	protected Integer numsequences=0;
	//protected Set<CChainBlastHit> hits=new LinkedHashSet<CChainBlastHit>();
	
	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(final String identifier){this.identifier=identifier;}

	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}
	
	public String getSwissprot(){return this.swissprot;}
	public void setSwissprot(String swissprot){this.swissprot=swissprot;}
	
	public String getSequence(){return this.sequence;}
	public void setSequence(String sequence){this.sequence=sequence;}
	
	public String getSecondaryStructure(){return this.secondaryStructure;}
	public void setSecondaryStructure(String secondaryStructure){this.secondaryStructure=secondaryStructure;}
	
	public Integer getNumsequences(){return this.numsequences;}
	public void setNumsequences(final Integer numsequences){this.numsequences=numsequences;}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="structure_id")
	public CStructure getStructure(){return this.structure;}
	public void setStructure(final CStructure structure){this.structure=structure;}
	
	public CChain() {}
	
	public CChain(String identifier)
	{
		this.identifier=identifier;
		this.name=identifier;
	}
	
	@Transient
	public int getLength()
	{
		return this.sequence.length();
	}

	/*
	public Set<CChainBlastHit> getHits(){return this.hits;}
	public void setHits(final Set<CChainBlastHit> hits){this.hits=hits;}
	
	public void addHit(CChainBlastHit hit)
	{
		if (!this.hits.contains(hit))
		{
			hit.setChain(this);
			hit.setChainname(this.name);
			this.hits.add(hit);
		}
	}
	*/
}