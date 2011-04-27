package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;

import com.google.common.collect.Lists;

//read in directories and sample information
// convert all files to fasta or at least fastq
// trim reads using quality control
// run velveth along with reference sequences
// run velvetg
// align contigs with reference sequence
// call snps
public class Main
{
	public static void main(String args[])
	{
		Params params=new Params();
	
		//List<String> samples=NextGenUtils.getSampleFolders(params.dir, params.sampleRegex);
		
		//String str=MaqHelper.easyrun(params, "ref/HHaa156.fasta", samples);
		//System.out.println(str);
		
		Params.Ref HHaa36=new Params.Ref("HHaa36");
		Params.Ref HHaa156=new Params.Ref("HHaa156");
		Params.Ref NKaa36=new Params.Ref("NKaa36");
		Params.Ref NKaa156=new Params.Ref("NKaa156");
		Params.Ref KT9aa36=new Params.Ref("KT9aa36");
		Params.Ref KT9aa156=new Params.Ref("KT9aa156");

		params.createSamples(1, 3, HHaa36, HHaa156);
		params.createSamples(4, 7, NKaa36, NKaa156);
		params.createSamples(8, 17, KT9aa36, KT9aa156);
		params.createSamples(18, 20, HHaa36, HHaa156);
		
		List<String> commands=Lists.newArrayList();
		BowtieHelper.map(params, commands);
		
		//System.out.println(makeScript(commands));
		
		commands=Lists.newArrayList();
		SamtoolsHelper.sam2bam(params, commands);
		
		System.out.println(makeScript(commands));

		
		//NextGenUtils.concatenate(params, samples);
		
		//NextGenUtils.qseq2fastq(params, samples);
		//NextGenUtils.qseq2fastqDirect(params, params.dir+"samples/", params.outdir);
		
		//NextGenUtils.fastq_btrim(params, samples);
		
		//VelvetHelper.velveth(params,samples);
		//VelvetHelper.velveth(params,"sample19");
	}
	
	public static String makeScript(List<String> commands)
	{
		return CStringHelper.join(commands,"\n");
		
	}
	
	
	
}
