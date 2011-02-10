package org.vardb.bio;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequence.IOTools;
import org.vardb.util.CException;
import org.vardb.util.CStringHelper;

public class SequenceFileParser
{	
	private static final String NAMESPACE="vardb";
	//private static final String TOKENIZATION="token";
	
	private SequenceFileParser(){}
	
	public static List<ISequence> readFasta(String temp)
	{
		return readFasta(temp,SequenceType.AA);
	}
	
	public static List<ISequence> readFasta(String str, SequenceType type)
	{
		try
		{
			str=str.trim();
			if (CStringHelper.isEmpty(str))
				return new ArrayList<ISequence>();
			BufferedReader reader=new BufferedReader(new StringReader(str));
			return readFasta(reader,type);
		}
		catch (Exception e)
		{
			throw new CException(e);
		}		
	}
	
	public static List<ISequence> readFasta(BufferedReader reader, SequenceType type)
	{
		try
		{
			SequenceIterator iter=getFastaIterator(reader,type);
			return convertToList(iter,type);
		}
		catch (Exception e)
		{
			throw new CException(e);
		}		
	}
	
	public static SequenceIterator getFastaIterator(BufferedReader reader, SequenceType type)
	{
		if (type==SequenceType.NT)
			return IOTools.readFastaDNA(reader,getNamespace());
		else return IOTools.readFastaProtein(reader,getNamespace());		
	}
	
	///////////////////////////////////////////////////////
	
	public static Map<String,String> readAlignment(String str, SequenceType type)
	{
		if (!CStringHelper.hasContent(str))
			throw new CException("alignment is empty: "+CStringHelper.truncateEllipsis(str,100));
		if (str.startsWith("CLUSTAL"))
			return readClustalWAlignment(str,type);
		else if (str.charAt(0)=='>')
			return readFastaAlignment(str,type);
		else throw new CException("can't determine format type for alignment: "+CStringHelper.truncateEllipsis(str,100));
	}
	
	public static Map<String,String> readClustalWAlignment(String str, SequenceType type)
	{
		ClustalMultipleAlignmentParser parser=new ClustalMultipleAlignmentParser();
		return parser.parse(str);
	}

	
	public static Map<String,String> readFastaAlignment(String str, SequenceType type)
	{
		str=str.trim();
		if (!CStringHelper.hasContent(str))
			throw new CException("alignment is empty: ["+str+"]");	
		//if (str.charAt(0)!='>')
		//	throw new CException("expecteding first character to be a >");
		try
		{
			
			BufferedReader reader=new BufferedReader(new StringReader(str));
			//SymbolTokenization tokenization=getTokenization(type);
			//SequenceIterator iter=IOTools.readFasta(reader,tokenization,getNamespace());
			SequenceIterator iter=getFastaIterator(reader,type);
			return convertToMap(iter);
		}
		catch (Exception e)
		{
			throw new CException(e);
		}		
	}
	
	////////////////////////////////////////////////////////
	
	public static String writeSequences(SequenceHelper.SequenceFileType filetype, Map<String,String> sequences)
	{
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		writeSequences(filetype,out,sequences);
		return out.toString();
	}
	
	public static void writeSequences(SequenceHelper.SequenceFileType filetype, OutputStream out, Map<String,String> sequences)
	{
		switch(filetype)
		{
		case FASTA:
			writeFasta(out,sequences);
			break;
		case CLUSTALW:
			writeClustalW(out,sequences);
			break;
		case PIR:
			writePir(out,sequences);
			break;
		default:
			throw new CException("No writer for file type: "+filetype);
		}
	}
	
	public static void writeFasta(Map<String,String> sequences, String filename)
	{
		try
		{
			if (sequences.isEmpty())
			{
				System.out.println("no sequences in map: skipping output for "+filename);
				return;
			}
			FileOutputStream stream=new FileOutputStream(filename);
			writeFasta(stream,sequences);
		}
		catch(IOException e)
		{
			throw new CException(e);
		}
	}
	
