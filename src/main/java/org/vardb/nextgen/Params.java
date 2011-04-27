package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;

import com.google.common.collect.Lists;

public class Params
{
	protected String dir="c:/projects/analysis/nextgen/data/";
	protected String outdir="c:/Documents and Settings/All Users/Documents/nextgen/";//"c:/temp/nextgen/";
	protected String tempdir="c:/temp/";
	protected String sampleRegex="sample[0-9]+";
	
	protected String qseq2fastq_path="c:/research/software/bin/qseq2fastq.pl";
	protected String fastq_btrim_path="c:/research/software/bin/fastq_btrim.pl";
	
	//velvet
	protected int hash_length=25;
	
	// bowtie
	protected int v=3;
	
	protected List<Sample> samples=Lists.newArrayList();
	
	public void addSample(Sample sample)
	{
		this.samples.add(sample);
	}
	
	public void createSamples(int startnum, int endnum, Params.Ref...refs)
	{
		for (int num=startnum; num<=endnum; num++)
		{
			String name=sampleName(num);
			addSample(new Params.Sample(name,refs));
		}	
	}
	
	
	private String sampleName(int num)
	{
		return "sample"+CStringHelper.padLeft(""+num, '0', 2);
	}
	
	
	/////////////////////////////////////////////
	
	
	public static class Ref
	{
		protected String name;
		
		public Ref(String name)
		{
			this.name=name;
		}
	}
	
	public static class Sample
	{
		protected String name;
		protected List<Ref> refs=Lists.newArrayList();
		
		public Sample(String name, Ref...refs)
		{
			this.name=name;
			this.refs=Lists.newArrayList(refs);
		}
	}
}
