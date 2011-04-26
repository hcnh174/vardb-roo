package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.CFileHelper;

import com.google.common.collect.Lists;

public class VelvetHelper
{
	public static void velveth(Params params, List<String> samples)
	{
		// convert to fastq format
		for (String sample : samples)
		{
			velveth(params, sample);
		}
	}
	
	public static void velveth(Params params, String sample)
	{
		String cmd="./velveth "+NextGenUtils.getPath(params.outdir,sample)+" "+params.hash_length+" -fastq "+sample+"*_Btrim.fq";
		System.out.println(cmd);
	}
	
	////////////////////////////////////////////////////////////
	
	public static void velvetg(Params params, List<String> folders)
	{
		// convert to fastq format
		for (String folder : folders)
		{
			velvetg(params,folder);
		}
	}
	
	public static void velvetg(Params params, String folder)
	{
		String cmd="./velvetg "+params.outdir+"/"+folder;
		System.out.println(cmd);
	}
}
