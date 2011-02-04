package org.vardb.setup;

import java.util.List;

import org.vardb.util.CFileHelper;

/*
 Database setup
 * get name of database from args
 * create a new database in SQL server
 * concatenate SQL files and load database setup scripts
 * create root user
 * load XML data
 * load sequences
 * update NCBI data
 * index Pfam domains
 * update counts
 * create BLAST database
 *
 JBoss configuration
 * rename old datasource XML file if it exists and create a new one
 * check if required jar files are present in jboss.lib and copy if not
 */
public class CSetup extends CAbstractSetup
{	
	public static final String TABLE_SUFFIX=".txt";
	
	public static void main(String[] argv)
	{		
		CSetup setup=new CSetup(argv);
		setup.execute();
	}
	
	public CSetup(String[] argv)
	{
		super(new CSetupParams(argv));
	}
	
	public void execute()
	{
		for (String action : params.getActions())
		{
			System.out.println("executing action: "+action);
			CAction.find(action).execute(this);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	
	@Override
	public void xml()
	{
		//getAdminService().validateFolder(params.getXmlDir(), params.getSchema(), writer);
		//getAdminService().loadXmlFromFolder(params.getXmlDir(),writer);
	}
	
	@Override
	public void loadusers()
	{
		System.out.println("loadusers: "+params.getUserfile());
		//if (CStringHelper.hasContent(params.getUserfile()) && CFileHelper.exists(params.getUserfile()))
		//	getAdminService().loadXmlFromFile(params.getUserfile(),writer);
	}
	
	public void sequences()
	{
		CSetupParams params=(CSetupParams)this.params;
		String folder=params.getSequenceDir();
		writer.message("loading tables from folder: "+folder);
		List<String> filenames=CFileHelper.listFilesRecursively(folder,TABLE_SUFFIX,loadparams.getDate());
		//IAdminService adminService=getAdminService();
		for (String filename : filenames)
		{
			//adminService.loadSequenceTableFromFile(filename,loadparams);
		}
		writer.message("Clearing cache");
		writer.message("Finished loading tables from folder: "+folder);		
	}
	
	///////////////////////////////////////////////////////////////////
	
	/*
	private IAdminService getAdminService()
	{
		return (IAdminService)getApplicationContext().getBean("adminService");
	}
	*/	
	
	/*
	IBatchService getBatchService()
	{
		return (IBatchService)getApplicationContext().getBean("batchService");
	}
	
	ICountService getCountService()
	{
		return (ICountService)getApplicationContext().getBean("countService");
	}
	
	IResourceService getResourceService()
	{
		return (IResourceService)getApplicationContext().getBean("resourceService");
	}
	
	IBlastService getBlastService()
	{
		return (IBlastService)getApplicationContext().getBean("blastService");
	}
	*/
}
