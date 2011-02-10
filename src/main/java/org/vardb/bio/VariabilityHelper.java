package org.vardb.bio;

import java.util.LinkedHashMap;
import java.util.Map;

import org.vardb.util.CMathHelper;

public final class VariabilityHelper
{
	// private constructor to enforce singleton pattern
	private VariabilityHelper(){}
	
	public static float getIdentity(Map<AminoAcid,Integer> counts)
	{
		int max=0;
		int total=0;
		for (AminoAcid aa : counts.keySet())
		{
			if (aa==AminoAcid.GAP)
				continue;
			int count=counts.get(aa);
			total+=count;
			if (count>max)
				max=count;
		}
		return (float)max/(float)total;
	}
	
	public static AminoAcid getConsensus(Map<AminoAcid,Integer> counts, float threshold)
	{
		int max=0;
		int total=0;
		AminoAcid consensus=null;
		for (AminoAcid aa : counts.keySet())
		{
			if (aa==AminoAcid.GAP)
				continue;
			int count=counts.get(aa);
			total+=count;
			if (count>max)
			{
				consensus=aa;
				max=count;
			}
		}
		float cutoff=((float)total*threshold);
		if (max>=cutoff && max>1)
			return consensus;
		else return AminoAcid.GAP;
	}
	
	public static AminoAcid getConsensusByCodon(Map<Codon,Integer> counts, float threshold)
	{
		Map<AminoAcid,Integer> aacounts=convertCodonCountsToAminoAcidCounts(counts);
		return getConsensus(aacounts,threshold);
	}
	
	public static Map<AminoAcid,Integer> convertCodonCountsToAminoAcidCounts(Map<Codon,Integer> counts)
	{
		Map<AminoAcid,Integer> aacounts=new LinkedHashMap<AminoAcid,Integer>();
		for (Codon codon : counts.keySet())
		{
			AminoAcid aa=codon.getAminoAcid();
			Integer count=aacounts.get(aa);
			if (count==null)
				count=0;
			aacounts.put(aa,count+1);
		}
		return aacounts;
	}
	
	// H=-E(i=1..M)Pilog2Pi
	// where Pi is the fraction of residues of amino acid type i and M is the number of amino acid types
	public static float getShannonEntropy(Map<? extends Object,Integer> counts)
	{
		float total=0;
		for (Object key : counts.keySet())
		{
			int count=counts.get(key);
			total+=count;
		}
		
		double sum=0;
		for (Object key : counts.keySet())
		{
			float count=(float)counts.get(key);
			if (count==0)
				continue;
			double Pi=count/total;
			double log2Pi=CMathHelper.log2(Pi);
			sum+=Pi*log2Pi;
		}
		// hack! if zero returns, -0.000
		if (sum==0)
			return 0;		
		return (float)(-1.0d*sum);  
	}
	
	// D=1-E(i=1..S) ni*(ni-1) / N*(N-1)
	public static float getSimpsonDiversityIndex(Map<? extends Object,Integer> counts)
	{
		float N=0;
		for (Object key : counts.keySet())
		{
			N+=counts.get(key);
		}
		if (N<=1)
		{
			//System.out.println("skipping for Simpson diversity: N="+N);
			return 0;
		}
		float sum=0;
		for (Object key : counts.keySet())
		{
			float n=(float)counts.get(key);
			sum+=(n*(n-1))/(N*(N-1));
		}
		return 1-sum;
	}
	
	// variability=(N*k)/n
	// where N is the number of sequences in the alignment
	// k is the number of different amino acids at a given position
	// n is the frequency of the most common amino acid at that position
	public static float getWuKabatVariabilityCoefficient(Map<? extends Object,Integer> counts)
	{
		int N=0, k=0, n=0;
		for (Object key : counts.keySet())
		{
			int count=counts.get(key);
			N+=count;				
			if (count>0)
				k++;
			if (count>n)
				n=count;
		}
		if (n==0)
			return 0;
		return ((float)N*(float)k)/(float)n;
	}
}