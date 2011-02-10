package org.vardb.bio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vardb.util.CStringHelper;

public class CodonUsageTable
{
	private static final String GAP_CODON="---";
	
	protected String name="Codon usage";
	protected CodonHelper.GcContent gcContent=null;
	protected Map<Codon,Frequency> frequencies=new LinkedHashMap<Codon,Frequency>();
	
	public String getName(){return this.name;}
	public void setName(final String name){this.name=name;}
	
	public CodonHelper.GcContent getGcContent(){return this.gcContent;}
	public void setGcContent(final CodonHelper.GcContent gcContent){this.gcContent=gcContent;}
	
	public CodonUsageTable()
	{
		initializeFrequencies();
	}
	
	public CodonUsageTable(String sequence)
	{
		this();
		this.gcContent=new CodonHelper.GcContent(sequence);
		getCodonUsage(sequence);
		calculate();
	}
	
	public CodonUsageTable(Collection<String> sequences)
	{
		this();
		this.gcContent=new CodonHelper.GcContent(sequences);
		getCodonUsage(sequences);
		calculate();		
	}
	
	public void getCodonUsage(Collection<String> sequences)
	{
		for (String sequence : sequences)
		{
			getCodonUsage(sequence);
		}
	}
	
	public void getCodonUsage(String sequence)
	{
		//System.out.println("getCodonUsage: "+sequence);
		sequence=sequence.toUpperCase();
		int numcolumns=CodonHelper.getNumcolumns(sequence);
		for (int column=0;column<numcolumns;column++)
		{
			int start=(column)*3;
			String codon=sequence.substring(start,start+3);
			count(codon);
		}
	}
	
	public void count(String value)
	{
		if (GAP_CODON.equals(value))
			return;
		Codon codon=Codon.find(value);
		if (codon==null)
		{
			System.out.println("skipping unrecognized codon: "+value);
			return;
		}
		Frequency frequency=getFrequency(codon);
		frequency.increment();
	}
	
	public void setCount(Codon codon, int count)
	{
		getFrequency(codon).setCount(count);
	}
	
	public void setFrequency(Codon codon, float frequency)
	{
		getFrequency(codon).setFrequency(frequency);
	}
	
	public void setRelativeAdaptiveness(Codon codon, float adaptiveness)
	{
		getFrequency(codon).setRelativeAdaptiveness(adaptiveness);
	}
	
	public Frequency getFrequency(Codon codon)
	{
		return this.frequencies.get(codon);
	}

	public List<Frequency> getFrequencies(AminoAcid aa)
	{
		List<Frequency> list=new ArrayList<Frequency>();
		for (Codon codon : this.frequencies.keySet())
		{
			if (codon!=null && !codon.isStopCodon() && codon.getAminoAcid()==aa)
				list.add(this.frequencies.get(codon));
		}
		return list;
	}
	
	public List<Frequency> getData()
	{
		List<Frequency> data=new ArrayList<Frequency>();
		boolean useRelativeAdaptiveness=false;
		FrequencyComparator comparator=new FrequencyComparator(useRelativeAdaptiveness);
		for (AminoAcid aa : AminoAcid.getCoding())
		{
			List<Frequency> list=getFrequencies(aa);
			Collections.sort(list,comparator);
			for (Frequency freq : list)
			{
				data.add(freq);
			}
		}
		return data;
	}
	
	public static List<Codon> getCodonOrder()
	{
		List<Codon> codons=new ArrayList<Codon>();
		for (AminoAcid aa : AminoAcid.getCoding())
		{
			for (Codon codon : Codon.getCodons(aa))
			{
				codons.add(codon);
			}
		}
		return codons;
	}
	
	public void calculate()
	{
		System.out.println("calculting frequencies");
		calculateFrequencies();
		calculateRelativeAdaptiveness();
		calculateRelativeCodonFrequency();
		calculateRelativeSynonymousCodonUsage();
	}
	
	private void calculateFrequencies()
	{
		int total=getTotal(this.frequencies.values());		
		for (Frequency frequency : this.frequencies.values())
		{
			frequency.setFrequency((float)frequency.getCount()/(float)total);
		}
	}
	
	//http://gcua.schoedl.de/faq/cai.html
	private void calculateRelativeAdaptiveness()
	{
		for (AminoAcid aa : AminoAcid.getCoding())
		{
			List<Frequency> list=getFrequencies(aa);
			float max=getMax(list);
			for (Frequency freq : list)
			{
				float frequency=freq.getFrequency();
				float adaptiveness=(frequency*(100.0f/max));
				freq.setRelativeAdaptiveness(adaptiveness);
			}
		}
	}
	
	private void calculateRelativeCodonFrequency()
	{
		for (AminoAcid aa : AminoAcid.getCoding())
		{
			List<Frequency> list=getFrequencies(aa);
			for (Frequency freq : list)
			{
				int n=freq.getCount();
				int sum=getTotal(list);
				freq.setRelativeCodonFrequency((float)n/(float)sum);
			}
		}
	}
	
