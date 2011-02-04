package org.vardb.setup;

import org.vardb.util.CException;

public enum CAction implements IAction
{
	dropdb("Drop database if exists")
	{
		public void execute(ISetup setup)
		{
			setup.dropdb();
		}
	},
	
	createdb("Create database")
	{
		public void execute(ISetup setup)
		{
			setup.createdb();
		}
	},
	
	sql("load setup SQL commands")
	{
		public void execute(ISetup setup)
		{
			setup.sql();
		}
	},
	
	loadusers("load users from XML user file")
	{
		public void execute(ISetup setup)
		{
			setup.loadusers();
		}
	},

	/*
	tagtypes("define basic tag types and attributes")
	{
		public void execute(ISetup setup)
		{
			setup.tagtypes();
		}
	},
	*/
	
	xml("load XML files in data directory")
	{
		public void execute(ISetup setup)
		{
			setup.xml();
		}
	},
	
	vacuum("run vacuum analyze on the database to refresh query statistics")
	{
		public void execute(ISetup setup)
		{
			setup.vacuum();
		}
	},
	
	help("help")
	{
		public void execute(ISetup setup)
		{
			System.out.println("help action");
		}
	},
	
	sequences("load pipeline-detected sequences")
	{
		public void execute(ISetup setup)
		{
			((CSetup)setup).sequences();
		}
	},
	
	/*
	domains("load Pfam domain information")
	{
		public void execute(CSetup setup)
		{
			setup.getBatchService().loadTablesFromFolder(setup.params.domainDir,setup.loadparams);
		}
	},
	
	othersequences("load non-pipeline sequences")
	{
		public void execute(CSetup setup)
		{
			setup.getBatchService().loadTablesFromFolder(setup.params.othersequenceDir,setup.loadparams);
		}
	},
	
	alignments("protein alignments")
	{
		public void execute(CSetup setup)
		{
			setup.getAdminService().loadAlignmentsFromFolder(setup.params.alignmentDir,setup.writer);
		}
	},
	
	bundles("load clinical data bundles/tags")
	{
		public void execute(CSetup setup)
		{
			String dir=params.bundleDir;
			IBatchService batchService=getBatchService();
			batchService.loadXmlFromFolder(dir,writer);
			batchService.loadBundlesFromFolder(dir,writer);
			batchService.loadJoinsFromFolder(dir,writer);
		}
	},
	
	pfam("update Pfam information")
	{
		public void execute(CSetup setup)
		{
			CPfamLoader.dowloadPfamDomainFiles(setup.params.tempDir);
			JdbcTemplate jdbcTemplate=new JdbcTemplate(CDatabaseHelper.createDataSource(setup.params.db,setup.params.db.getName()));
			CPfamLoader loader=new CPfamLoader(jdbcTemplate);
			loader.loadFiles(setup.params.tempDir);
			setup.getAdminService().updatePfamDomains(writer);
		}
	},
	
	taxonomy("update NCBI taxonomy data")
	{
		public void execute(CSetup setup)
		{
			getResourceService().updateTaxonomy(writer);
		}
	},
	
	refs("update PubMed references")
	{
		public void execute(CSetup setup)
		{
			getResourceService().updateReferences(writer);
		}
	},
	
	pdb("load PDB structures")
	{
		public void execute(CSetup setup)
		{
			getAdminService().loadStructuresFromFolder(params.pdbDir,writer);
		}
	},
	
	drugs("update drug information from KEGG drug")
	{
		public void execute(CSetup setup)
		{
			getResourceService().updateDrugs(writer);
		}
	},
	
	counts("update sequence counts")
	{
		public void execute(CSetup setup)
		{
			getCountService().updateCounts(writer);
		}
	},
	
	blast("update blast database")
	{
		public void execute(CSetup setup)
		{
			getBlastService().updateBlastDatabase(writer);
		}
	},
	*/
	
