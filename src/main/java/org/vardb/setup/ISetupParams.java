package org.vardb.setup;

import java.util.List;
import java.util.Map;

import org.vardb.util.CDatabaseHelper;

public interface ISetupParams
{ 
	void setSetupfile(String setupfile);
	String getSetupfile();

	void setAction(String action);
	String getAction();
	
	String getBaseDir();
	String getTempDir();
	String getSqlDir();
	String getXmlDir();
	String getUserfile();
	Boolean getDropdbifexists();
	String getImportdb();
	String getSchema();
	CDatabaseHelper.Params getDb();
	List<String> getActions();
	Map<String,Object> getPropertyMap();
	void validate();
}