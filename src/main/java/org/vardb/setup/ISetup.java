package org.vardb.setup;

public interface ISetup
{   
	void dropdb();
	void createdb();
	void sql();
	void loadusers();
	//void tagtypes();
	void xml();
	void vacuum();
}