package org.vardb.bio;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.io.ParseException;
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.LocationTools;
import org.biojavax.Comment;
import org.biojavax.DocRef;
import org.biojavax.Namespace;
import org.biojavax.Note;
import org.biojavax.RankedDocRef;
import org.biojavax.RichAnnotation;
import org.biojavax.SimpleNamespace;
import org.biojavax.SimpleRankedCrossRef;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichLocation;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.GenbankLocationParser;
import org.biojavax.bio.taxa.NCBITaxon;
import org.vardb.util.CDateHelper;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CMessageWriter;
import org.vardb.util.CStringHelper;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class GenbankParser
{
	public static final Namespace NAMESPACE=new SimpleNamespace("vardb");

	// private constructor to enforce singleton pattern
	private GenbankParser(){}
	
	public static void parseFolder(String folder, List<GenbankSequence> sequences)
	{
		List<String> filenames=CFileHelper.listFilesRecursively(folder,".gbk");
		parseFiles(filenames,sequences);
	}
	
	public static void parseFiles(List<String> filenames, List<GenbankSequence> sequences)
	{
		for (String filename : filenames)
		{
			parseFile(filename,sequences);
		}
	}
	
	public static void parseFile(String filename, List<GenbankSequence> sequences)
	{
		String str=CFileHelper.readFile(filename);
		parse(str,sequences);
	}
	
	public static void parse(String gb, List<GenbankSequence> sequences)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new StringReader(gb));
			RichSequenceIterator iter = RichSequence.IOTools.readGenbankDNA(reader,NAMESPACE);
			RichSequence richsequence=null;
			while(iter.hasNext())
			{
				richsequence=iter.nextRichSequence();
				convert(richsequence,sequences);
			}
		}
		catch (Exception e)
		{
			throw new CException(e);
		}
	}
	
	public static RichSequence read(String gb)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new StringReader(gb));
			RichSequenceIterator iter = RichSequence.IOTools.readGenbankDNA(reader,NAMESPACE);
			if (!iter.hasNext())
				return null; 
			return iter.nextRichSequence();
		}
		catch (Exception e)
		{
			System.err.println("could not parse GenBank file: "+e);
			throw new CException(e);
		}		
	}

	private static void convert(RichSequence richsequence, List<GenbankSequence> sequences)
	{		
		FeatureFilter geneFilter=new FeatureFilter.ByType("gene"); // hack? CDS
		FeatureFilter cdsFilter=new FeatureFilter.ByType("CDS");
		FeatureFilter filter = new FeatureFilter.Or(geneFilter,cdsFilter);
		
		NonOverlappingLocations locations=new NonOverlappingLocations();
		FeatureHolder holder=richsequence.filter(filter);
		for (Iterator<?> i = holder.features();i.hasNext();)
		{
			RichFeature feature = (RichFeature)i.next();
			locations.add(feature.getLocation());
		}
		
		if (locations.getLocations().size()==0)
		{
			GenbankSequence sequence=convert(richsequence);
			sequences.add(sequence);
			return;
		}
		
		for (Location location : locations.getLocations())
		{
			GenbankSequence sequence=convert(richsequence,location);
			sequences.add(sequence);
		}
	}
	
	/*
	// assume one CDS - extract translation
	// get all matpeptides
	private static void convertPolyprotein(RichSequence richsequence, List<GenbankSequence> sequences)
	{
		GenbankSequence template=new GenbankSequence();
		RichFeature cds;
		for (Iterator<?> i = richsequence.features();i.hasNext();)
		{
			RichFeature feature = (RichFeature)i.next();
			String featuretype=feature.getType();
			Map<String,String> annotations=getAnnotations(feature);
			Map<String,String> crossrefs=getCrossrefs(feature);
			if ("source".equals(featuretype))
				setSourceProperties(template,richsequence,feature,annotations);
			else if ("CDS".equals(featuretype))
			{
				cds=feature;
				setCdsProperties(template,richsequence,feature,annotations,crossrefs);
			}
		}
			
		for (Iterator<?> i = richsequence.features();i.hasNext();)
		{
			RichFeature feature = (RichFeature)i.next();
			String featuretype=feature.getType();
			Map<String,String> annotations=getAnnotations(feature);
			Map<String,String> crossrefs=getCrossrefs(feature);
			if ("mat_peptide".equals(featuretype))
			{
				GenbankSequence sequence=new GenbankSequence(template);
				setMatPeptideProperties(sequence,richsequence,feature,annotations,crossrefs,cds);
			}
		}
	}
	
	private static void setMatPeptideProperties(GenbankSequence sequence, RichSequence richsequence, RichFeature richfeature,
			Map<String,String> annotations, Map<String,String> crossrefs,
			RichFeature cds)
	{
		String translation=annotations.get("biojavax:translation");
		int transstart=
		
		//sequence.setProduct(annotations.get("biojavax:product"));
		//sequence.setProtein_id(annotations.get("biojavax:protein_id"));
		//sequence.setUniprot(getUniprot(crossrefs));
		//sequence.setTranslation(annotations.get("biojavax:translation"));
		//sequence.updateAalength();
		sequence.setSplicing(getSplicing(richfeature.getLocation().getMin(),richfeature));
		sequence.setSpliced(extractSubsequence(richsequence,richfeature));
		//sequence.setPseudogene(getPseudogene(annotations));
		sequence.setStrand(getStrand(richfeature));
		sequence.addNote(getNote(annotations));
	}
	*/
	

	// don't filter by location
	public static GenbankSequence convert(RichSequence richsequence)
	{
		GenbankSequence sequence=createGenbankSequence(richsequence);
		sequence.setSequence(richsequence.seqString());
		sequence.setNtlength(sequence.getSequence().length());
		for (Iterator<?> i = richsequence.features();i.hasNext();)
		{
			RichFeature feature = (RichFeature)i.next();
			setProperties(sequence,richsequence,feature);
		}
		return sequence;
	}
	
	public static GenbankSequence convert(RichSequence richsequence, Location location)
	{
		GenbankSequence sequence=createGenbankSequence(richsequence);
		//System.out.println("checking location "+location.getMin()+".."+location.getMax());
		FeatureFilter locationFilter = new FeatureFilter.ShadowOverlapsLocation(location);
		for (Iterator<?> i = richsequence.filter(locationFilter).features();i.hasNext();)
		{
			RichFeature feature = (RichFeature)i.next();
			setProperties(sequence,richsequence,feature);
		}
		return sequence;
	}
	
	public static ListMultimap<String,Location> findCodingRegions(String gpt, CMessageWriter writer)
	{
		try
		{
			writer.message("finding coding regions");
			ListMultimap<String,Location> locations=ArrayListMultimap.create();//Multimaps.newArrayListMultimap();
			BufferedReader reader = new BufferedReader(new StringReader(gpt));			
			RichSequenceIterator iter = RichSequence.IOTools.readGenbankProtein(reader,NAMESPACE);
			while(iter.hasNext())
			{
				RichSequence richsequence = iter.nextRichSequence();
				FeatureFilter filter=new FeatureFilter.HasAnnotation("coded_by");
				for (Iterator<?> i = richsequence.filter(filter).features();i.hasNext();)
				{
					RichFeature feature = (RichFeature)i.next();
					//display(feature);
					Map<String,String> annotations=getAnnotations(feature);
					String coded_by=annotations.get("biojavax:coded_by");
					if (coded_by==null)
						continue;
					writer.message("found coded_by annotation: "+coded_by);
					int index=coded_by.indexOf(':');
					String accession=stripVersion(coded_by);					
					String loc=coded_by.substring(index+1);
					Location location=parseLocation(richsequence,loc);
					locations.put(accession,location);
				}
			}
			return locations;
		}
		catch (Exception e)
		{
			throw new CException(e);
		}
	}
	
	public static ListMultimap<String,Location> getLocationsFromGeneSummaries(String summaries)
	{
		String regex="Annotation: ([a-zA-Z0-9_.]+) \\(([0-9]+)\\.\\.([0-9]+)(, complement)?\\)";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(summaries);
		ListMultimap<String,Location> locations=ArrayListMultimap.create();
		while (matcher.find())
		{
			System.out.println("found regex match: "+matcher.group(0));
			String accession=matcher.group(1);
			int start=Integer.parseInt(matcher.group(2));
			int end=Integer.parseInt(matcher.group(3));
			Location location=createLocation(start, end);
			System.out.println("adding location: "+accession+":"+start+".."+end);
			if (!locations.containsEntry(accession, location))
				locations.put(accession,location);
			else System.out.println("found duplicate location: "+accession+":"+start+".."+end);
		}
		return locations;
	}
	
	// filter GenBank files based on coding regions	
	public static void convertByLocation(String gbk, ListMultimap<String,Location> locations, List<GenbankSequence> sequences, CMessageWriter writer)
	{	
		BufferedReader reader = new BufferedReader(new StringReader(gbk));
		RichSequenceIterator iter = RichSequence.IOTools.readGenbankDNA(reader,NAMESPACE);
		while(iter.hasNext())
		{
			RichSequence richsequence = nextRichSequence(iter);
			if (richsequence==null)
				continue;
			//String accession=richsequence.getAccession();
			String accession=richsequence.getName();
			writer.message("parsing Genbank sequence "+accession);
			convertByLocation(richsequence, locations.get(accession), sequences);
		}
	}
	
	public static void convertByLocation(String gbk, List<Location> locations, List<GenbankSequence> sequences, CMessageWriter writer)
	{	
		BufferedReader reader = new BufferedReader(new StringReader(gbk));
		RichSequenceIterator iter = RichSequence.IOTools.readGenbankDNA(reader,NAMESPACE);
		while(iter.hasNext())
		{
			RichSequence richsequence = nextRichSequence(iter);
			if (richsequence==null)
				continue;
			String accession=richsequence.getName();
			writer.message("parsing Genbank sequence "+accession);
			convertByLocation(richsequence, locations, sequences);
		}
	}
	
	private static RichSequence nextRichSequence(RichSequenceIterator iter)
	{
		try
		{	
			return iter.nextRichSequence();
		}
		catch (Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
	
	public static void convertByLocation(RichSequence richsequence, Collection<Location> locations, List<GenbankSequence> sequences)
	{
		for (Location location : locations)
		{
			GenbankSequence sequence=createGenbankSequence(richsequence);
			sequences.add(sequence);	
			System.out.println("checking location "+location.getMin()+".."+location.getMax());
			//FeatureFilter locationFilter = new FeatureFilter.ShadowOverlapsLocation(location);
			FeatureFilter locationFilter = new FeatureFilter.ContainedByLocation(location);
			for (Iterator<?> i = richsequence.filter(locationFilter).features();i.hasNext();)
			{
				RichFeature feature = (RichFeature)i.next();
				setProperties(sequence,richsequence,feature);
			}
		}
	}
	
	public static Location parseLocation(RichSequence richsequence, String loc)
	{
		try
		{
			return GenbankLocationParser.parseLocation(NAMESPACE,richsequence.getName(),loc);
		}
		catch(ParseException e)
		{
			throw new CException(e);
		}
	}
	
	public static Location createLocation(int start, int end)
	{
		return LocationTools.makeLocation(start,end);
	}
	
	private static void setProperties(GenbankSequence sequence, RichSequence richsequence, RichFeature feature)
	{
		String featuretype=feature.getType();
		Map<String,String> annotations=getAnnotations(feature);
		Map<String,String> crossrefs=getCrossrefs(feature);
		if ("source".equals(featuretype))
			setSourceProperties(sequence,richsequence,feature,annotations);
		else if ("gene".equals(featuretype))
			setGeneProperties(sequence,richsequence,feature,annotations,crossrefs);
		else if ("CDS".equals(featuretype))
			setCdsProperties(sequence,richsequence,feature,annotations,crossrefs);
	}

	private static void setSourceProperties(GenbankSequence sequence, RichSequence richsequence, RichFeature richfeature,
			Map<String,String> annotations)
	{		
		//sequence.setChromosome(CMathHelper.parseInt(annotations.get("biojavax:chromosome")));
		sequence.setLocus(richsequence.getAccession());
		sequence.setStart(richfeature.getLocation().getMin());
		sequence.setEnd(richfeature.getLocation().getMax());
		sequence.setIsolate(annotations.get("biojavax:isolate"));
		sequence.setIsolation_source(annotations.get("biojavax:isolation_source"));
		sequence.setCountry(getCountry(annotations.get("biojavax:country")));
		sequence.setSubregion(getSubregion(annotations.get("biojavax:country")));
		sequence.setCollection_date(annotations.get("biojavax:collection_date"));
		//sequence.setPathogen(annotations.get("biojavax:pathogen"));
		sequence.setMol_type(annotations.get("biojavax:mol_type"));
		sequence.setStrain(annotations.get("biojavax:strain"));
		sequence.setClone(annotations.get("biojavax:clone"));
		sequence.setSegment(annotations.get("biojavax:segment"));
		sequence.setSerotype(annotations.get("biojavax:serotype"));
		sequence.setSerogroup(annotations.get("biojavax:serogroup"));		
		sequence.setSerovar(annotations.get("biojavax:serovar"));
		sequence.setSubtype(getSubtype(annotations));
		sequence.setHost(annotations.get("biojavax:host"));
		sequence.setLab_host(annotations.get("biojavax:lab_host"));		
		sequence.setSpecific_host(annotations.get("biojavax:specific_host"));
		sequence.setPlasmid(annotations.get("biojavax:plasmid"));
		sequence.setCodedby(richsequence.getName());
		sequence.addNote(getNote(annotations));
	}
	
	private static void setGeneProperties(GenbankSequence sequence, RichSequence richsequence, RichFeature richfeature,
			Map<String,String> annotations, Map<String,String> crossrefs)
	{
		sequence.setStart(richfeature.getLocation().getMin());
		sequence.setEnd(richfeature.getLocation().getMax());
		sequence.setLocus_tag(annotations.get("biojavax:locus_tag"));
		sequence.setGene(annotations.get("biojavax:gene"));
		sequence.setSequence(extractSubsequence(richsequence,richfeature));
		sequence.setNtlength(sequence.getSequence().length());
		sequence.setPseudogene(getPseudogene(annotations));
		sequence.setAllele(annotations.get("biojavax:allele"));
		sequence.setGeneid(getIntCrossRef(crossrefs,"GeneID"));		
		sequence.addNote(getNote(annotations));
	}
	
	private static void setCdsProperties(GenbankSequence sequence, RichSequence richsequence, RichFeature richfeature,
			Map<String,String> annotations, Map<String,String> crossrefs)
	{
		sequence.setCodon_start(getIntAnnotation(annotations,"biojavax:codon_start"));
		sequence.setTransl_table(annotations.get("biojavax:transl_table"));
		sequence.setProtein_gi(getIntCrossRef(crossrefs,"GI"));
		sequence.setProduct(annotations.get("biojavax:product"));
		sequence.setProtein_id(annotations.get("biojavax:protein_id"));
		sequence.setUniprot(getUniprot(crossrefs));
		sequence.setTranslation(annotations.get("biojavax:translation"));
		//sequence.updateAalength();
		sequence.setSplicing(getSplicing(richfeature.getLocation().getMin(),richfeature));
		sequence.setSpliced(extractSubsequence(richsequence,richfeature));
		sequence.setPseudogene(getPseudogene(annotations));
		sequence.setStrand(getStrand(richfeature));
		sequence.addNote(getNote(annotations));
		
		// if there are no gene features listed, use the splicing sequence instead
		if (!CStringHelper.hasContent(sequence.getSequence()))
		{
			sequence.setSequence(sequence.getSpliced());
			sequence.setNtlength(sequence.getSequence().length());
			//sequence.setStart(richfeature.getLocation().getMin());
			//sequence.setEnd(richfeature.getLocation().getMax());
		}
	}
	
	private static GenbankSequence createGenbankSequence(RichSequence richsequence)
	{
		//display(richsequence);
		Map<String,String> annotations=getAnnotations(richsequence);
		GenbankSequence sequence=new GenbankSequence();
		sequence.setGi(Integer.parseInt(richsequence.getIdentifier()));
		sequence.setAccession(stripVersion(richsequence.getName()));
		sequence.setDefline(clean(richsequence.getDescription()));
		sequence.setVersion(richsequence.getAccession());
		sequence.setCircular(getCircular(richsequence));
		sequence.setDivision(richsequence.getDivision());
		sequence.setTaxon(getTaxon(richsequence));
		sequence.setUdate(getUdate(annotations));
		//sequence.setKw(getKw(annotations));
		sequence.setComments(getComments(richsequence));
		sequence.setConceptual(getConceptual(sequence.getComments()));
		sequence.setEc(getEc(sequence.getComments()));
		sequence.setStrand(StrandType.forward);
		sequence.setRef(CStringHelper.join(getRefs(richsequence),";"));
		return sequence;
	}
	
	////////////////////////////////////////////////////
	
	private static String stripVersion(String accession)
	{
		int index=accession.indexOf('.');
		if (index==-1)
			return accession;
		return accession.substring(0,index);
	}
	
	public static String clean(String value)
	{
		if (value==null)
			return null;
		value=value.trim();
		if (value.indexOf('\n')==-1)
			return value;
		value=CStringHelper.replace(value,"\n","|");
		return value;
	}
	
	private static String getTaxon(RichSequence richsequence)
	{
		NCBITaxon taxon=richsequence.getTaxon();
		if (taxon==null)
			return null;
		return Integer.toString(taxon.getNCBITaxID());
	}
	
	@SuppressWarnings("unchecked")
	private static String getComments(RichSequence richsequence)
	{
		StringBuilder buffer=new StringBuilder();
		for(Comment comment : (Set<Comment>)richsequence.getComments())
		{
			buffer.append(clean(comment.getComment()));
			buffer.append("|");
		}
		return buffer.toString().trim();
	}
	
	private static Boolean getConceptual(String comments)
	{
		if (comments.indexOf("conceptual translation")!=-1)
			return true;
		return null;
	}
	
	private static Boolean getCircular(RichSequence richsequence)
	{
		if (richsequence.getCircular())
			return true;
		return null;
	}
	
	private static String getEc(String str)
	{
		if (str.indexOf("EC=")==-1) //EC=6.3.1.2: or EC=6.3.1.2.
			return null;
		String regex="EC=(([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.([0-9]+))";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(str);
		boolean result=matcher.find();
		if (!result)
		{
			System.err.println("can't find match: "+str);
			return null;
		}
		String ec=matcher.group(1);
		//System.out.println("ec="+ec);
		return ec;
	}
	
	public static List<Integer> getRefs(RichSequence richsequence)
	{
		List<Integer> refs=new ArrayList<Integer>();
		for (Object obj : richsequence.getRankedDocRefs())
		{
			RankedDocRef rankedref=(RankedDocRef)obj;
			DocRef ref=rankedref.getDocumentReference();
			if (ref.getCrossref()!=null)
				refs.add(Integer.parseInt(ref.getCrossref().getAccession()));
		}
		return refs;
	}

	public static Date getUdate(Map<String,String> annotations)
	{		
		String strdate=annotations.get("biojavax:udat");
		//System.out.println("udate="+strdate);
		if (strdate==null)
			return null;
		return CDateHelper.parse(strdate,"dd-MMM-yyyy"); //26-MAY-2005
	}
	
	public static String getKw(Map<String,String> annotations)
	{		
		return annotations.get("biojavax:kw");
	}
	
	public static String getNote(Map<String,String> annotations)
	{
		String note=annotations.get("biojavax:note");
		if (note==null)
			return "";
		else return clean(note);
	}
	
	public static String getSubtype(Map<String,String> annotations)
	{
		String note=annotations.get("biojavax:note");
		if (note==null)
			return "";
		if (note.indexOf("subtype")==-1)
			return "";
		String regex="subtype[: ]+([a-zA-Z0-9]+)";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(note);
		if (matcher.find())
			return matcher.group(1);
		return "";		
	}
	
	public static String getCountry(String str)
	{
		if (str==null)
			return null;
		int index=str.indexOf(':');
		if (index==-1)
			return str;
		return str.substring(0,index);
	}
	
	public static String getSubregion(String str)
	{
		if (str==null)
			return null;
		int index=str.indexOf(':');
		if (index==-1)
			return "";
		return str.substring(index+1).trim();
	}
	
	public static boolean getPseudogene(Map<String,String> annotations)
	{
		return annotations.containsKey("biojavax:pseudo");
	}		
	
	public static String getUniprot(Map<String,String> crossrefs)
	{
		return getCrossRef(crossrefs,"UniProtKB/Swiss-Prot","UniProtKB/TrEMBL");
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public static String extractSubsequence(RichSequence sequence, RichFeature richfeature)
	{
		return richfeature.getLocation().symbols(sequence).seqString();
	}
	
	@SuppressWarnings("unchecked")
	public static String getSplicing(int offset, RichFeature richfeature)
	{
		Iterator iter=richfeature.getLocation().blockIterator();
		List<String> exons=new ArrayList<String>();
		while (iter.hasNext())
		{
			RichLocation location=(RichLocation)iter.next();
			int start=location.getMin()-offset+1;
			int end=location.getMax()-offset+1;
			exons.add(start+".."+end);
		}
		return CStringHelper.join(exons,",");
	}
	
	public static String getLocation(RichFeature richfeature)
	{
		Iterator<?> iter=richfeature.getLocation().blockIterator();
		List<String> ranges=new ArrayList<String>();
		while (iter.hasNext())
		{
			RichLocation location=(RichLocation)iter.next();
			int start=location.getMin();//+1;
			int end=location.getMax();//+1;
			ranges.add(start+".."+end);
		}
		return CStringHelper.join(ranges,",");
	}
	
	public static Map<String,String> getAnnotations(RichSequence richsequence)
	{
		Map<String,String> annotations=new LinkedHashMap<String,String>();
		Map<?,?> map=richsequence.getAnnotation().asMap();
		for (Map.Entry<?,?> entry : map.entrySet())
		{
			annotations.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return annotations;
	}
	
	public static Map<String,String> getAnnotations(RichFeature richfeature)
	{
		Map<String,String> annotations=new LinkedHashMap<String,String>();
		Map<?,?> map=richfeature.getAnnotation().asMap();
		for (Map.Entry<?,?> entry : map.entrySet())
		{
			annotations.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return annotations;
	}
	
	public static Integer getIntAnnotation(Map<String,String> annotations, String name)
	{
		String value=annotations.get(name);
		if (value==null)
			return null;
		return Integer.parseInt(value);
	}
	
	/////////////////////////////////////////////////////////////////////////
	
	public static Map<String,String> getCrossrefs(RichFeature richfeature)
	{
		Map<String,String> crossrefs=new LinkedHashMap<String,String>();
		for (Object obj : richfeature.getRankedCrossRefs())
		{
			SimpleRankedCrossRef crossref=(SimpleRankedCrossRef)obj;
			crossrefs.put(crossref.getCrossRef().getDbname(),crossref.getCrossRef().getAccession());
		}
		return crossrefs;
	}
	
	public static String getCrossRef(Map<String,String> crossrefs, String name1, String name2)
	{
		String value=getCrossRef(crossrefs,name1);
		if (value!=null)
			return value;
		return getCrossRef(crossrefs,name2);
	}
	
	public static String getCrossRef(Map<String,String> crossrefs, String name)
	{
		if (!crossrefs.containsKey(name))
			return null;
		return crossrefs.get(name);
	}
	
	public static Integer getIntCrossRef(Map<String,String> crossrefs, String name)
	{
		String value=getCrossRef(crossrefs,name);
		if (value==null)
			return null;
		return Integer.parseInt(value);
	}
	
	public static StrandType getStrand(RichFeature richfeature)
	{
		RichLocation location=(RichLocation)richfeature.getLocation();
		//System.out.println("strand="+location.getStrand());
		if (location.getStrand()==RichLocation.Strand.POSITIVE_STRAND)
			return StrandType.forward;
		if (location.getStrand()==RichLocation.Strand.NEGATIVE_STRAND)
			return StrandType.reverse;
		return null;				
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void display(FeatureHolder fh, PrintStream pw, String prefix)
	{
	    for (Iterator<Feature> i = fh.features(); i.hasNext(); )
	    {
	        Feature f=i.next();
			pw.print(prefix);
			pw.print(f.getType());
			pw.print(" at ");
			pw.print(f.getLocation().toString());
			pw.print(f.getParent());
			pw.println();
			display(f, pw, prefix+"   ");
	    }
	}
	
	public static void display(RichFeature feature)
	{
		System.out.println("type="+feature.getType());
		System.out.println("location.min="+feature.getLocation().getMin());
		System.out.println("location.max="+feature.getLocation().getMax());
		System.out.println("location="+feature.getLocation());
		Map<?,?> annotations=feature.getAnnotation().asMap();
		System.out.println("annotations");
		for (Object key : annotations.keySet())
		{
			String name=key.toString();
			Object value=annotations.get(key);
			System.out.println("\t\t: "+name+ ": " + value);
		}
		System.out.println("crossrefs");
		for (Object obj : feature.getRankedCrossRefs())
		{
			SimpleRankedCrossRef crossref=(SimpleRankedCrossRef)obj;
			System.out.println("\t\t: "+crossref.getCrossRef());
			System.out.println("\t\t\t-dbname:"+crossref.getCrossRef().getDbname());
			System.out.println("\t\t\t-accession:"+crossref.getCrossRef().getAccession());
		}
	}
	
	@SuppressWarnings({"unchecked"})
	public static void display(RichSequence richsequence)
	{
		String divider="__________________________________________________";
		System.out.println(divider);
		System.out.println("Accession="+richsequence.getAccession());
		System.out.println("Description="+richsequence.getDescription());
		System.out.println("Identifier="+richsequence.getIdentifier());
		System.out.println("Name="+richsequence.getName());
		System.out.println("Version="+richsequence.getVersion());
		System.out.println("SeqVersion="+richsequence.getSeqVersion());
		System.out.println("URN="+richsequence.getURN());
		System.out.println("Namespace="+richsequence.getNamespace());
		System.out.println("Annotation="+richsequence.getAnnotation());
		//NCBITaxon taxon=richsequence.getTaxon();
		//display(taxon);	
		
		/*
		System.out.println(divider);
		System.out.println("Features");
		//System.out.println(richsequence.toString()+" has "+richsequence.countFeatures()+" features");
		for(Iterator i = richsequence.features(); i.hasNext(); )
		{
			RichFeature richfeature=(RichFeature)i.next();
			System.out.println("___Feature________________________________________");
			display(richfeature);
		}
		*/
		
		System.out.println(divider);
		System.out.println("Annotations");		
		RichAnnotation annotation = (RichAnnotation)richsequence.getAnnotation();
		for (Iterator i = annotation.keys().iterator(); i.hasNext(); )
		{
			Object key=i.next();
			String value=(String)annotation.getProperty(key);
			System.out.println(key+"="+value);
		}
		
		System.out.println(divider);
		System.out.println("Notes");
		Iterator notesIterator = annotation.getNoteSet().iterator();
		while (notesIterator.hasNext())
		{
			Note note = (Note)notesIterator.next();
			// biojavax:udat = submission date
			System.out.println("Note: rank="+note.getRank()+", term="+note.getTerm()+", value="+note.getValue());
			//System.out.println(note);
		}
		
		System.out.println(divider);
		System.out.println("References");
		for (Object ref : richsequence.getRankedDocRefs())
		{
			System.out.println("Ref: "+ref);
		}
		
		System.out.println(divider);
		System.out.println("Sequence");
		//System.out.println(extractSequenceAsFasta(richsequence));
	}
	
	public static void display(NCBITaxon taxon)
	{
		System.out.println("__________________________________________________");
		System.out.println("Taxon");
		System.out.println("Display name="+taxon.getDisplayName());
		System.out.println("Name hierarchy="+taxon.getNameHierarchy());
		System.out.println("NCBITaxID="+taxon.getNCBITaxID());
		System.out.println("NodeRank="+taxon.getNodeRank());
		System.out.println("GeneticCode="+taxon.getGeneticCode());
		System.out.println("LeftValue="+taxon.getLeftValue());
		System.out.println("MitoGeneticCode="+taxon.getMitoGeneticCode());
		System.out.println("NameClasses="+taxon.getNameClasses());
		System.out.println("ParentNCBITaxID="+taxon.getParentNCBITaxID());
		System.out.println("RightValue="+taxon.getRightValue());
	}
	
	public static class NonOverlappingLocations
	{
		protected List<Location> locations=new ArrayList<Location>();
		
		public List<Location> getLocations(){return this.locations;}
		
		public boolean add(Location location)
		{
			Location old=findOverlapping(location);
			if (old==null)
				this.locations.add(location);
			return true;
		}
		
		public Location findOverlapping(Location location)
		{
			for (Location loc : this.locations)
			{
				if (loc.overlaps(location))
					return loc;
			}
			return null;
		}
	}
}