package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.CFileHelper;

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
	
		List<String> samples=NextGenUtils.getSampleFolders(params.dir, params.sampleRegex);
		
		String str=MaqHelper.easyrun(params, "ref/HHaa156.fasta", samples);
		System.out.println(str);
		
		//NextGenUtils.concatenate(params, samples);
		
		//NextGenUtils.qseq2fastq(params, samples);
		//NextGenUtils.qseq2fastqDirect(params, params.dir+"samples/", params.outdir);
		
		//NextGenUtils.fastq_btrim(params, samples);
		
		//VelvetHelper.velveth(params,samples);
		//VelvetHelper.velveth(params,"sample19");
	}
}
