package org.vardb.bio;

import java.util.ArrayList;
import java.util.List;

public class CStructure
{
	protected String identifier;
	protected String name;
	protected String description;
	protected String technique=null;
	protected Float resolution=null;
	protected Integer numchains=0;
	protected Integer numsequences=0;
	protected List<CChain> chains=new ArrayList<CChain>();
	
	public String getIdentifier(){return this.identifier;}
	public void setIdentifier(final String identifier){this.identifier=identifier;}

	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}
	
	public String getDescription(){return this.description;}
	public void setDescription(final String description){this.description=description;}
	
	public String getTechnique(){return this.technique;}
	public void setTechnique(String technique){this.technique=technique;}
	
	public Float getResolution(){return this.resolution;}
	public void setResolution(Float resolution){this.resolution=resolution;}
	
	public Integer getNumchains(){return this.numchains;}
	public void setNumchains(final Integer numchains){this.numchains=numchains;}
	
	public Integer getNumsequences(){return this.numsequences;}
	public void setNumsequences(final Integer numsequences){this.numsequences=numsequences;}
	
	public List<CChain> getChains(){return this.chains;}
	public void setChains(final List<CChain> chains){this.chains=chains;}

	public CStructure() {}
	
	public CStructure(String pdbId)
	{
		this.identifier=pdbId.toUpperCase();
		this.name=this.identifier;
	}
	
	public void add(CChain chain)
	{
		chain.setStructure(this);
		getChains().add(chain);
		chain.setIdentifier(getName()+chain.getName());
	}

	public CChain findOrCreateChain(String name)
	{
		CChain chain=findChain(name);
		if (chain==null)
		{
			chain=new CChain(name);
			add(chain);
		}
		return chain;
	}
	
	public CChain findChain(String name)
	{
		for (CChain chain : this.chains)
		{
			if (chain.getName().equals(name))
				return chain;
		}
		return null;
	}
}