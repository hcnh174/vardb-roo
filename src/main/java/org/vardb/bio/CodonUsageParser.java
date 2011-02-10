package org.vardb.bio;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vardb.util.CStringHelper;

public final class CodonUsageParser
{
	// private constructor to enforce singleton pattern
	private CodonUsageParser(){}
	
	//UUU 26.8(   464)  UCU 16.1(   278)  UAU 28.0(   485)  UGU 10.2(   176)
	public static CodonUsageTable parse(String str)
	{
		CodonUsageTable table=new CodonUsageTable();
		Map<Codon,Integer> counts=new LinkedHashMap<Codon,Integer>();
		str=str.trim();
		String regex="([AGCU]{3})\\s+([0-9]+\\.[0-9]+)\\(\\s*([0-9]+)\\)";
		Pattern pattern=Pattern.compile(regex);
		int sum=0;
		for (String line : CStringHelper.splitLines(str))
		{
			Matcher matcher=pattern.matcher(line);
			while (matcher.find())
			{
				String triplet=matcher.group(1);
				//float frequency=Float.parseFloat(matcher.group(2));
				int number=Integer.parseInt(matcher.group(3));
				Codon codon=Codon.find(triplet);
				//System.out.println("codon="+codon.getRna()+" frequency="+frequency+" number="+number);
				counts.put(codon,number);
				sum+=number;
			}
		}
		//System.out.println("total="+sum);
		for (Codon codon : Codon.values())
		{
			Integer count=counts.get(codon);
			if (count==null)
				count=0;
			float frequency=(float)count/(float)sum;
			//System.out.println(codon.getRna()+": "+count);
			table.setCount(codon,count);
			table.setFrequency(codon,frequency);
		}
		table.calculate();// calculateRelativeAdaptiveness
		
		table.setGcContent(new CodonHelper.GcContent(counts));
		return table;
	}
}