package org.vardb.bio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vardb.util.AbstractCommandLineWrapper;
import org.vardb.util.CCommandLine;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;

public class GBlocksWrapper extends AbstractCommandLineWrapper
{	
	public GBlocksWrapper(String dir, String tempDir)
	{
		super(dir,tempDir);
	}
	
	public GblocksAnalysis gblocks(Map<String,String> sequences, Params params)
	{
		String infile=createTempfile("gblocks",".fasta",SequenceFileParser.writeFasta(sequences));
		String outfile=addTempfile(infile+"-gb"+params.getResultsType().getExtension()); //.txts");
		CCommandLine commands=params.getCommand(this.dir,infile,sequences.size());
		commands.setExitValue(1);
		CCommandLine.Output output=exec(commands);
		check(output);
		String html=CFileHelper.readFile(outfile);

		List<Flank> flanks=parse(html);
		SimpleLocation location=convert(flanks);		
		//CTable table=SimpleLocation.applyToUngappedSequences(sequences,location,"gblocks");
		
		GblocksAnalysis analysis=new GblocksAnalysis(sequences,params);
		analysis.getResults().setHtml(html);
		analysis.getResults().setLocation(location.toString());
		//analysis.getResults().setApplied(table.toString());
		
		deleteTempfiles();
		if (location.isEmpty())
			throw new CException("Gblocks: could not find any conserved blocks with the given parameters");
		return analysis;
	}

	private void check(CCommandLine.Output output)
	{
		String target="Execution terminated";
		if (output.getOut().indexOf(target)!=-1)
			throw new CException("Gblocks: "+output.getOut());
	}
	
	private static List<Flank> parse(String str)
	{
		for (String line : CStringHelper.split(str,"\n"))
		{
			if (line.indexOf("Flanks: ")!=-1 && line.indexOf('[')!=-1)
				return parseFlanks(line);
		}
		return new ArrayList<Flank>();
	}
	
	private static List<Flank> parseFlanks(String line)
	{
		//System.out.println("trying to parse flank line="+line);
		List<Flank> flanks=new ArrayList<Flank>();
		line=line.substring(line.indexOf('[')+1);
		line=line.substring(0,line.lastIndexOf(']'));
		//System.out.println("cleaned line: "+line);
		for (String pairs: CStringHelper.split(line,"\\]  \\["))
		{
			//System.out.println("split by ]  [:"+pairs);
			List<String> pair=CStringHelper.splitAsList(pairs,"  ");
			flanks.add(new Flank(Integer.parseInt(pair.get(0)),Integer.parseInt(pair.get(1))));
		}
		return flanks;
	}	
	
	private static SimpleLocation convert(List<Flank> flanks)
	{
		//System.out.println("converting location with "+flanks.size()+" blocks");
		SimpleLocation location=new SimpleLocation();
		for (Flank flank : flanks)
		{
			location.add(flank.getStart(),flank.getEnd());
		}
		return location;
	}
	
	public static class Flank
	{
		protected int start;
		protected int end;
		
		public int getStart(){return this.start;}
		public void setStart(final int start){this.start=start;}

		public int getEnd(){return this.end;}
		public void setEnd(final int end){this.end=end;}
		
		public Flank(int start, int end)
		{
			//System.out.println("creating flank: start="+start+", end="+end);
			this.start=start;
			this.end=end;
		}
	}
	
	@SuppressWarnings("serial")
	public static class Params implements Serializable
	{
		protected SequenceTypeEnum type=SequenceTypeEnum.PROTEIN;
		protected GapPositions gaps=GapPositions.NONE;
		protected int minBlock=10; //b4
		protected boolean similarityMatrices=true; //b6
		protected float minConserved=0.5f; //b1
		protected float minFlanking=0.85f; //b2
		protected int maxNonconserved=8; //b3
		protected ResultsFileType resultsType=ResultsFileType.YES; // p
		
		public SequenceTypeEnum getType(){return this.type;}
		public void setType(final SequenceTypeEnum type){this.type=type;}

		public GapPositions getGaps(){return this.gaps;}
		public void setGaps(final GapPositions gaps){this.gaps=gaps;}
		
		public int getMinBlock(){return this.minBlock;}
		public void setMinBlock(final int minBlock){this.minBlock=minBlock;}

		public boolean getSimilarityMatrices(){return this.similarityMatrices;}
		public void setSimilarityMatrices(final boolean similarityMatrices){this.similarityMatrices=similarityMatrices;}

		public float getMinConserved(){return this.minConserved;}
		public void setMinConserved(final float minConserved){this.minConserved=minConserved;}

		public float getMinFlanking(){return this.minFlanking;}
		public void setMinFlanking(final float minFlanking){this.minFlanking=minFlanking;}

		public int getMaxNonconserved(){return this.maxNonconserved;}
		public void setMaxNonconserved(final int maxNonconserved){this.maxNonconserved=maxNonconserved;}
		
		public ResultsFileType getResultsType(){return this.resultsType;}
		public void setResultsType(final ResultsFileType resultsType){this.resultsType=resultsType;}
		
		public CCommandLine getCommand(String dir, String infile, int numsequences)
		{		
			CCommandLine commands=new CCommandLine(dir+"Gblocks");
			commands.addArg(infile);
			commands.addArg("-t="+this.type);
			commands.addArg("-b1="+format(numsequences,this.minConserved));
			commands.addArg("-b2="+format(numsequences,this.minFlanking));
			commands.addArg("-b3="+this.maxNonconserved);
			commands.addArg("-b4="+this.minBlock);
			commands.addArg("-b5="+this.gaps);
			commands.addArg("-b6="+format(this.similarityMatrices));
			commands.addArg("-s=n");
			commands.addArg("-p="+this.resultsType.getValue());
			return commands;
		}
		
		private String format(int numsequences, float proportion)
		{
			return String.valueOf(Math.round(1.0f+(float)numsequences*proportion));
		}
		
		private String format(boolean value)
		{
			return value ? "y" : "n";
		}
	}
	
	public enum ResultsFileType
	{
		YES("y",".htm"),
		TEXT("t",".txt"),
		SHORT_TEXT("s",".txts"),
		NO("n","");
		
		private String value;
		private String extension;
		
		ResultsFileType(String value, String extension)
		{
			this.value=value;
			this.extension=extension;
		}
		
		public String getValue(){return this.value;}
		public String getExtension(){return this.extension;}
	}
	
	public enum SequenceTypeEnum
	{
		PROTEIN("p",SequenceType.AA),
		DNA("d",SequenceType.NT),
		CODONS("c",SequenceType.NT);
		
		private String value;
		private SequenceType type;
		
		SequenceTypeEnum(String value, SequenceType type)
		{
			this.value=value;
			this.type=type;
		}
		
		public String getValue(){return this.value;}
		public SequenceType getType(){return this.type;}
	};
	
	public enum GapPositions
	{
		NONE("n"),
		HALF("h"),
		ALL("a");
		
		private String value;
		
		GapPositions(String value){this.value=value;}
		
		public String getValue(){return this.value;}		
	};
}