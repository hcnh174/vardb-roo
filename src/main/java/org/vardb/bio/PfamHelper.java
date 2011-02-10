package org.vardb.bio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;
import org.vardb.util.HttpHelper;

import au.com.bytecode.opencsv.CSVReader;

public class PfamHelper
{
	public static final String PFAM_SERVER="ftp://ftp.sanger.ac.uk";
	public static final String PFAM_FOLDER="/pub/databases/Pfam/releases/Pfam24.0/database_files/";
	
	private final JdbcTemplate jdbcTemplate;
	
	public PfamHelper(JdbcTemplate jdbcTemplate)
	{		
		this.jdbcTemplate=jdbcTemplate;
	}
	
	public void loadFiles(String dir)
	{
		deleteAll();
		loadFile(dir,"pfamA");
		loadFile(dir,"clans");
		loadFile(dir,"clan_membership");
	}

	private void deleteAll()
	{
		StringBuilder buffer=new StringBuilder();
		buffer.append("truncate table temp_clan_membership cascade;\n");
		buffer.append("truncate table temp_clans cascade;\n");
		buffer.append("truncate table temp_pfamA cascade;\n");
		jdbcTemplate.execute(buffer.toString());
	}
	
	private void loadFile(String dir, String type)
	{
		try
		{
			String filename=dir+type+".txt";
			System.out.println("loading file: "+filename);
			StringBuilder buffer=new StringBuilder();
			buffer.append("insert into temp_"+type+"\n");
			buffer.append("values");
			Reader filereader=new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			CSVReader reader = new CSVReader(filereader,'\t','\'');
		    String[] fields;
		    boolean first=true;
		    while ((fields = reader.readNext()) != null)
		    {
		    	if (first)
		    		first=false;
		    	else buffer.append(",\n");
		    	buffer.append("(");
		    	buffer.append(join(fields));
		    	buffer.append(")");
		    }
		    buffer.append(";");
		    //CFileHelper.writeFile("c:/temp/"+type+"-insert.sql", buffer.toString());
		    jdbcTemplate.execute(buffer.toString());
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	
	private static String join(String[] fields)
	{
		return CStringHelper.join(wrap(fields),",");
	}
	
	private static List<String> wrap(String[] fields)
	{
		List<String> values=new ArrayList<String>();
		for (String field : fields)
		{
			if (field.equals("N"))
				values.add("NULL");
			else values.add("'"+CStringHelper.escapeSql(field)+"'");
		}
		return values;
	}
	
	public static void dowloadPfamDomainFiles(String dir)
	{
		List<String> required=new ArrayList<String>();
		required.add("clans.txt");
		required.add("pfamA.txt");
		required.add("clan_membership.txt");
		
		List<String> filenames=new ArrayList<String>();
		for (String filename : required)
		{
			if (!CFileHelper.exists(dir+filename))
				filenames.add(filename+".gz");
		}
		HttpHelper.getFtpFiles(PFAM_SERVER, PFAM_FOLDER, filenames, dir);
		CFileHelper.unGzipFiles(dir,filenames);
	}
}