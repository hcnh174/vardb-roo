package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.CFileHelper;

import com.google.common.collect.Lists;

public class BowtieHelper
{
	public static void map(Params params, List<String> commands)
	{
		for (Params.Sample sample : params.samples)
		{
			map(params, sample, commands);
		}
	}
	
	public static void map(Params params, Params.Sample sample, List<String> commands)
	{
		String infile="fastq/"+sample.name+".fq";
		for (Params.Ref ref : sample.refs)
		{
			String outfile=sample.name+"."+ref.name+".sam";
			commands.add("bowtie -v "+params.v+" -S "+ref.name+" "+infile+" "+outfile);
		}
	}
}
