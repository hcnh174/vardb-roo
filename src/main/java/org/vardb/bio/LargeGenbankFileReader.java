package org.vardb.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biojava.bio.symbol.Location;
import org.biojavax.bio.seq.RichSequence;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;
import org.vardb.util.CTable;

// use grep instead? grep -n -f patterns.txt core.gb
public class LargeGenbankFileReader
{
	public static final String FASTA_SUFFIX=".fasta";
	public static final String START_TAG="LOCUS       ";
	public static final String END_TAG="//";
	public static final String NEWLINE="\n";
	public static final String NOTFOUND_FILENAME="d:/temp/notfound.txt";
	public static final String MULTIPLE_FILENAME="d:/temp/multiple.txt";
	
	protected String genbankcache;
	protected Map<String,RichSequence> cache=new HashMap<String,RichSequence>();
	
	public LargeGenbankFileReader(String genbankcache)
	{
		this.genbankcache=genbankcache;
	}
	
	public static void clearLogfiles()
	{
		CFileHelper.writeFile(NOTFOUND_FILENAME,"");
		CFileHelper.writeFile(MULTIPLE_FILENAME,"");
	}	
	
	//public List<GenbankSequence> getSequences(CTable table)
	public List<GenbankSequence> getSequences(Collection<String> identifiers)
	{	
		List<GenbankSequence> sequences=new ArrayList<GenbankSequence>();
		//for (CTable.Row row : table.getRows())
		for (String identifier : identifiers)
		{
			//String identifier=row.getValue(0);
			try
			{
				GenbankSequence sequence=getSequence(identifier);//,table,row);
				if (sequence==null)
					continue;
				//addAnnotations(sequence,table,row);
				sequences.add(sequence);
			}
			catch(Exception e)
			{
				System.out.println("error parsing GenBank entry for accession "+SequenceHelper.Address.removeLocation(identifier)+":"+e.getMessage());
				e.printStackTrace();
				continue;
				//throw new CException(e);
				//break;
			}
		}
		return sequences;
	}

	private GenbankSequence getSequence(String identifier)
	{
		String accession=SequenceHelper.Address.removeLocation(identifier);
		String filename=this.genbankcache+accession+GenbankService.GENBANK_SUFFIX;
		if (!CFileHelper.exists(filename))
			return notfound(identifier);
		String genbank=CFileHelper.readFile(filename);
		if (genbank==null)
			return notfound(identifier);
		if (SequenceHelper.Address.hasLocation(identifier))
		{
			RichSequence richsequence=getRichSequence(accession,genbank);
			GenbankSequence sequence=GenbankParser.convert(richsequence,getLocation(identifier));
			if (sequence==null)
				return notfound(identifier);
			sequence.setAccession(identifier);
			sequence.setIdentifier(identifier);
			return sequence;
		}
		else
		{
			//System.out.println("parsing genbank entry for accession "+accession);
			List<GenbankSequence> sequences=new ArrayList<GenbankSequence>();
			GenbankParser.parse(genbank,sequences);
			if (sequences.isEmpty())
				return notfound(identifier);
			else if (sequences.size()>1)
				throw new CException("multiple genbank sequences found: "+identifier);//return multiple(identifier,sequences);
			return sequences.get(0);
		}		
	}
	
	private RichSequence getRichSequence(String accession, String genbank)
	{
		RichSequence richsequence=this.cache.get(accession);
		if (richsequence==null)
		{
			//System.out.println("parsing and caching richsequence for "+accession);
			richsequence=GenbankParser.read(genbank);
			this.cache.put(accession,richsequence);
		}
		//else System.out.println("using cached richsequence for "+accession);
		return richsequence;
	}
	
	public Location getLocation(String identifier)
	{
		SequenceHelper.Address address=new SequenceHelper.Address(identifier);
		return GenbankParser.createLocation(address.getStart(),address.getEnd());
	}
	
