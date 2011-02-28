package org.vardb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import com.google.protobuf.ByteString.Output;

public abstract class AbstractCommandLineWrapper
{
	//protected IRuntimeService runtimeService=new CRuntimeServiceImpl();
	protected String dir;
	protected String tempDir=null;
	protected List<String> tempfiles=new ArrayList<String>();
	protected String timestamp=null;
	protected boolean deleteTempfiles=false;//true;
	
	//public IRuntimeService getRuntimeService(){return this.runtimeService;}
	//public void setRuntimeService(final IRuntimeService runtimeService){this.runtimeService=runtimeService;}
	
	public AbstractCommandLineWrapper(String dir, String tempDir)
	{
		this.dir=dir;
		this.tempDir=tempDir;
	}
	
	public String createTempfile(String prefix, String suffix, String str)
	{
		String tempfile=this.tempDir+prefix+"-"+getTimestamp()+suffix;
		writeTempfile(tempfile,str);
		return tempfile;
	}
	
	public String expectOutfile(String prefix, String suffix)
	{
		String tempfile=this.tempDir+prefix+"-"+getTimestamp()+suffix;
		addTempfile(tempfile);
		return tempfile;
	}
	
	public String addTempfile(String tempfile)
	{
		this.tempfiles.add(tempfile);
		return tempfile;
	}
	
	public void writeTempfile(String tempfile, String str)
	{
		CFileHelper.writeFile(tempfile,str);
		addTempfile(tempfile);
	}
	
	public void deleteTempfiles()
	{
		if (!this.deleteTempfiles)
			return;
		for (String tempfile : this.tempfiles)
		{
			CFileHelper.deleteFile(tempfile);
		}
	}
	
	private String getTimestamp()
	{
		if (this.timestamp==null)
			this.timestamp=CFileHelper.getTimestamp();
		return this.timestamp;
	}
	
	public CCommandLine.Output exec(CCommandLine commands)
	{	
		System.out.println("COMMAND LINE: "+commands.toString());
		DefaultExecutor executor=commands.createExecutor();
		Stream out = new Stream();
		Stream err = new Stream();
		executor.setStreamHandler(new PumpStreamHandler(out,err));
		Integer exitValue=null;
		try
		{
			exitValue=executor.execute(commands.getCommandLine());
		}
		catch (IOException e)
		{
			throw new CommandLineException(e,commands.toString(),exitValue,out.getAsString(),err.getAsString());
		}
		CCommandLine.Output output=new CCommandLine.Output();
		output.setExitValue(exitValue);
		output.setOut(out.getAsString());
		output.setErr(err.getAsString());
		
		if (commands.getThrowExceptionIfOutputStream() && output.hasOutput())
			throw new CommandLineException("Output stream is not empty",commands.toString(),output);
		if (commands.getThrowExceptionIfErrorStream() && output.hasError())
			throw new CommandLineException("Error stream is not empty",commands.toString(),output);
		return output;
	}

	public static class Stream extends ByteArrayOutputStream
	{
		public String getAsString()
		{
			return new String(toByteArray()).trim();
		}
	}
}