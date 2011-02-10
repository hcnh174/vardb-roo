package org.vardb.bio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.bio.symbol.SymbolListViews;
import org.biojava.bio.symbol.TranslationTable;
import org.vardb.util.CBeanHelper;
import org.vardb.util.CException;
import org.vardb.util.CStringHelper;

public class SequenceHelper
{
	public static final String NEWLINE=System.getProperty("line.separator");
	public static final int DEFAULT_CHUNKSIZE=80;
	public static final String GAP="-";
	public static final String FASTA_SUFFIX=".fasta";
	public static final String GENBANK_SUFFIX=".gbk";
	public static final String GENPEPT_SUFFIX=".gpt";
	public static final String UDATE_PATTERN="d'-'MM'-'yy";
	
	public enum FastaType
	{
		AA,
		NT,
		CDS
	}
	
	public enum SequenceFileType
	{
		FASTA(".fasta"),
		CLUSTALW(".aln"),
		PIR(".pir");
		
		private final String extension;
		
		SequenceFileType(String extension)
		{
			this.extension=extension;
		}
		
		public String getExtension(){return this.extension;}
	}
	
	private SequenceHelper(){}
	
	public static ISequence copy(ISequence sequence)
	{
		try
		{
			ISequence copy=sequence.getClass().newInstance();
			CBeanHelper beanhelper=new CBeanHelper();
			beanhelper.copyProperties(copy,sequence);
			return sequence;
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	
	//find numcolumns
	public static int getNumcolumns(Collection<String> sequences)
	{
		return sequences.iterator().next().length();
	}
	
	//find numcolumns
	public static int getNumcolumns(Map<String,String> sequences)
	{
		if (sequences.isEmpty())
			throw new CException("alignment is empty");
		return sequences.values().iterator().next().length();
	}
		
	public static int findMaximumLength(Collection<String> sequences)
	{
		if (sequences.isEmpty())
			return 0;
		int max=0;
		for (String sequence : sequences)
		{
			if (sequence==null)
				continue;
			int length=sequence.length();
			if (length>max)
				max=length;
		}	
		return max;	
	}
	
	public static int findMaximumLength(Map<String,String> map)
	{
		if (map.isEmpty())
			return 0;
		int max=0;
		for (String sequence : map.values())
		{
			if (sequence==null)
				continue;
			int length=sequence.length();
			if (length>max)
				max=length;
		}	
		return max;	
	}
	
	public static boolean areAligned(Map<String,String> map)
	{
		Integer length=null;
		for (String sequence : map.values())
		{
			if (length==null)
				length=sequence.length();
			else if (length!=sequence.length())
				return false;
		}	
		return true;	
	}
	
	public static void pad(List<String> sequences, int length)
	{
		for (int index=0;index<sequences.size();index++)
		{
			String sequence=sequences.get(index);
			if (sequence==null)
				continue;
			sequences.set(index,pad(sequence,length));
		}
	}
	
	public static String pad(String sequence, int length)
	{
		if (sequence==null)
			return null; 
		if (sequence.length()==length)
			return sequence;
		return sequence+CStringHelper.repeatString(SequenceHelper.GAP,length-sequence.length());
	}
	
	/*
	//removes positions with only gaps 
	public static List<Integer> removeEmptyColumns(Map<String,String> sequences)
	{
		Consensus consensus=new Consensus(sequences);
		List<Integer> columns=new ArrayList<Integer>();
		for (CConsensus.Position position : consensus.getPositions())
		{
			if (position.isEmpty())
				columns.add(position.getNumber()-1);
		}
		
		System.out.println("empty columns="+CStringHelper.join(columns,","));
		//for (String accession : sequences.keySet())
		for (Map.Entry<String,String> entry : sequences.entrySet())
		{
			String accession=entry.getKey();
			String sequence=entry.getValue();
			StringBuilder buffer=new StringBuilder();
			for (int index=0;index<sequence.length();index++)
			{
				String aa=sequence.substring(index,index+1);
				boolean skip=false;
				for (int column : columns)
				{
					if (column==index)
						skip=true;
				}
				if (!skip)
					buffer.append(aa);
				//else //System.out.println("skipping column "+(index));
			}
			sequences.put(accession,buffer.toString());
		}
		List<Integer> gaps=new ArrayList<Integer>();
		int column=0;
		for (int index=0;index<columns.size();index++) // positions: 15, 40, 45, 70
		{
			int next_column=columns.get(index);
			int num_ungapped=next_column-column;
			for (int i=0;i<num_ungapped;i++)
			{
				gaps.add(0);
			}
			gaps.add(1);
		}
		System.out.println("gap profile="+CStringHelper.join(gaps,","));
		return gaps;
	}
	*/
	
	public static List<Map<String,String>> subset(Map<String,String> alignment, SimpleLocation location)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for (SimpleLocation.SubLocation loc : location.getSublocations())
		{
			list.add(subset(alignment,loc.getStart(),loc.getEnd()));
		}
		return list;
	}
	
	// assumes all sequences are the same length
	public static Map<String,String> subset(Map<String,String> alignment, int start, int end)
	{
		Map<String,String> subset=new LinkedHashMap<String,String>();
		boolean first=true;
		for (Map.Entry<String,String> entry : alignment.entrySet())
		{
			String subsequence=entry.getValue().substring(start-1,end);
			if (first)
			{
				System.out.println("subsequence length="+subsequence.length()+" ("+subsequence.length()%3+"): "+subsequence);
				first=false;
			}
			subset.put(entry.getKey(),subsequence);
		}
		return subset;
	}
	
	public static List<Map<String,String>> split(Map<String,String> sequences, SimpleLocation location)
	{
		int length=sequences.values().iterator().next().length();
		List<Map<String,String>> subalignments=new ArrayList<Map<String,String>>();
		List<Integer> columns=new ArrayList<Integer>();
		for (SimpleLocation.SubLocation sublocation : location.getSublocations())
		{
			int start=sublocation.getStart();
			int end=sublocation.getEnd();
			columns.add(start);
			if (end>=length)
				break;
			columns.add(end+1);
		}
		System.out.println("location="+location);
		System.out.println("columns="+columns);
		
		int lastcolumn=0;
		for (int column : columns)
		{
			Map<String,String> subalignment=new LinkedHashMap<String,String>();
			subalignments.add(subalignment);
			//for (String accession : sequences.keySet())
			//{
			//	String sequence=sequences.get(accession);
			for (Map.Entry<String,String> entry : sequences.entrySet())
			{
				String accession=entry.getKey();
				String sequence=entry.getValue();
				if (column>=sequence.length())
					sequence=sequence.substring(lastcolumn);
				else sequence=sequence.substring(lastcolumn,column);
				subalignment.put(accession,sequence);
			}
			lastcolumn=column;
		}
		// add the rest
		Map<String,String> subalignment=new LinkedHashMap<String,String>();
		subalignments.add(subalignment);
		for (Map.Entry<String,String> entry : sequences.entrySet())
		{
			String accession=entry.getKey();
			String sequence=entry.getValue().substring(lastcolumn);
			subalignment.put(accession,sequence);
		}
		return subalignments;
	}
	
	public static Map<String,String> join(List<Map<String,String>> subalignments, String delimiter)
	{
		if (subalignments.isEmpty())
			throw new CException("subalignments list is empty");
		if (subalignments.size()==1)
			throw new CException("only one subalignment");
		Map<String,String> joined=new LinkedHashMap<String,String>();		
		for (Map<String,String> subalignment : subalignments)
		{
			for (Map.Entry<String,String> entry : subalignment.entrySet())
			{
				String accession=entry.getKey();
				if (!joined.containsKey(accession))
					joined.put(accession,"@@@");
				String sequence=joined.get(accession);
				joined.put(accession,sequence+delimiter+subalignment.get(accession));
			}
		}
		return joined;
	}

	public static String removeGaps(String sequence)
	{
		return CStringHelper.replace(sequence,SequenceHelper.GAP,"");
	}
	
	/*
	public static Map<String,String> getMap(List<ISequence> seqs, CSequenceType type)
	{
		Map<String,String> map=new LinkedHashMap<String,String>();
		for (ISequence seq : seqs)
		{
			map.put(seq.getName(),seq.getSequence(type));
		}
		return map;
	}
		
	public static Map<String,Integer> mapIdsToIdentifiers(List<ISequence> sequences)
	{
		Map<String,Integer> ids=new HashMap<String,Integer>();
		for (ISequence sequence : sequences)
		{
			ids.put(sequence.getIdentifier(),sequence.getId());
		}
		return ids;
	}
	
	public static boolean isEmpty(ISequence sequence)
	{
		if (sequence==null || sequence.getAligned()==null || sequence.getSequence().length()==0)
			return true;
		return false;
	}
	*/	
	
	public static String getUnaligned(String sequence)
	{
		return CStringHelper.replace(sequence,SequenceHelper.GAP,"");
	}
	
	/*
	public static String getFasta(Collection<ISequence> sequences)
	{
		StringBuilder buffer=new StringBuilder();
		for (ISequence sequence : sequences)
		{
			sequence.getFasta().getSequence(buffer);
		}
		return buffer.toString();
	}
	*/
	
	public static String getFastChunkedSequences(Collection<? extends ISequence> sequences)
	{
		StringBuilder buffer=new StringBuilder();
		for (ISequence seq : sequences)
		{
			String sequence=seq.getSequence();
			if (!CStringHelper.hasContent(sequence))
				continue;
			SequenceHelper.getFastaChunked(seq.getAccession(),sequence,buffer);
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public static String getFastChunkedTranslations(Collection<? extends ISequence> sequences)
	{
		StringBuilder buffer=new StringBuilder();
		for (ISequence seq : sequences)
		{
			String sequence=seq.getTranslation();
			if (!CStringHelper.hasContent(sequence))
				continue;				
			SequenceHelper.getFastaChunked(seq.getAccession(),sequence,buffer);
			buffer.append("\n");
		}
		return buffer.toString();
	}

	public static String getFastaChunked(String name, String sequence)
	{
		return getFastaChunked(name,sequence,DEFAULT_CHUNKSIZE);
	}
	
	public static String getFastaChunked(String name, String sequence, int chunksize)
	{
		StringBuilder buffer=new StringBuilder();
		getFastaChunked(name,sequence,buffer,chunksize);
		return buffer.toString();
	}
	
	public static void getFastaChunked(String name, String sequence, StringBuilder buffer)
	{
		getFastaChunked(name,sequence,buffer,DEFAULT_CHUNKSIZE);
	}
	
	public static void getFastaChunked(String name, String sequence, StringBuilder buffer, int chunksize)
	{
		buffer.append(">");
		buffer.append(name);
		buffer.append(NEWLINE);
		CStringHelper.chunk(sequence,chunksize,NEWLINE,buffer);
	}
	
	public static String getFastaColumns(String name, String sequence)
	{
		StringBuilder buffer=new StringBuilder();
		getFastaChunked(name,sequence,buffer);
		return buffer.toString();
	}
	
	public static void getFastaColumns(String name, String sequence, StringBuilder buffer)
	{
		buffer.append(">");
		buffer.append(name);
		buffer.append(NEWLINE);
		getColumns(sequence,buffer);
	}

	public static String getColumns(String sequence)
	{
		StringBuilder buffer=new StringBuilder();
		getColumns(sequence,buffer);
		return buffer.toString();
	}
	
	public static void getColumns(String sequence, StringBuilder buffer)
	{
		int chunksize=60;
		List<String> lines=CStringHelper.chunk(sequence,chunksize);
		for (String line : lines)
		{
			List<String> columns=CStringHelper.chunk(line,10);
			buffer.append(CStringHelper.join(columns," "));
			buffer.append(NEWLINE);
		}
	}
	
	/*
	public static String getFasta(CTable table, String column)
	{
		Integer col=table.findColumn(column);
		if (col==null)
			throw new CException("can't find column: "+column);
		Integer pseudogenecol=table.findColumn("pseudogene");
		StringBuilder buffer=new StringBuilder();
		for (CTable.Row row : table.getRows())
		{
			String accession=row.getValue(0);
			String sequence=row.getValue(col);
			if (pseudogenecol!=null && row.getValue(pseudogenecol).equalsIgnoreCase("TRUE"))
				continue;
			if (!CStringHelper.hasContent(sequence))
				continue;
			getFastaChunked(accession, sequence, buffer);
			buffer.append(NEWLINE);
		}
		return buffer.toString();
	}
	*/
	
	/*
	public static List<Integer> getIds(List<ISequence> sequences)
	{
		//System.out.println("getting sequence ids");
		List<Integer> idlist=new ArrayList<Integer>(); 
		for (ISequence sequence : sequences)
		{
			idlist.add(sequence.getId());
		}
		return idlist;
	}
	
	public static List<String> getSubAlignments(List<ISequence> sequences)
	{
		Map<Integer,StringBuilder> buffers=new HashMap<Integer,StringBuilder>();
		for (ISequence seq : sequences)
		{
			CSequence sequence=(CSequence)seq;
			if (!CStringHelper.hasContent(sequence.getTranslation()))
				continue;
			Integer alignment_id=sequence.getAlignment_id();
			if (alignment_id==null)
				alignment_id=0;	
			String aligned=sequence.getAligned();
			if (!CStringHelper.hasContent(aligned))
				aligned=sequence.getTranslation();
			StringBuilder buffer=buffers.get(alignment_id);
			if (buffer==null)
			{
				buffer=new StringBuilder();
				buffers.put(alignment_id,buffer);
			}
			buffer.append(">"+sequence.getAccession()+NEWLINE+aligned+NEWLINE);
		}
		
		List<String> alignments=new ArrayList<String>();
		for (StringBuilder buffer : buffers.values())
		{
			alignments.add(buffer.toString());
		}
		return alignments;
	}
	*/
	
	////////////////////////////////////////////////////
	
	public static List<String> getExons(String sequence, String splicing,
			Integer start, StrandType strand)
	{
		if (!CStringHelper.hasContent(sequence) || start==null || strand==null || !CStringHelper.hasContent(splicing))
			return null;
		if (strand==StrandType.reverse)
			sequence=CStringHelper.reverse(sequence);
		
		SimpleLocation location=new SimpleLocation(splicing);
		List<String> exons=new ArrayList<String>();
		for (SimpleLocation.SubLocation sublocation : location.getSublocations())
		{
			String subseq=sublocation.extract(sequence,start);
			exons.add(subseq);
		}
		if (strand==StrandType.reverse)
		{
			for (int index=0;index<exons.size();index++)
			{
				String exon=exons.get(index);
				exons.set(index,CStringHelper.reverse(exon));
			}
			Collections.reverse(exons);
		}
		exons.set(exons.size()-1,removeStopCodon(exons.get(exons.size()-1)));
		return exons;
	}
	
	public static List<String> getIntrons(String sequence, String splicing,
			Integer start, StrandType strand)
	{
		if (!CStringHelper.hasContent(sequence) || start==null || strand==null || !CStringHelper.hasContent(splicing))
			return null;
		if (strand==StrandType.reverse)
			sequence=CStringHelper.reverse(sequence);
		
		SimpleLocation location=new SimpleLocation(splicing);
		location=location.invert();
		List<String> introns=new ArrayList<String>();
		for (SimpleLocation.SubLocation sublocation : location.getSublocations())
		{
			String subseq=sublocation.extract(sequence,start);
			introns.add(subseq);
		}
		if (strand==StrandType.reverse)
		{
			for (int index=0;index<introns.size();index++)
			{
				String intron=introns.get(index);
				introns.set(index,CStringHelper.reverse(intron));
			}
			Collections.reverse(introns);
		}
		return introns;
	}
	
	// introns should follow splicing pattern: 5' GU 3' AG
	public static String getCodingSequence(String accession, String sequence, String splicing,
			Integer start, StrandType strand, Integer codon_start)
	{
		if (!CStringHelper.hasContent(sequence))
			return null;
		String spliced=sequence;
		if (start!=null && strand!=null && CStringHelper.hasContent(splicing))
		{
			SimpleLocation location=new SimpleLocation(splicing);
			spliced=location.extract(sequence,start,strand);
		}
		spliced=removeStopCodon(spliced);
		if (codon_start!=null)
			spliced=spliced.substring(codon_start-1);
		if (spliced.length()%3!=0)
			throw new CException("sequence "+accession+" does not have a length that is a mulitple of 3: "+spliced.length());
		return spliced;
	}
	
	public static String getCodingSequence(String accession, String spliced, String sequence)
	{
		if (!CStringHelper.hasContent(spliced))
			spliced=sequence;
		if (!CStringHelper.hasContent(spliced))
			return null;
		spliced=removeStopCodon(spliced);
		if (spliced.length()%3!=0)
		{
			//System.out.println("sequence "+accession+" does not have a length that is a multiple of 3: "+spliced.length());
			//throw new CException("sequence "+accession+" does not have a length that is a mulitple of 3: "+spliced.length());
			//return null;
			spliced=spliced.substring(0,spliced.length()-spliced.length()%3);
		}
		return spliced;
	}
	
	public static String removeStopCodon(String sequence)
	{
		//if (sequence.length()<3)
		//	throw new CException("can't remove stop codon: "+sequence);
		if (Codon.isStopCodon(sequence.substring(sequence.length()-3)))
			return sequence.substring(0,sequence.length()-3);
		else return sequence;
	}
	
	/*
	public static boolean isStopCodon(String codon)
	{
		boolean stopcodon=(codon.equals("TGA") || codon.equals("TAA") || codon.equals("TAG"));
		//System.out.println("lastcodon="+codon+" isStopCodon="+stopcodon);
		return stopcodon;
	}
	*/
	
	private static final String GAP_ENCODING_SEPARATOR=",";
	private static final String GAP_ENCODING_COUNT_TOKEN="+";
	
	// converts an aligned sequence into an encoded form, start position+number of gaps 
	public static String encodeAlignedSequence(String aligned)
	{
		String regex="-+|[A-Z]+";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(aligned);
		List<String> parts=new ArrayList<String>();
		int position=0;
		while (matcher.find())
		{
			String match=matcher.group();
			if (match.indexOf('-')!=-1)
				parts.add(position+GAP_ENCODING_COUNT_TOKEN+match.length());
			else position+=match.length();
		}
		if (aligned.charAt(aligned.length()-1)!='-') // if the sequence does not end with a gap, add the remaining positions
		{
			String ungapped=removeGaps(aligned);
			parts.add((ungapped.length()-1)+GAP_ENCODING_COUNT_TOKEN+"0");
		}
		return CStringHelper.join(parts,GAP_ENCODING_SEPARATOR);
	}
	
	// -----MCDIISA------------------------------------------------------IDNFAN------------DPKNQGGHNSIGYSKYYCP------DNN-----CDTDVKKIISTFIFLVTLFNGIDHNEKLESD--------------------------------K--IAEYAILWLSYKLNQK------TQNGT--TKLYDFYTE--HINTNSKYNE------------HITN-G-----------FKINKSVIENKIK------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	// 0+5,7+54,13+12,32+6,35+5,66+32,67+2,83+6,88+2,97+2,107+12,111+1,112+11,125+1050";
	public static String unencodeAlignedSequence(String sequence, String encoded)
	{
		List<String> parts=CStringHelper.splitAsList(encoded,GAP_ENCODING_SEPARATOR);
		StringBuilder buffer=new StringBuilder();
		int lastposition=0;
		for (String part : parts)
		{
			int position=Integer.parseInt(part.substring(0,part.indexOf(GAP_ENCODING_COUNT_TOKEN)));
			int numgaps=Integer.parseInt(part.substring(part.indexOf(GAP_ENCODING_COUNT_TOKEN)+1));
			//System.out.println("part="+part+",lastpostion="+lastposition+",position="+position+", numgaps="+numgaps);
			buffer.append(sequence.substring(lastposition,position));
			buffer.append(CStringHelper.repeatString(SequenceHelper.GAP,numgaps));
			lastposition=position;
		}
		return buffer.toString();
	}
	
	// expects nucleotide sequence - applies encoding to codons instead of amino acids
	// 0+5,7+54,13+12,32+6,35+5,66+32,67+2,83+6,88+2,97+2,107+12,111+1,112+11,125+1050";
	public static String unencodeCodonAlignedSequence(String sequence, String encoded)
	{
		if (sequence==null)
		{
			System.out.println("sequence is null. cannot unencode codon aligned sequence");
			return "";
		}
		if (sequence.length()%3!=0)
		{
			System.out.println("sequence length is not a multiple of 3. cannot unencode. length="+sequence.length());
			return "";
		}
		List<String> parts=CStringHelper.splitAsList(encoded,GAP_ENCODING_SEPARATOR);
		StringBuilder buffer=new StringBuilder();
		int lastposition=0;
		for (String part : parts)
		{
			int position=Integer.parseInt(part.substring(0,part.indexOf(GAP_ENCODING_COUNT_TOKEN)));
			int numgaps=Integer.parseInt(part.substring(part.indexOf(GAP_ENCODING_COUNT_TOKEN)+1));
			//System.out.println("part="+part+",position="+position+", numgaps="+numgaps);
			buffer.append(sequence.substring(lastposition*3,position*3));
			buffer.append(CStringHelper.repeatString(SequenceHelper.GAP,numgaps*3));
			lastposition=position;
		}
		return buffer.toString();
	}
	
	public static void splitByConservation(Map<String,String> alignment, SimpleLocation location, List<Map<String,String>> conserved, List<Map<String,String>> variable)
	{
		for (SimpleLocation.SubLocation loc : location.getSublocations())
		{
			conserved.add(SequenceHelper.subset(alignment,loc.getStart(),loc.getEnd()));
		}
		
		Integer start=null;
		Integer end=null;
		boolean first=true;
		for (SimpleLocation.SubLocation loc : location.getSublocations())
		{
			System.out.println("location="+loc.getStart()+".."+loc.getEnd());
			if (first)
				first=false;
			else
			{
				start=end+1;
				end=loc.getStart()-1;
				System.out.println("adding interval location: "+start+".."+end);
				variable.add(SequenceHelper.subset(alignment,start,end));
			}
			end=loc.getEnd();
		}
	}

	public static String translate(String dna)
	{
		try
		{
			// if it doesn't divide evenly by three, truncate it
			//dna=truncateCodons(dna);			
			//create a DNA SymbolList
			SymbolList sequence = DNATools.createDNA(dna);	 
			//transcribe to RNA (after biojava 1.4 use this method instead)
			sequence = DNATools.toRNA(sequence);
			//translate to protein
			sequence = RNATools.translate(sequence);			 
			//prove that it worked
			//System.out.println(symL.seqString());
			return sequence.seqString();
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	
	public static String translate(String dna, int transl_table)
	{
		try
		{
			TranslationTable table = RNATools.getGeneticCode(transl_table);
			SymbolList sequence = DNATools.createDNA(dna);
			sequence=DNATools.transcribeToRNA(sequence);//DNATools.toRNA(sequence);
			//view the RNA sequence as codons, this is done internally by RNATool.translate()
			sequence = SymbolListViews.windowedSymbolList(sequence, 3);
		    //translate
			SymbolList protein = SymbolListViews.translate(sequence, table);
			return protein.seqString();
		}
		catch(Exception e)
		{
			throw new CException(e);
		}
	}
	
	public static String truncateCodons(String str)
	{
		int remainder=str.length() % 3;
		if (remainder==0)
			return str;
		return str.substring(0,str.length()-remainder);
	}
	
	/////////////////////////////////////////////////
	
	public static void verifyAlignableSequences(Map<String,String> sequences)
	{
		verifyNumSequences(sequences.size());
		verifySequenceLength(sequences.values().iterator().next());
	}
	
	/*
	// check to make sure there are enough sequences to align and that they are all of the minimum length
	public static void verifyAlignableSequences(List<ISequence> sequences)
	{
		int count=0;
		for (ISequence sequence : sequences)
		{ 
			String translation=sequence.getTranslation();
			if (translation==null)
				continue;
			count++;
			verifySequenceLength(translation);
		}
		verifyNumSequences(count);		
	}
	*/
	
	public static void verifyNumSequences(int num)
	{
		int MIN_SEQUENCES=2;
		if (num<MIN_SEQUENCES)
			throw new CException("Need "+MIN_SEQUENCES+" or more sequences to align.");
	}
	
	public static void verifySequenceLength(String sequence)
	{
		int MIN_ALIGNMENT_LENGTH=3;
		if (sequence.length()<MIN_ALIGNMENT_LENGTH)
			throw new CException("Sequences must be at least "+MIN_ALIGNMENT_LENGTH+" residues to align");
	}
	
	public static String parseArchitecture(String value)
	{
		CompoundLocation domains=new CompoundLocation(value);
		List<String> names=domains.getArchitecture();
		return CStringHelper.join(names,";");
	}
	
	public static class Address
	{
		private static final String DOTDOT="..";
		private static final String DOT=".";
		protected String locus;
		protected Integer start;
		protected Integer end;
		
		public Address(String identifier)
		{
			int index=identifier.indexOf(DOTDOT);
			if (index==-1)
				throw new CException("cannot find start..stop pattern in identifier "+identifier);
			this.end=Integer.parseInt(identifier.substring(index+DOTDOT.length()));
			String substr=identifier.substring(0,index);
			index=substr.lastIndexOf(DOT);
			if (index==-1)
				throw new CException("cannot find accession.start delimiter in identifier "+identifier);
			this.locus=substr.substring(0,index);
			this.start=Integer.parseInt(substr.substring(index+DOT.length()));
			//System.out.println("locus="+this.locus+", start="+this.start+", end="+this.end);
		}
		
		public String getLocus(){return this.locus;}
		public void setLocus(final String locus){this.locus=locus;}

		public Integer getStart(){return this.start;}
		public void setStart(final Integer start){this.start=start;}

		public Integer getEnd(){return this.end;}
		public void setEnd(final Integer end){this.end=end;}
		
		public static List<String> removeLocations(Collection<String> list)
		{
			List<String> accessions=new ArrayList<String>();
			for (String accession : list)
			{
				accession=removeLocation(accession);
				if (!accessions.contains(accession))
					accessions.add(accession);
			}
			return accessions;
		}
		
		public static String removeLocation(String identifier)
		{
			int index=identifier.indexOf(DOTDOT);
			if (index==-1)
				return identifier;
			String substr=identifier.substring(0,index);
			index=substr.lastIndexOf(DOT);
			if (index==-1)
				return substr;
			else return substr.substring(0,index);
		}
		
		public static boolean hasLocation(String identifier)
		{
			return (identifier.indexOf(DOTDOT)!=-1);
		}
	}
}