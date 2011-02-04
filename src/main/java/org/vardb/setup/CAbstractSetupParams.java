package org.vardb.setup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.vardb.util.CBeanHelper;
import org.vardb.util.CDatabaseHelper;
import org.vardb.util.CFileHelper;
import org.vardb.util.CSpringHelper;
import org.vardb.util.CStringHelper;

public abstract class CAbstractSetupParams implements ISetupParams
{
	public static final String ACTION_DELIMITER=",";
	
	protected String setupfile;
	protected String action;
	protected String baseDir;
	protected String tempDir;
	protected String sqlDir;
	protected String xmlDir;
	protected String userfile;
	protected Boolean dropdbifexists;
	protected String importdb;
	protected String schema;
	protected CDatabaseHelper.Params db=new CDatabaseHelper.Params();
	
	@Option(name="-setupfile",usage="setupfile")
	public void setSetupfile(final String setupfile){this.setupfile=setupfile;}
	public String getSetupfile(){return this.setupfile;}

	@Option(name="-action",usage="action")
	public void setAction(final String action){this.action=action;}
	public String getAction(){return this.action;}

	@Option(name="-baseDir")
	public void setBaseDir(final String baseDir){this.baseDir=baseDir;}
	public String getBaseDir(){return this.baseDir;}
	
	@Option(name="-tempDir")
	public void setTempDir(final String tempDir){this.tempDir=tempDir;}
	public String getTempDir(){return this.tempDir;}	

	@Option(name="-sqlDir")
	public void setSqlDir(final String sqlDir){this.sqlDir=sqlDir;}
	public String getSqlDir(){return this.sqlDir;}

	@Option(name="-xmlDir")
	public void setXmlDir(final String xmlDir){this.xmlDir=xmlDir;}
	public String getXmlDir(){return this.xmlDir;}	

	@Option(name="-userfile")
	public void setUserfile(final String userfile){this.userfile=userfile;}
	public String getUserfile(){return this.userfile;}

	@Option(name="-dropdbifexists")
	public void setDropdbifexists(final Boolean dropdbifexists){this.dropdbifexists=dropdbifexists;}
	public Boolean getDropdbifexists(){return this.dropdbifexists;}	

	@Option(name="-importdb")
	public void setImportdb(final String importdb){this.importdb=importdb;}
	public String getImportdb(){return this.importdb;}

	@Option(name="-schema")
	public void setSchema(final String schema){this.schema=schema;}
	public String getSchema(){return this.schema;}	
	
	// db params
	@Option(name="-db-name")
	public void setName(final String name){this.db.setName(name);}
	public String getName(){return this.db.getName();}	
	
	@Option(name="-db-jndi")
	public void setJndi(final String jndi){this.db.setJndi(jndi);}
	public String getJndi(){return this.db.getJndi();}	
	
	@Option(name="-db-driver")
	public void setDriver(final String driver){this.db.setDriver(driver);}
	public String getDriver(){return this.db.getDriver();}	
	
	@Option(name="-db-template")
	public void setTemplate(final String template){this.db.setTemplate(template);}
	public String getTemplate(){return this.db.getTemplate();}	

	@Option(name="-db-encoding")
	public void setEncoding(final String encoding){this.db.setEncoding(encoding);}
	public String getEncoding(){return this.db.getEncoding();}	

	@Option(name="-db-host")
	public void setHost(final String host){this.db.setHost(host);}
	public String getHost(){return this.db.getHost();}	

	@Option(name="-db-port")
	public void setPort(final String port){this.db.setPort(port);}
	public String getPort(){return this.db.getPort();}	

	@Option(name="-db-username")
	public void setUsername(final String username){this.db.setUsername(username);}
	public String getUsername(){return this.db.getUsername();}	

	@Option(name="-db-password")
	public void setPassword(final String password){this.db.setPassword(password);}
	public String getPassword(){return this.db.getPassword();}	

	@Option(name="-db-basedb")
	public void setBasedb(final String basedb){this.db.setBasedb(basedb);}
	public String getBasedb(){return this.db.getBasedb();}	
	
	public CDatabaseHelper.Params getDb(){return this.db;}
	
	public CAbstractSetupParams(){}
	
	public CAbstractSetupParams(String[] args)
	{
		CmdLineParser parser = new CmdLineParser(this);
		try
		{
			parser.parseArgument(args);
		}
		catch (CmdLineException e)
		{
			 System.err.println(e.getMessage());
			 parser.printUsage(System.err);
		}
	}
	
	public List<String> getActions()
	{
		List<String> actions=new ArrayList<String>();
		for (String action : CStringHelper.split(this.action,ACTION_DELIMITER))
		{
			actions.add(action);
		}
		return actions;
	}
	
    public void validate()
	{
		tempDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("tempDir",tempDir));
		sqlDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("sqlDir",sqlDir));
		xmlDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("xmlDir",xmlDir));
		userfile=CFileHelper.normalize(userfile);
		db.validate();
	}
    
    @Transient
    public Map<String,Object> getPropertyMap()
	{
    	Map<String,Object> map=new LinkedHashMap<String,Object>();
    	List<String> exclusions=CStringHelper.splitAsList("class,actions,db,propertyMap,setupfile");
    	for (String name : CBeanHelper.getProperties(this))
    	{
    		if (exclusions.contains(name))
    			continue;
    		//System.out.println("found property: "+name);
    		map.put(name,"${"+name+"}");
    	}
    	return map;
	}
    
	@Override
	public String toString()
	{
		return CStringHelper.toString(this);
	}
}