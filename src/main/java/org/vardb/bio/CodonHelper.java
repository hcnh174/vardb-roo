package org.vardb.bio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vardb.util.CException;

public class CodonHelper
{
	// private constructor to enforce singleton pattern
	private CodonHelper(){}
	
	// determines the average gc content using a sliding window
	public static List<Float> getGcContent(String sequence, int window)
	{
		List<Float> values=new ArrayList<Float>();
		for (int index=0;index<sequence.length()-window;index++)
		{
			String subsequence=sequence.substring(index,index+window);
			GcContent gc=getGcContent(subsequence);
			values.add(gc.getGc());
		}
		return values;
	}
	
	// calculates GC content based on a nucleotide alignment
	public static List<Float> getGcContent(Collection<String> sequences, int window)
	{
		String sequence=getNtConsensus(sequences);
		System.out.println("nt consensus="+sequence);
		return getGcContent(sequence,window);
	}
		
	public static String getNtConsensus(Collection<String> sequences)
	{
		StringBuilder buffer=new StringBuilder();
		int columns=SequenceHelper.getNumcolumns(sequences);
		for (int index=0;index<columns;index++)
		{
			String nt=getNtConsensus(sequences,index);
			buffer.append(nt);
		}
		return buffer.toString();
	}
	
	public static String getNtConsensus(Collection<String> sequences, int index)
	{
		Map<String,Integer> map=new LinkedHashMap<String,Integer>();
		map.put("A",0);
		map.put("T",0);
		map.put("C",0);
		map.put("G",0);
		for (String sequence : sequences)
		{
			sequence=sequence.toUpperCase();
			String nt=sequence.substring(index,index+1);
			if (SequenceHelper.GAP.equals(nt))
				continue;
			if (!map.containsKey(nt))
			{
				System.out.println("unrecognized nt character: "+nt);
				continue;
			}
			map.put(nt,map.get(nt)+1);
		}
		int max=0;
		String consensus=SequenceHelper.GAP;
		for (String nt : map.keySet())
		{
			int count=map.get(nt);
			if (count>max)
			{
				consensus=nt;
				max=count;
			}
		}
		return consensus;
	}

	public static List<String> getCodons(String sequence)
	{
		int numcolumns=getNumcolumns(sequence);
		sequence=sequence.toUpperCase();
		List<String> codons=new ArrayList<String>();
		for (int column=0;column<numcolumns;column++)
		{
			int start=(column)*3;
			String codon=sequence.substring(start,start+3);
			codons.add(codon);
		}
		return codons;
	}
	
	public static CodonUsageTables compareCodonUsage(List<Collection<String>> list)
	{
		CodonUsageTables tables=new CodonUsageTables();
		int counter=1;
		for (Collection<String> sequences : list)
		{
			for (String sequence : sequences)
			{
				CodonUsageTable table=new CodonUsageTable(sequence);
				tables.add(table,Integer.toString(counter));
				counter++;
			}			
		}
		return tables;
	}
	
	public static List<GcVariability> getGcVariability(Map<String,String> alignment, Map<String,String> codonalignment)
	{
		List<Float> gcs=getGcContent(codonalignment.values(),10);
		Consensus consensus=new Consensus(alignment);
		List<GcVariability> data=new ArrayList<GcVariability>();
		for (int index=0;index<consensus.getPositions().size();index++)
		{
			float variability=consensus.getPositions().get(index).getShannonEntropy();
			int col=index*3;
			if (col+2>=gcs.size()) break;
			data.add(new GcVariability(col+1,gcs.get(col),variability));
			data.add(new GcVariability(col+2,gcs.get(col+1),variability));
			data.add(new GcVariability(col+3,gcs.get(col+2),variability));
		}
		return data;
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	public static int getNumcolumns(Collection<String> sequences)
	{
		return getNumcolumns(sequences.iterator().next());
	}
	
	public static int getNumcolumns(String sequence)
	{
		verifyLength(sequence);
		return sequence.length()/3; 
	}
	
	// verify that it's a multiple of 3
	public static void verifyLength(String sequence)
	{
		if (sequence.length()%3!=0)
			throw new CException("coding sequence is not a multiple of three ("+sequence.length()+"): "+sequence);
	}

	public static class GcVariability
	{
		protected int column;
		protected float gc;
		protected float variability;
		
		public GcVariability(int column, float gc, float variability)
		{
			this.column=column;
			this.gc=gc;
			this.variability=variability;
		}
		
		public int getColumn(){return this.column;}
		public void setColumn(final int column){this.column=column;}
		
		public float getGc(){return this.gc;}
		public void setGc(final float gc){this.gc=gc;}

		public float getVariability(){return this.variability;}
		public void setVariability(final float variability){this.variability=variability;}
	}
	
	public static GcContent getGcContent(String sequence)
	{
		return new GcContent(sequence);
	}
	
	public static GcContent getGcContent(Collection<String> sequences)
	{
		return new GcContent(sequences);
	}
	
	public static class GcContent
	{
		private int total=0;
		private int gnum=0;
		private int cnum=0;
		private int anum=0;
		private int tnum=0;
		private int g3num=0;
		private int c3num=0;
		
		private Float gfreq=null;
		private Float cfreq=null;
		private Float afreq=null;
		private Float tfreq=null;
		private Float gcfreq=null;
		private Float gc3freq=null;
		private Float gc3skewfreq=null;
		
		public GcContent(String sequence)
		{
			getGcContent(sequence,1);
			calculate();
		}
		
		public GcContent(Collection<String> sequences)
		{
			for (String sequence : sequences)
			{
				getGcContent(sequence,1);
			}
			calculate();
		}
		
		public GcContent(Map<Codon,Integer> counts)
		{
			for (Codon codon : counts.keySet())
			{
				int count=counts.get(codon);
				getGcContent(codon.name(),count);					
			}
			calculate();
		}
		
		public Float getG(){return this.gfreq;}
		public Float getC(){return this.cfreq;}
		public Float getA(){return this.afreq;}
		public Float getT(){return this.tfreq;}
		public Float getGc(){return this.gcfreq;}
		public Float getGc3(){return this.gc3freq;}
		public Float getGc3skew(){return this.gc3skewfreq;}
		
		private void getGcContent(String sequence, int count)
		{
			sequence=SequenceHelper.removeGaps(sequence);
			sequence=sequence.toUpperCase();
			if (sequence.length()==0)
				return;
			total+=sequence.length()*count;
			for (int index=0;index<sequence.length();index++)
			{
				char nt=sequence.charAt(index);
				if (nt=='G')
					gnum+=count;
				if (nt=='C')
					cnum+=count;
				if (nt=='A')
					anum+=count;
				if (nt=='T')
					tnum+=count;
				if ((index+1)%3==0)
				{
					if (nt=='G')
						g3num+=count;
					if (nt=='C')
						c3num+=count;
				}
			}
		}
		
		private void calculate()
		{
			float sum=(float)this.total;
			this.gfreq=100.0f*(float)gnum/sum;
			this.cfreq=100.0f*(float)cnum/sum;
			this.afreq=100.0f*(float)anum/sum;
			this.tfreq=100.0f*(float)tnum/sum;			
			this.gcfreq=100.0f*(float)(gnum+cnum)/sum;
			this.gc3freq=100.0f*(float)(g3num+c3num)/sum;
			this.gc3skewfreq=100.0f*(float)(g3num-c3num)/sum;
		}
	}
}