	/*
	public Location getLocation(String identifier)
	{
		int index=identifier.indexOf(CConstants.LOCATION_IDENTIFIER_DELIMITER);
		String location=identifier.substring(index+1);
		index=location.indexOf("..");
		int start=Integer.parseInt(location.substring(0,index));
		int end=Integer.parseInt(location.substring(index+2));
		return GenbankParser.createLocation(start, end);
	}
	*/
	/*
	private GenbankSequence multiple(String identifier, List<GenbankSequence> sequences)
	{
		System.out.println("found "+sequences.size()+" entries with accession: "+identifier);
		List<CSequenceProperties.Property> properties=new ArrayList<CSequenceProperties.Property>();
		properties.add(CSequenceProperties.Property.accession);
		properties.add(CSequenceProperties.Property.division);
		properties.add(CSequenceProperties.Property.taxon);
		properties.add(CSequenceProperties.Property.isolate);
		properties.add(CSequenceProperties.Property.mol_type);
		properties.add(CSequenceProperties.Property.locus);
		properties.add(CSequenceProperties.Property.gene);
		properties.add(CSequenceProperties.Property.locus_tag);
		properties.add(CSequenceProperties.Property.pseudogene);
		properties.add(CSequenceProperties.Property.start);
		properties.add(CSequenceProperties.Property.end);
		properties.add(CSequenceProperties.Property.strand);
		properties.add(CSequenceProperties.Property.splicing);
		properties.add(CSequenceProperties.Property.product);
		properties.add(CSequenceProperties.Property.aalength);
		properties.add(CSequenceProperties.Property.protein_id);
		properties.add(CSequenceProperties.Property.uniprot);
		properties.add(CSequenceProperties.Property.defline);
		properties.add(CSequenceProperties.Property.notes);
    	CFileHelper.appendFile(MULTIPLE_FILENAME,GenbankSequence.createTable(sequences,properties,false).toString());
    	return null;
	}
	*/
	
	private GenbankSequence notfound(String identifier)
	{
		System.out.println("could not find sequence with identifier: "+identifier);
    	CFileHelper.appendFile(NOTFOUND_FILENAME,identifier);
    	return null;
	}
	
	/*
	private String extractEntry(RandomAccessFile reader, String accession, long pointer)
		throws IOException
	{
		//System.out.println("trying to find entry ["+accession+"] of type "+type+" at pointer "+pointer);
		reader.seek(pointer);
        String line;
        StringBuilder buffer=new StringBuilder();
        List<GenbankSequence> sequences=new ArrayList<GenbankSequence>();
        while ((line = reader.readLine()) != null)
	    {
        	buffer.append(line).append(NEWLINE);
	    	if (line.indexOf(END_TAG)==0)
	    	{
	    		String genbank=buffer.toString();
	    		return genbank;
	    	}
	    }
        return null;
	}
	*/
	
	/*
	private void addAnnotations(GenbankSequence sequence, CTable table, CTable.Row row)
	{
		sequence.setFamily(row.getValue(table.findColumn("family")));
		sequence.setSource(row.getValue(table.findColumn("source")));
		sequence.setNumexons(CMathHelper.parseInt(row.getValue(table.findColumn("numexons"))));
		sequence.setMethod(row.getValue(table.findColumn("method")));
		sequence.setModel(row.getValue(table.findColumn("model")));
		sequence.setScore(CMathHelper.parseDouble(row.getValue(table.findColumn("score"))));
		sequence.setEvalue(CMathHelper.parseDouble(row.getValue(table.findColumn("evalue"))));
		sequence.setHmmloc(row.getValue(table.findColumn("hmmloc")));
		if (CStringHelper.hasContent(sequence.getSequence()))
			sequence.setGc(CCodonHelper.getGcContent(sequence.getSequence()));
	}
	*/
	
	public static String getLocus(String line)
	{
		int start=START_TAG.length();
		int end=line.indexOf(' ',start);
		String accession=line.substring(start,end).trim();
		//System.out.println("accession=["+accession+"]");
		return accession;
	}
	
