package org.vardb.setup;

//import groovy.lang.GroovyShell;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CMessageWriter;
import org.vardb.util.CXmlHelper;
import org.vardb.util.CXmlValidationException;


public class Setup {

	public static void main(String ... args) throws Exception {
		
		/*
		DataFrame<Integer,String,String> dataframe=new DataFrame<Integer,String,String>();//("patientid","name","birthdate");
		dataframe.put(8888,"name","Bob Jones");
		dataframe.put(8888,"birthdate","1/1/2001");
		dataframe.put(9999,"name","Rita Smith");
		dataframe.put(9999,"birthdate","9/9/1999");
		System.out.println(dataframe.toString());
		*/
		
		//java groovy.lang.GroovyShell foo/MyScript.groovy [arguments]
		//GroovyShell.main(new String[]{"setup.groovy"});
		//groovy.lang.GroovyShell.main(groovy.lang.GroovyShell.EMPTY_ARGS);
		
		
		String basedir="C:/dropbox/My Dropbox/vardb/dr-5/";
		//String basedir="C:/Documents and Settings/nhayes/My Documents/My Dropbox/vardb/dr-5/";
		//String dir="C:/Documents and Settings/nhayes/My Documents/My Dropbox/vardb/dr-5/sequence/";
		String dir=basedir+"sequence/";
		Setup setup=new Setup();
		String filename="anaplasma.marginale_st_maries-msp2_p44_map1_omp.txt";
		setup.loadSequences(dir+filename);	
	}
	
	public void loadSequences(String filename) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/batch-setup.xml");
		JobLauncher jobLauncher = context.getBean(JobLauncher.class);
		Job job = (Job) context.getBean("loadSequences");

		JobParameters jobparams=new JobParametersBuilder().addString("resource",filename).toJobParameters();
		JobExecution jobExecution = jobLauncher.run(job,jobparams);
		System.out.println("Job ended with status: "+jobExecution.getExitStatus());
	}
	
	public void loadXml(String xml, CMessageWriter writer)
	{
		try
		{
			CXmlDataReader xmlloader=new CXmlDataReader(writer);
			xmlloader.loadXml(xml);
		}
		catch(Exception e)
		{
			CFileHelper.writeFile("c:/setup.xml",xml);
			throw new CException(e);
		}
	}

	public void loadXmlFromFile(String filename, CMessageWriter writer)
	{
		loadXml(CFileHelper.readFile(filename),writer);
		writer.message("Finished loading resources from file: "+filename);
	}
	
	public void loadXmlFromFolder(String folder, CMessageWriter writer)
	{
		String xml=CXmlHelper.mergeXmlFiles(folder,CXmlDataReader.ROOT);
		loadXml(xml,writer);
		writer.message("Finished loading resources from folder: "+folder);
	}
	
	public void validate(String xml, String schema, CMessageWriter writer)
	{
		try
		{
			String xsd=CFileHelper.getResource(schema);
			CXmlHelper.validate(xml,xsd);
		}
		catch(CXmlValidationException e)
		{
			writer.message(e.getMessage());
		}
	}
	
	public void validateFolder(String folder, String schema, CMessageWriter writer)
	{
		List<String> filenames=CFileHelper.listFilesRecursively(folder,".xml");
		for (String filename : filenames)
		{
			String xml=CFileHelper.readFile(filename);
			validate(xml,schema,writer);
		}
	}
}
