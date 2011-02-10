package org.vardb.bio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AminoAcidUsageTable
{
	public enum CType{VARIABLE,CONSERVED,UPSTREAM,DOWNSTREAM};
	
	protected String name="Amino acid usage";
	protected Map<AminoAcid,Integer> counts=new HashMap<AminoAcid,Integer>();
	protected Map<AminoAcid,Float> frequencies=null;

	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}
	
	public Map<AminoAcid,Integer> getCounts(){return this.counts;}
	
	// preload counts	
	public AminoAcidUsageTable()
	{
		for (AminoAcid aa : AminoAcid.values())
		{
			this.counts.put(aa,0);
		}
	}
	
	public AminoAcidUsageTable(String sequence)
	{
		this();
		getAminoAcidUsage(sequence);
	}
	
	public AminoAcidUsageTable(Collection<String> sequences)
	{
		this();
		getAminoAcidUsage(sequences);
	}	

	public void getAminoAcidUsage(Collection<String> sequences)
	{
		for (String sequence : sequences)
		{
			getAminoAcidUsage(sequence);
		}
	}
	
	public void getAminoAcidUsage(String sequence)
	{
		for (int index=0;index<sequence.length();index++)
		{
			String aa=sequence.substring(index,index+1);
			count(aa);
		}
	}
	
	public void count(String value)
	{
		if (SequenceHelper.GAP.equals(value))
			return;
		AminoAcid aa=AminoAcid.find(value);
		count(aa);
	}
	
	public void count(AminoAcid aa)
	{
		update(aa,1);
	}
	
	public void update(AminoAcid aa, int count)
	{
		int newcount=this.counts.get(aa)+count;
		this.counts.put(aa,newcount);
	}

	public int getCount(AminoAcid aa)
	{
		return this.counts.get(aa);
	}
	
	public float getFrequency(AminoAcid aa)
	{
		return getFrequencies().get(aa);
	}
	
	public Map<AminoAcid,Float> getFrequencies()
	{
		if (this.frequencies==null)
			this.frequencies=calculateFrequencies();
		return this.frequencies;
	}
	
	protected Map<AminoAcid,Float> calculateFrequencies()
	{
		float total=getTotal();
		Map<AminoAcid,Float> frequencies=new HashMap<AminoAcid,Float>();
		for (AminoAcid aa : this.counts.keySet())
		{
			float freq=(float)this.counts.get(aa)/(float)total;
			frequencies.put(aa,freq);
		}
		return frequencies;
	}
	
	public float getTotal()
	{
		int total=0;
		for (AminoAcid aa : this.counts.keySet())
		{
			if (aa!=AminoAcid.GAP)
				total+=this.counts.get(aa);
		}
		return total;
	}
	
	public float getFrequencyByGroup(AminoAcidGroup group)
	{
		int sum=0;
		float total=getTotal();
		for (AminoAcid aa : this.counts.keySet())
		{
			if (aa.getGroup()!=null && aa.getGroup()==group)
				sum+=this.counts.get(aa);
		}
		return (float)sum/total;
	}
	
	/*
	public String getTable()
	{
		Map<AminoAcid,Float> frequencies=getFrequencies();
		StringBuilder buffer=new StringBuilder();
		buffer.append("aa\tname\tcode\tcount\tfrequency\n");
		for (AminoAcid aa : AminoAcid.values())
		{
			buffer.append(aa.getLongname());
			buffer.append("\t");
			buffer.append(aa.getCode());
			buffer.append("\t");
			buffer.append(getCount(aa));
			buffer.append("\t");
			buffer.append(frequencies.get(aa));
			buffer.append("\n");
		}
		return buffer.toString();
	}
	*/
	
	public String getTable()
	{
		List<Frequency> items=getData();
		StringBuilder buffer=new StringBuilder();
		buffer.append("aa\tname\tcode\tcount\tfrequency\n");
		for (Frequency item : items)
		{
			buffer.append(item.getAa().getLongname());
			buffer.append("\t");
			buffer.append(item.getAa().getCode());
			buffer.append("\t");
			buffer.append(item.getCount());
			buffer.append("\t");
			buffer.append(item.getFrequency());
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public List<Frequency> getData()
	{
		Map<AminoAcid,Float> frequencies=getFrequencies();
		List<Frequency> data=new ArrayList<Frequency>();
		for (AminoAcid aa : AminoAcid.values(true))
		{
			data.add(new Frequency(aa,getCount(aa),frequencies.get(aa)));

		}
		return data;
	}
	
	public static class Frequency
	{
		protected AminoAcid aa;
		protected Integer count;
		protected Float frequency;
		
		public Frequency(AminoAcid aa, int count, float frequency)
		{
			this.aa=aa;
			this.count=count;
			this.frequency=frequency;
		}
		
		public AminoAcid getAa(){return this.aa;}
		public void setAa(final AminoAcid aa){this.aa=aa;}

		public Integer getCount(){return this.count;}
		public void setCount(final Integer count){this.count=count;}

		public Float getFrequency(){return this.frequency;}
		public void setFrequency(final Float frequency){this.frequency=frequency;}
	}
}