	public static void splitFile(String filename, String destination)
	{
		RandomAccessFile reader=null;
		try
		{
            reader=new RandomAccessFile(new File(filename),"r");
            StringBuilder buffer=new StringBuilder();
            //String line;
            String locus=null;
		    //while ((line = reader.readLine()) != null)
            for (String line=reader.readLine(); line!=null; line=reader.readLine())
		    {
		    	if (line.indexOf(START_TAG)==0)
		    	{
		    		locus=getLocus(line);
		    		buffer=new StringBuilder();
		    		buffer.append(line).append(NEWLINE);
		    	}
		    	else if (line.indexOf(END_TAG)!=-1)
		    	{
		    		buffer.append(line).append(NEWLINE);
		    		CFileHelper.writeFile(destination+locus+".gbk",buffer.toString());
		    	}
		    	else buffer.append(line).append(NEWLINE);
		    }
        }
		catch (IOException e)
		{
           throw new CException(e);
        }
		finally
		{
			CFileHelper.closeReader(reader);
		}
	}
	
	public static void splitString(String str, String destination)
	{
		BufferedReader reader=null;
		try
		{
            reader=new BufferedReader(new StringReader(str));
            StringBuilder buffer=new StringBuilder();
            //String line;
            String locus=null;
		    //while ((line = reader.readLine()) != null)
            for (String line=reader.readLine(); line!=null; line=reader.readLine())
		    {
		    	if (line.indexOf(START_TAG)==0)
		    	{
		    		locus=getLocus(line);
		    		buffer=new StringBuilder();
		    		buffer.append(line).append(NEWLINE);
		    	}
		    	else if (line.indexOf(END_TAG)!=-1)
		    	{
		    		buffer.append(line).append(NEWLINE);
		    		CFileHelper.writeFile(destination+locus+".gbk",buffer.toString());
		    	}
		    	else buffer.append(line).append(NEWLINE);
		    }
        }
		catch (IOException e)
		{
           throw new CException(e);
        }
		finally
		{
			CFileHelper.closeReader(reader);
		}
	}
	
	public static void removeRows(String filename, String idfile)
	{
		List<String> ids=CStringHelper.splitAsList(CFileHelper.readFile(idfile),"\n"); //,true BREAK?
		RandomAccessFile reader=null;
		String newfilename=filename+".new";
		CFileHelper.writeFile(newfilename,"");
		try
		{
            reader=new RandomAccessFile(new File(filename),"r");
            //String line;
		    //while ((line = reader.readLine()) != null)
            for (String line=reader.readLine(); line!=null; line=reader.readLine())
		    {
		    	int index=line.indexOf('\t');
		    	if (index==-1)
		    		break;
		    	String identifier=line.substring(0,index);
		    	if (!ids.contains(identifier))
		    		CFileHelper.appendFile(newfilename,line);
		    	else System.out.println("skipping entry from remove list: "+identifier);
		    }
        }
		catch (IOException e)
		{
           throw new CException(e);
        }
		finally
		{
			CFileHelper.closeReader(reader);
		}
	}
	
	// test.largegenbankfilereader
	public static void main(String[] args)
	{
		String table_filename="d:/projects/vardb/data/diego/sequences/var/var-p.falciparum-core.txt";
		CTable diego_table=CTable.parseFile(table_filename);
		//String filename="d:/temp/load/plasmodium.falciparum.var.gbk";
		//String filename="d:/projects/vardb.etc/diego/genbank/plasmodium.falciparum/core.gb";
		//Index index=new Index(filename);
		String genbankcache="d:/temp/genbankcache/";
		LargeGenbankFileReader reader=new LargeGenbankFileReader(genbankcache);
		List<GenbankSequence> sequences=reader.getSequences(diego_table.getIdentifiers());
		//CTable table=GenbankSequence.createTable(sequences,CSequenceProperties.getExtractedProperties());
		//if (!table.isEmpty())
		//	CFileHelper.writeFile("d:/temp/var-p.falciparum-core.txt",table.toString());
	}
}
