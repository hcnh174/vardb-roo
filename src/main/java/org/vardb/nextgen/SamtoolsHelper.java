package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.CFileHelper;

import com.google.common.collect.Lists;

public class SamtoolsHelper
{
	public static void sam2bam(Params params, List<String> commands)
	{
		for (Params.Sample sample : params.samples)
		{
			sam2bam(params, sample, commands);
		}
	}
	
	public static void sam2bam(Params params, Params.Sample sample, List<String> commands)
	{
		for (Params.Ref ref : sample.refs)
		{
			String root=sample.name+"."+ref.name;
			String samfile=root+".sam";
			String bamfile=root+".bam";			
			commands.add("samtools view -bS -o "+bamfile+" "+samfile);
			commands.add("samtools sort "+bamfile+" "+root);
			commands.add("samtools pileup -cv -f ref/"+ref.name+".fasta "+bamfile);
			commands.add("samtools index "+bamfile);
		}
	}
}