	test("test action")
	{
		public void execute(ISetup setup)
		{
			System.out.println("void do action: "+this.name());
		}
	};
	
	private String description;
	
	CAction(String description)
	{
		this.description=description;
	}
	
	public String getDescription(){return description;}
	
	public abstract void execute(ISetup setup);
	
	public static CAction find(String value)
	{
		try
		{
			return CAction.valueOf(value.trim());
		}
		catch(IllegalArgumentException e)
		{
			throw new CException("cannot find action type: "+value);
		}
	}
}

/*
public enum CAction
{
	dropdb("Drop database if exists")
	{
		public void execute(CSetup setup)
		{
			if (!CFileHelper.confirm("Delete database "+setup.params.db.getName()+"? (press y to confirm drop or any other key to skip)"))
			{
				System.out.println("Skipping dropdb");
				return;
			}
			CDatabaseHelper.dropDatabase(setup.params.db);
		}
	},
	
	createdb("Create database")
	{
		public void execute(CSetup setup)
		{
			if (CDatabaseHelper.databaseExists(setup.params.db))
			{
				if (!setup.params.dropdbifexists)
				{
					setup.writer.message("database already exists and dropdbifexists is false - quitting");
					return;
				}
				else CAction.dropdb.execute(setup);
			}
			CDatabaseHelper.createDatabase(setup.params.db);
		}
	},
	
	tables("load setup SQL commands")
	{
		public void execute(CSetup setup)
		{
			String sql=CDatabaseHelper.concatenateScripts(setup.params.sqlDir);
			CFileHelper.writeFile(setup.params.tempDir+"setup.sql",sql);
			CDatabaseHelper.executeSql(setup.params.db,sql);
		}
	},
	
	loadusers("load users from XML user file")
	{
		public void execute(CSetup setup)
		{
			if (CStringHelper.hasContent(setup.params.userfile) && CFileHelper.exists(setup.params.userfile))
				setup.getAdminService().loadXmlFromFile(setup.params.userfile,setup.writer);
		}
	},
	
	importusers("import users from another database")
	{
		public void execute(CSetup setup)
		{
			if (!CStringHelper.hasContent(setup.params.importdb))
				return;
			if (!CFileHelper.confirm("Import users from database "+setup.params.importdb+"? (press y to confirm drop or any other key to skip)"))
			{
				System.out.println("Skipping import user step");
				return;
			}
			DataSource datasource=CDatabaseHelper.createDataSource(setup.params.db,setup.params.importdb);
			CDatabaseImporter importer=new CDatabaseImporter(datasource,setup.writer);
			setup.getUserService().addUsers(importer.getUsers());
		}
	},
	
	tagtypes("define basic tag types and attributes")
	{
		public void execute(CSetup setup)
		{
			List<String> filenames=CFileHelper.listFilesRecursively(setup.params.xmlDir+"tagtypes/", ".txt");
			ITagService tagService=setup.getTagService();
			for (String filename : filenames)
			{
				tagService.createTagTypeFromAttributeFile(filename);
			}
			tagService.getDao().updateAttributeCounts();
		}
	},
	
	xml("load XML files in data directory")
	{
		public void execute(CSetup setup)
		{
			setup.getAdminService().validateFolder(setup.params.xmlDir, setup.writer);
			setup.getAdminService().loadXmlFromFolder(setup.params.xmlDir,setup.writer);
		}
	},
	
	sequences("load pipeline-detected sequences")
	{
		public void execute(CSetup setup)
		{
			String folder=setup.params.sequenceDir;
			setup.writer.message("loading tables from folder: "+folder);
			List<String> filenames=CFileHelper.listFilesRecursively(folder,CConstants.TABLE_SUFFIX,setup.loadparams.getDate());
			IAdminService adminService=setup.getAdminService();
			for (String filename : filenames)
			{
				adminService.loadSequenceTableFromFile(filename,setup.loadparams);
			}
			setup.writer.message("Clearing cache");
			setup.writer.message("Finished loading tables from folder: "+folder);		
		}
	},
	
	domains("load Pfam domain information")
	{
		public void execute(CSetup setup)
		{
			setup.getBatchService().loadTablesFromFolder(setup.params.domainDir,setup.loadparams);
		}
	},
	
	othersequences("load non-pipeline sequences")
	{
		public void execute(CSetup setup)
		{
			setup.getBatchService().loadTablesFromFolder(setup.params.othersequenceDir,setup.loadparams);
		}
	},
	
	alignments("protein alignments")
	{
		public void execute(CSetup setup)
		{
			setup.getAdminService().loadAlignmentsFromFolder(setup.params.alignmentDir,setup.writer);
		}
	},
	
	bundles("load clinical data bundles/tags")
	{
		public void execute(CSetup setup)
		{
			String dir=params.bundleDir;
			IBatchService batchService=getBatchService();
			batchService.loadXmlFromFolder(dir,writer);
			batchService.loadBundlesFromFolder(dir,writer);
			batchService.loadJoinsFromFolder(dir,writer);
		}
	},
	
	pfam("update Pfam information")
	{
		public void execute(CSetup setup)
		{
			CPfamLoader.dowloadPfamDomainFiles(setup.params.tempDir);
			JdbcTemplate jdbcTemplate=new JdbcTemplate(CDatabaseHelper.createDataSource(setup.params.db,setup.params.db.getName()));
			CPfamLoader loader=new CPfamLoader(jdbcTemplate);
			loader.loadFiles(setup.params.tempDir);
			setup.getAdminService().updatePfamDomains(writer);
		}
	},
	
	taxonomy("update NCBI taxonomy data")
	{
		public void execute(CSetup setup)
		{
			getResourceService().updateTaxonomy(writer);
		}
	},
	
	refs("update PubMed references")
	{
		public void execute(CSetup setup)
		{
			getResourceService().updateReferences(writer);
		}
	},
	
	pdb("load PDB structures")
	{
		public void execute(CSetup setup)
		{
			getAdminService().loadStructuresFromFolder(params.pdbDir,writer);
		}
	},
	
	drugs("update drug information from KEGG drug")
	{
		public void execute(CSetup setup)
		{
			getResourceService().updateDrugs(writer);
		}
	},
	
	counts("update sequence counts")
	{
		public void execute(CSetup setup)
		{
			getCountService().updateCounts(writer);
		}
	},
	
	blast("update blast database")
	{
		public void execute(CSetup setup)
		{
			getBlastService().updateBlastDatabase(writer);
		}
	},
	
	vacuum("run vacuum analyze on the database to refresh query statistics")
	{
		public void execute(CSetup setup)
		{
			CDatabaseHelper.vacuum(setup.params.db);
		}
	},
	
	test("test action")
	{
		public void execute(CSetup setup)
		{
			System.out.println("void do action: "+this.name());
		}
	};
	
	private String description;
	
	CAction(String description)
	{
		this.description=description;
	}
	
	public String getDescription(){return description;}
	
	public abstract void execute(CSetup setup);
	
	public static CAction find(String value)
	{
		try
		{
			return CAction.valueOf(value.trim());
		}
		catch(IllegalArgumentException e)
		{
			throw new CException("cannot find action type: "+value);
		}
	}
}
*/

/*
//drugs("update drug information from KEGG drug"),	
//domains("load Pfam domain information"),
//othersequences("load non-pipeline sequences"),
//alignments("protein alignments"),
//bundles("load clinical data bundles/tags"),
//pfam("update Pfam information"),
//taxonomy("update NCBI taxonomy data"),
//refs("update PubMed references"),
//pdb("load PDB structures"),
//counts("update sequence counts"),		
//blast("update blast database"),
//vacuum("run vacuum analyze on the database to refresh query statistics"),
//help("show usage and action types");
*/
