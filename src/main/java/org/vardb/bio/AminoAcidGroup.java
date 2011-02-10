package org.vardb.bio;

import org.vardb.util.CException;

public enum AminoAcidGroup
{	
	SMALL("Small",1),
	NUCLEOPHILIC("Nucleophilic",2),
	HYDROPHOBIC("Hydrophobic",3),
	AROMATIC("Aromatic",4),
	ACIDIC("Acidic",5),
	AMIDE("Amide",6),
	BASIC("Basic",7);
	
	private final String name;
	private final Integer number;

	AminoAcidGroup(String name, Integer number)
	{
		this.name=name;
		this.number=number;
	}
	
	public String getName(){return this.name;}
	public Integer getNumber(){return this.number;}
	
	public static AminoAcidGroup find(String name)
	{
		for (AminoAcidGroup type : AminoAcidGroup.values())
		{
			if (type.getName().equals(name))
				return type;
		}
		throw new CException("can't find CAminoAcid: "+name);
	}
}