	// assumes RF is already calculated
	private void calculateRelativeSynonymousCodonUsage()
	{
		for (AminoAcid aa : AminoAcid.getCoding())
		{
			List<Frequency> list=getFrequencies(aa);
			for (Frequency freq : list)
			{
				int degeneracy=Codon.getCodons(aa).size();
				float rf=freq.getRelativeCodonFrequency();
				freq.setRelativeSynonymousCodonUsage(degeneracy*rf);
			}
		}
	}
	
	
	/*
	public CTable getTable()
	{
		CTable table=new CTable();
		table.setShowHeader(false);
		table.getHeader().add("AA");
		for (int num=1;num<=6;num++)
		{
			table.getHeader().add("Codon "+num);	
		}
		boolean useRelativeAdaptiveness=false;
		FrequencyComparator comparator=new FrequencyComparator(useRelativeAdaptiveness);
		for (AminoAcid aa : AminoAcid.values())
		{
			if (aa==AminoAcid.GAP || aa==AminoAcid.X)
				continue;
			CTable.Row row=table.addRow();
			row.add(aa.getShort());
			List<Frequency> list=getFrequencies(aa);
			Collections.sort(list,comparator);
			for (Frequency freq : list)
			{
				Codon codon=freq.getCodon();
				if (useRelativeAdaptiveness)
					row.add(codon.getRna()+" ("+CStringHelper.formatDecimal(freq.getRelativeAdaptiveness(),2)+")");
				else row.add(codon.getRna()+" ("+CStringHelper.formatDecimal(freq.getFrequency(),4)+")");
			}
			for (int num=list.size()+1;num<=6;num++)
			{
				//row.add("&nbsp;");
			}
		}
		return table;
	}
	*/
	
	public AminoAcidUsageTable getAminoAcidUsage()
	{
		AminoAcidUsageTable table=new AminoAcidUsageTable(this.name);
		for (Codon codon : this.frequencies.keySet())
		{
			if (!codon.isStopCodon())
				table.update(codon.getAminoAcid(),this.frequencies.get(codon).getCount());
		}
		table.calculateFrequencies();
		return table;
	}
	
	///////////////////////////
	
	private float getMax(List<Frequency> list)
	{
		float max=0.0f;
		for (Frequency freq : list)
		{
			float frequency=freq.getFrequency();
			if (frequency>max)
				max=frequency;
		}
		return max;
	}
	
	private int getTotal(Collection<Frequency> list)
	{
		int total=0;
		for (Frequency freq : list)
		{
			total+=freq.getCount();
		}
		return total;
	}
	
	private void initializeFrequencies()
	{
		for (Codon codon : Codon.values())
		{
			this.frequencies.put(codon,new Frequency(codon));
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder buffer=new StringBuilder();
		char[] nts=new char[]{'T','C','A','G'};
		List<String> cells=new ArrayList<String>();
		for (char first : nts)
		{
			for (char third : nts)
			{
				for (char second : nts)
				{
					Codon codon=Codon.valueOf(""+first+second+third); // NOPMD
					int count=this.frequencies.get(codon).getCount();
					cells.add(codon.name()+" ("+count+")");
				}
				buffer.append(CStringHelper.join(cells,"\t")+"\n");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public static class Frequency
	{
		protected Codon codon;
		protected Integer count=0;
		protected Float frequency;
		protected Float relativeAdaptiveness;
		protected Float relativeCodonFrequency;//RF
		protected Float relativeSynonymousCodonUsage;//RSCU
		
		public Frequency(Codon codon)
		{
			this.codon=codon;
		}
		
		public Codon getCodon(){return this.codon;}
		
		public Integer getCount(){return this.count;}
		public void setCount(final Integer count){this.count=count;}
		
		public Float getFrequency(){return this.frequency;}
		public void setFrequency(final float frequency){this.frequency=frequency;}

		public Float getRelativeAdaptiveness(){return this.relativeAdaptiveness;}
		public void setRelativeAdaptiveness(final float relativeAdaptiveness){this.relativeAdaptiveness=relativeAdaptiveness;}
		
		public Float getRelativeCodonFrequency(){return this.relativeCodonFrequency;}
		public void setRelativeCodonFrequency(final Float relativeCodonFrequency){this.relativeCodonFrequency=relativeCodonFrequency;}

		public Float getRelativeSynonymousCodonUsage(){return this.relativeSynonymousCodonUsage;}
		public void setRelativeSynonymousCodonUsage(final Float relativeSynonymousCodonUsage){this.relativeSynonymousCodonUsage=relativeSynonymousCodonUsage;}
		
		public void increment()
		{
			update(1);
		}
		
		public void update(int num)
		{
			this.count+=num;
		}
	}
	
	public static class FrequencyComparator implements Comparator<Frequency>
	{
		private boolean useRelativeAdaptiveness=false;
		
		public FrequencyComparator(boolean useRelativeAdaptiveness)
		{
			this.useRelativeAdaptiveness=useRelativeAdaptiveness;	
		}
		
		public int compare(Frequency f1, Frequency f2)
		{
			if (f1.getCodon()!=f2.getCodon())
				return f1.getCodon().compareTo(f2.getCodon());
			if (this.useRelativeAdaptiveness)
				return f1.getRelativeAdaptiveness().compareTo(f2.getRelativeAdaptiveness());
			else return f1.getFrequency().compareTo(f2.getFrequency());
		}
	}
}