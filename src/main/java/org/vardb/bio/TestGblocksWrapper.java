package org.vardb.bio;

import java.util.Map;

import org.vardb.util.CException;
import org.vardb.util.CFileHelper;

public class TestGblocksWrapper //extends AbstractAnalysisTest
{
	protected String dir="d:/research/software/gblocks/";
	protected String tempDir="d:/temp/";
	
	
	//@Test
	public void testGblocks()
	{
		String aln=getResource("org/vardb/bio/","gblocks.aln");
		GBlocksWrapper wrapper=getGBlocksWrapper();
		GBlocksWrapper.Params params=new GBlocksWrapper.Params();
		Map<String,String> alignment=SequenceFileParser.readFastaAlignment(aln,SequenceType.AA);
		GblocksAnalysis analysis=wrapper.gblocks(alignment,params);
		SimpleLocation location=analysis.getLocation();
		//System.out.println("location="+location);
		//Assertions.assertThat(location.toString()).isEqualTo("25..35,132..146,161..175,193..207,230..252");
	}
	
	private GBlocksWrapper getGBlocksWrapper()
	{
		return new GBlocksWrapper(this.dir,this.tempDir);
	}
	
	public static String getResource(String path, String filename)
	{
		String str=CFileHelper.getResource(path+filename);
		if (str==null)
			throw new CException("can't find resouce: "+path+filename);
		return str;
	}
	
	/*
	@Test
	public void testGblocksUnaligned()
	{
		String aln=getResource("gblocks.aln");
		CGBlocksWrapper wrapper=getGBlocksWrapper();
		CGBlocksWrapper.Params params=new CGBlocksWrapper.Params();
		Map<String,String> alignment=CSequenceFileParser.readFastaAlignment(aln,CSequenceType.AA);
		alignment.put("NOTALIGNED","GGGGGGGGGGGGGGGGGGG");
		CGblocksAnalysis analysis=wrapper.gblocks(alignment,params);
		CSimpleLocation location=analysis.getLocation();
		//System.out.println("location="+location);
		Assertions.assertThat(location.toString()).isEqualTo("25..35,132..146,161..175,193..207,230..252");
	}
	*/
}
