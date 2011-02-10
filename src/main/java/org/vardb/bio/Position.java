package org.vardb.bio;

import java.util.HashMap;
import java.util.Map;

public class Position
{
	protected int number;
	protected Map<AminoAcid,Integer> counts=new HashMap<AminoAcid,Integer>();
	protected AminoAcid consensus=null;
	protected Float identity=null;
	protected Float variability=null;

	public Position(int number)
	{
		this.number=number;
		for (AminoAcid aa : AminoAcid.values(true))
		{
			this.counts.put(aa,0);
		}
		this.counts.put(AminoAcid.GAP,0);
	}
	
	public int getNumber(){return this.number;}
	public void setNumber(int number){this.number=number;}

	public Map<AminoAcid,Integer> getCounts(){return this.counts;}

	public void setConsensus(AminoAcid consensus){this.consensus=consensus;}
	public void setIdentity(Float identity){this.identity=identity;}
	public void setVariability(Float variability){this.variability=variability;}
	
	public boolean isEmpty()
	{
		int total=0;
		for (AminoAcid aa : AminoAcid.values(true))
		{
			int count=getCount(aa);
			total+=count;
		}
		return (total==0);
	}
	
	public void count(AminoAcid aa)
	{
		count(aa,1);
	}
	
	public void count(AminoAcid aa, int num)
	{
		Integer count=this.counts.get(aa);
		if (count==null)
			count=0;
		this.counts.put(aa,count+num);
	}

	public AminoAcid getConsensus()
	{
		if (this.consensus==null)
			this.consensus=VariabilityHelper.getConsensus(this.counts,0.5f);
		return this.consensus;
	}
	
	public int getCount(AminoAcid aa)
	{
		return this.counts.get(aa);
	}
	
	public float getIdentity()
	{
		if (this.identity==null)
			this.identity=VariabilityHelper.getIdentity(this.counts);
		return this.identity;
	}
	
	public float getShannonEntropy()
	{
		return VariabilityHelper.getShannonEntropy(this.counts);
	}
	
	public float getSimpsonDiversityIndex()
	{
		return VariabilityHelper.getSimpsonDiversityIndex(this.counts);
	}

	public float getWuKabatVariabilityCoefficient()
	{
		return VariabilityHelper.getWuKabatVariabilityCoefficient(this.counts);
	}
}