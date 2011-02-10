package org.vardb.bio;

import org.junit.Test;

public class TestPdbReader
{	
	@Test
	public void testPdbReader()
	{
		String pdbid="1L8W";
		CStructure structure=new CStructure(pdbid);
		PdbHelper.downloadPdbFile(pdbid, structure);
		System.out.println("title: "+structure.getDescription());
		//System.out.println("sequence: "+structure.getSequence());
		//System.out.println("secondary structure: "+structure.getSecondaryStructure());
	}
}