	public static String writeFasta(Map<String,String> sequences)
	{
		if (sequences.isEmpty())
			return "";
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		writeFasta(out,sequences);
		return out.toString();
	}	
	
	public static String writeFasta(List<ISequence> sequences)
	{
		if (sequences.isEmpty())
			return "";
		Map<String,String> map=new LinkedHashMap<String,String>();			
		for (ISequence sequence : sequences)
		{
			if (sequence.getTranslation()!=null)
				map.put(sequence.getAccession(),sequence.getTranslation());
		}
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		writeFasta(out,map);
		return out.toString();
	}	
	
	public static void writeFasta(OutputStream out, Map<String,String> sequences)
	{
		try
		{
			String newline="\n";
			for (Map.Entry<String,String> entry : sequences.entrySet())
			{
				String name=entry.getKey();
				String sequence=entry.getValue();
				if (name==null || sequence==null)
					continue;
				String defline=">"+name+newline;
				out.write(defline.getBytes());
				out.write(CStringHelper.chunk(sequence,80,newline).getBytes());
				out.write(newline.getBytes());
			}
		}
		/*
		catch(SocketException e)
		{
			System.err.println(e);
		}
		*/
		catch(Exception e)
		{
			System.err.println(e);
			//throw new CException(e);
		}
	}
	
