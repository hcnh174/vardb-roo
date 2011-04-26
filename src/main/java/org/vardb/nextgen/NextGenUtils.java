package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.AbstractCommandLineWrapper;
import org.vardb.util.CCommandLine;
import org.vardb.util.CFileHelper;

import com.google.common.collect.Lists;

public class NextGenUtils
{	
	public static final String QSEQ_FILENAME_REGEX="s_[0-9]_[0-9]_[0-9]+_qseq\\.txt";
	public static final String FASTQ_FILENAME_REGEX="s_[0-9]_[0-9]_[0-9]+_qseq\\.fq";
	public static final String TRIMMED_FILENAME_REGEX="s_[0-9]_[0-9]_[0-9]+_qseq_Btrim\\.fq";
	
	public static List<String> getSampleFolders(String dir, String regex)
	{
		List<String> folders=Lists.newArrayList();
		for (String folder : CFileHelper.listDirectories(dir))
		{
			//if (folder.startsWith(prefix))
			if (folder.matches(regex))
				folders.add(folder);
		}
		return folders;
	}
	
	public static String getPath(String basedir, String folder)
	{
		return basedir+folder;
	}
	
	public static String getPath(String basedir, String folder, String filename)
	{
		return basedir+folder+"/"+filename;
	}
	
	public static List<String> getQseqFiles(Params params, String sample)
	{
		return listFilesByRegex(params.dir+sample,QSEQ_FILENAME_REGEX);
	}
	
	public static List<String> getFastqFiles(Params params, String sample)
	{
		return listFilesByRegex(params.outdir+sample,FASTQ_FILENAME_REGEX);
	}
	
	public static List<String> getTrimmedFiles(Params params, String sample)
	{
		return listFilesByRegex(params.outdir+sample,TRIMMED_FILENAME_REGEX);
	}
	
	public static List<String> listFilesByRegex(String dir, String regex)
	{
		List<String> files=Lists.newArrayList();
		for (String filename : CFileHelper.listFiles(dir))
		{
			if (filename.matches(regex))
				files.add(filename);
		}
		return files;
	}
	
	//////////////////////////////////////////////
	
	// concatenates all qseq files in a directory into a single sampleNN.qseq file in the parent directory
	public static void concatenate(Params params, List<String> samples)
	{
		for (String sample : samples)
		{
			concatenate(params,sample);
		}
	}
	
	public static void concatenate(Params params, String sample)
	{
		//String cmd="cat "+sample+"/*_qseq.txt > "+params.outdir+sample+".qseq";
		String cmd="cat "+sample+"/*.txt > samples/"+sample+".qseq";
		System.out.println(cmd);
		String outdir=params.dir;
		//exec(params,outdir,cmd);
	}
	
	////////////////////////////////////////
	
	public static void qseq2fastq(Params params, List<String> samples)
	{
		for (String sample : samples)
		{
			qseq2fastq(params,sample);
		}
	}
	
	public static void qseq2fastq(Params params, String sample)
	{
		for (String filename : getQseqFiles(params,sample))
		{
			qseq2fastq(params, sample, filename);
		}
	}
	
	public static void qseq2fastq(Params params, String sample, String filename)
	{
		String cmd="perl "+params.qseq2fastq_path+" -a "+getPath(params.dir,sample,filename);
		String outdir=params.outdir+sample+"/";
		exec(params,outdir,cmd);
	}
	
	public static void qseq2fastqDirect(Params params, String indir, String outdir)
	{
		for (String sample : CFileHelper.listFiles(indir,".qseq"))
		{
			String cmd="perl "+params.qseq2fastq_path+" -a "+indir+sample;
			exec(params,outdir,cmd);
		}
	}
	
	///////////////////////////////////////////
	
	public static void fastq_btrim(Params params, List<String> samples)
	{
		for (String sample : samples)
		{
			fastq_btrim(params,sample);
		}
	}
	
	public static void fastq_btrim(Params params, String sample)
	{
		for (String filename : getFastqFiles(params,sample))
		{
			fastq_btrim(params, sample, filename);
		}
	}
	
	public static void fastq_btrim(Params params, String sample, String filename)
	{
		String cmd="perl "+params.fastq_btrim_path+" -v T -a "+getPath(params.outdir,sample,filename);
		String outdir=params.outdir+sample+"/";
		exec(params,outdir,cmd);
	}
	
	///////////////////////////////
	
	public static void exec(Params params, String workdir, String command)
	{
		CommandExecutor executor=new CommandExecutor(params, workdir);
		CFileHelper.createDirectory(workdir);
		CCommandLine commandline=new CCommandLine(command);
		commandline.setWorkingDir(workdir);
		CCommandLine.Output output=executor.exec(commandline);
		System.out.println(output.getOut());
	}
	
	public static class CommandExecutor extends AbstractCommandLineWrapper
	{
		public CommandExecutor(Params params)
		{
			super(params.dir, params.tempdir);
		}
		
		public CommandExecutor(Params params, String outdir)
		{
			super(outdir, params.tempdir);
		}
	}
}
