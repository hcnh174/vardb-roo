package org.vardb.ngs;

import java.util.List;

import org.vardb.util.CFileHelper;

import com.google.common.collect.Lists;


public class Main
{
	protected Params params;
	protected List<String> folders=Lists.newArrayList();
	
	public static void main(String args[])
	{
		//ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration-demo.xml");
		Params params=new Params();
		
		Main helper=new Main(params);
		
		helper.folders=CFileHelper.listDirectories(params.dir);
		
		helper.qseq2fastq();
		helper.trim();
		
		helper.velveth();
		helper.velvetg();
	}
	
	////////////////////////////////////////
	
	public Main(Params params)
	{
		this.params=params;
	}
	
	///////////////////////////////////////////
	
	public void qseq2fastq()
	{
		// convert to fastq format
		for (String dir : folders)
		{
			for (String filename : CFileHelper.listFiles(dir,".txt",true))
			{
				qseq2fastq(filename);
			}
		}
	}
	
	public void qseq2fastq(String filename)
	{
		// convert to fastq format
		String cmd="perl qseq2fastq.pl -a "+filename;
		System.out.println(cmd);
	}
	
	///////////////////////////////////////////
	
	public void trim()
	{
		// convert to fastq format
		for (String dir : folders)
		{
			for (String filename : CFileHelper.listFiles(dir,".fq",true))
			{
				trim(filename);
			}
		}
	}
	
	public void trim(String filename)
	{
		// convert to fastq format
		String cmd="perl fastq_btrim.pl -a "+filename;
		System.out.println(cmd);
	}
	
	////////////////////////////////////////////////////////////
	
	public void velveth()
	{
		// convert to fastq format
		for (String folder : folders)
		{
			velveth(folder);
		}
	}
	
	public void velveth(String folder)
	{
		folder=CFileHelper.stripPath(folder);
		String cmd="./velveth "+this.params.outdir+"/"+folder+" "+params.hash_length+" -fastq "+folder+"/*.fq";
		System.out.println(cmd);
	}
	
	////////////////////////////////////////////////////////////
	
	public void velvetg()
	{
		// convert to fastq format
		for (String folder : folders)
		{
			velvetg(folder);
		}
	}
	
	public void velvetg(String folder)
	{
		String cmd="./velvetg "+this.params.outdir+"/"+folder;
		System.out.println(cmd);
	}
	
////////////////////////////////////////////////////////////
	
	public static class Params
	{
		protected String dir;
		protected String outdir;
		
		//velvet
		protected int hash_length=25;
	}
}