	public static void writeFasta(RichSequence richsequence, String filename)
	{
		try
		{
			FileOutputStream stream=new FileOutputStream(filename);
			RichSequence.IOTools.writeFasta(stream,richsequence,getNamespace());
		}
		catch(IOException e)
		{
			throw new CException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////
	
	public static void writePir(OutputStream out, Map<String,String> sequences)
	{
		try
		{
			String newline="\n";
			for (Map.Entry<String,String> entry : sequences.entrySet())
			{
				String name=entry.getKey();
				String sequence=entry.getValue();//sequences.get(name);
				if (name==null || sequence==null)
					continue;
				String line1=">P1;"+name+newline;
				String line2="sequence"+":"+name+":::::::0.00: 0.00"+newline;
				out.write(line1.getBytes());
				out.write(line2.getBytes());
				out.write(CStringHelper.chunk(sequence,80,newline).getBytes());
				out.write("*".getBytes());
				out.write(newline.getBytes());
			}
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////
	
	public static void writeClustalW(OutputStream out, Map<String,String> sequences)
	{
		try
		{
			int maxAccessionWidth=findLongestAccession(sequences);
			int sequenceLength=SequenceHelper.findMaximumLength(sequences);
			int chunksize=60;
			int numparts=(int)Math.ceil((float)sequenceLength/(float)chunksize);
			int spacer=6;
			int col1width=maxAccessionWidth+spacer;
			String newline="\n";
			out.write("CLUSTAL W (1.83) multiple sequence alignment".getBytes());
			out.write(newline.getBytes());
			out.write(newline.getBytes());
			out.write(newline.getBytes());
			for (int index=0;index<numparts;index++)
			{
				for (Map.Entry<String,String> entry : sequences.entrySet())
				{
					String name=entry.getKey();
					String sequence=entry.getValue();
					if (name==null || sequence==null)
						continue;
					out.write(CStringHelper.padRight(name,' ',col1width).getBytes());
					out.write(extractSubsequence(sequence,index*chunksize,chunksize,sequenceLength).getBytes());
					out.write(newline.getBytes());
				}
				out.write(newline.getBytes()); //skip a line (or two?) between parts
			}
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	
	private static String extractSubsequence(String sequence, int start, int chunksize, int maxlength)
	{
		if (start>=sequence.length())
		{
			System.out.println("ClustalW formatter: start position ("+start+") is out of bounds for sequence: "+sequence);
			return CStringHelper.repeatString(SequenceHelper.GAP,maxlength-start);
		}
		int end=start+chunksize;
		if (end>=sequence.length())
			return sequence.substring(start);
		else return sequence.substring(start,end);
	}
	
	private static int findLongestAccession(Map<String,String> sequences)
	{
		int max=0;
		for (String accession : sequences.keySet())
		{
			int length=accession.length();
			if (length>max)
				max=length;
		}
		return max;
	}
	
	/////////////////////////////////////////////////////
	
	private static List<ISequence> convertToList(SequenceIterator iter, SequenceType type)
	{
		try
		{
			List<ISequence> sequences=new ArrayList<ISequence>();
			while (iter.hasNext())
			{
				Sequence seq = iter.nextSequence();
				//System.out.println(seq.getName() + ": " + seq.seqString());
				ISequence sequence=new SimpleSequence(seq.getName());//,seq.seqString());
				if (type==SequenceType.AA)
					sequence.setTranslation(seq.seqString());
				else if (type==SequenceType.NT)
					sequence.setSequence(seq.seqString());
				sequences.add(sequence);
			}
			return sequences;
		}
		catch (Exception e)
		{
			throw new CException(e);
		}		
	}
	
	private static Map<String,String> convertToMap(SequenceIterator iter)
	{
		try
		{
			Map<String,String> sequences=new LinkedHashMap<String,String>();
			while (iter.hasNext())
			{
				Sequence seq = iter.nextSequence();
				String accession=seq.getName().trim();
				String sequence=seq.seqString().trim();
				//System.out.println(accession);//+": "+sequence);
				if (CStringHelper.hasContent(accession) && CStringHelper.hasContent(sequence))
					sequences.put(accession,sequence);
			}
			return sequences;
		}
		catch (Exception e)
		{
			throw new CException(e);
		}		
	}
	
	public static SimpleNamespace getNamespace()
	{
		return new SimpleNamespace(NAMESPACE);
	}
	
	///////////////////////////////////////////
	
	// assume FASTA without the header line
	public static String readSimpleSequence(String sequence)
	{
		//System.out.println("before: "+sequence);
		StringBuilder buffer=new StringBuilder();
		for (int index=0;index<sequence.length();index++)
		{
			char ch=sequence.charAt(index);
			if (Character.isLetter(ch) || ch=='-')
				buffer.append(ch);
		}
		//System.out.println("after: "+buffer.toString());
		return buffer.toString();
	}
	
	////////////////////////////////////////////////////////////////////////
	
	/*
	public static void parseDefline(String defline, ISequence sequence)
	{
		defline=defline.trim();
		sequence.setDescription(defline);
		//sequence.setDefline(defline);
		//System.out.println("parsing name="+str);
		if (defline.indexOf("gi|")==0)
		{
			parseGenbankDefline(defline,sequence);
		}
		else if (defline.contains(";"))
		{
			List<String> parts=CStringHelper.splitAsList(defline,";");
			String name=parts.get(0).trim();
			sequence.setAccession(name);
		}
		else if (defline.contains("|"))
		{
			String name=defline.substring(0,defline.indexOf('|')).trim();
			sequence.setAccession(name);
		}
		else
		{
			sequence.setAccession(defline);
		}
	}
	
	protected static void parseGenbankDefline(String defline, ISequence sequence)
	{
		CGenbankDefline entry=new CGenbankDefline(defline);
		sequence.setAccession(entry.getAccession());
		sequence.setGi(entry.getGi());
		sequence.setVersion(entry.getVersion());
		sequence.setDefline(defline);
	}
	*/
	
	public static String getName(String defline)
	{
		defline=defline.trim();
		//System.out.println("parsing name="+str);
		if (defline.startsWith("gi|"))
		{
			CGenbankDefline entry=new CGenbankDefline(defline);
			return entry.getAccession();
		}
		else if (defline.contains(";"))
		{
			List<String> parts=CStringHelper.splitAsList(defline,";");
			return parts.get(0).trim();
		}
		else if (defline.contains("|"))
		{
			return defline.substring(0,defline.indexOf('|')).trim();
		}
		else return defline;
	}
	
	public static String clean(String str)
	{
		str=str.trim();
		StringBuilder buffer=new StringBuilder();
		for (int index=0;index<str.length();index++)
		{
			char ch=str.charAt(index);
			if (Character.isLetter(ch) || ch=='-')
				buffer.append(ch);
		}
		return buffer.toString();
	}
	
	public static class CGenbankDefline
	{
		protected static final String REGEX="gi\\|([0-9]+)\\|([^|]*)\\|([^|]*)(\\|([^|]*))?";
		protected static final Pattern pattern=Pattern.compile(REGEX);
		
		protected String defline;
		protected Integer gi;
		protected String accession;
		protected String version;
		protected String source;
		protected String description;
		
		public CGenbankDefline(String defline)
		{
			parse(defline);
		}
		
		public String getDefline(){return this.defline;}
		public Integer getGi(){return this.gi;}
		public String getAccession(){return this.accession;}
		public String getVersion(){return this.version;}
		public String getSource(){return this.source;}
		public String getDescription(){return this.description;}
		
		protected void parse(String defline)
		{
			Matcher matcher=pattern.matcher(defline);
			if (!matcher.find())
				throw new CException("can't parse defline: "+defline);
			this.gi=Integer.parseInt(matcher.group(1));
			this.source=matcher.group(2);
			this.version=matcher.group(3);
			this.accession=this.version;
			if (this.accession.indexOf('.')!=-1)
				this.accession=this.accession.substring(0,this.accession.indexOf('.'));		
			if (matcher.groupCount()>3)
			{
				this.description=matcher.group(4);
				//System.out.println("description="+this.description);
			}
			if (this.description==null)
				this.description="";
			//System.out.println(defline+" --> "+toString());
		}

		@Override
		public String toString()
		{
			return "gi="+this.gi+" source="+this.source+" version="+this.version+" description="+this.description;
		}
	}
	
	
	/*
	public static String convert(String str, String format, String args[])
	{
		try
		{
			int outid=BioseqFormats.formatFromName(format);
			BioseqWriterIface seqwriter=BioseqFormats.newWriter(outid);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			seqwriter.setOutput(out);
			seqwriter.writeHeader();
			Readseq rd=new Readseq();
			for (int i=0;i<args.length;i++)
			{
				rd.setInputObject(args[i]);
				if (rd.isKnownFormat() && rd.readInit())
					rd.readTo(seqwriter);
			}
			seqwriter.writeTrailer();
			return out.toString();
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}

	public static List<ISequence> read(String str)
	{
		try
		{
			Object inputObject= new FileReader("myseq.embl");
			Readseq rd=new Readseq();
			String seqname= rd.setInputObject( inputObject );
			// Readseq.setInputObject() accepts many basic Java objects including 
			// Reader, File, URL, InputStream, String, char[], byte[], 
			// and an Enumeration of these objects
			System.err.println("Reading from "+seqname);
			if (rd.isKnownFormat() && rd.readInit())
			{
				while (rd.readNext())
				{
					BioseqRecord seqrec=new BioseqRecord(rd.nextSeq());
					//do something with seqrec....
					FeatureItem[] fits= seqrec.findFeatures( new String[]{"CDS", "intron"});
					if (fits==null)
						System.out.println("  No such features found.");
					else
					{
						System.out.println("  Extracted features and their sequence");
						for (int k= 0; k<fits.length; k++)
						{
							try
							{
								System.out.println(fits[k]);
								Bioseq bseq= seqrec.extractFeatureBases(fits[k]);
								System.out.println(bseq); System.out.println();
							}
							catch (SeqRangeException sre)
							{
								System.out.println(sre.getMessage());
							}
						}
					}
				}
			}
			return null;
	    }
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	*/
}