package org.vardb.bio;

import java.util.List;
import java.util.Map;

import org.biojava.bio.structure.AminoAcid;
import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.PDBHeader;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;
import org.vardb.util.CException;
import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;
import org.vardb.util.HttpHelper;

public final class PdbHelper
{	
	private PdbHelper(){}
	
	//http://www.pdb.org/pdb/files/4hhb.pdb
	public static void downloadPdbFile(String name, CStructure structure)
	{
		String url="http://www.pdb.org/pdb/files/"+name+".pdb";
		String str=HttpHelper.getRequest(url);
		String filename=CFileHelper.createTempFile(name,"pdb");
		System.out.println("temp pdb filename: "+filename);
		CFileHelper.writeFile(filename,str);
		parsePdbFile(filename,structure);		
	}
	
	public static void parsePdbFile(String filename, CStructure structure)
	{
		Structure struct=parsePdbFile(filename);
		updateStructure(structure,struct);
	}
	
	public static Structure parsePdbFile(String filename)
	{
		try
		{
			PDBFileReader pdbreader = new PDBFileReader();
			//pdbreader.setParseSecStruc(true);
			Structure structure = pdbreader.getStructure(filename);
			//System.out.println(structure);
			return structure;
		}
		catch (Exception e)
		{
			throw new CException(e);
		}
	}
    
	public static void updateStructure(CStructure structure, Structure struc)
	{
		PDBHeader header=struc.getPDBHeader();
		structure.setDescription(isNull(header.getTitle(),""));
		structure.setTechnique(isNull(header.getTechnique(),""));
		structure.setResolution(header.getResolution());
		
		structure.getChains().clear();
		for (Chain chn : struc.getChains())
		{
			String name=chn.getName().trim();
			if (CStringHelper.isEmpty(name))
				continue;
			CChain chain=structure.findOrCreateChain(name);
			chain.setSwissprot(chn.getSwissprotId());	
			chain.setSequence(chn.getAtomSequence());
			chain.setSecondaryStructure(getSecondaryStructure(chn));
		}
		structure.setNumchains(structure.getChains().size());
	}
	
	private static String isNull(String value, String dflt)
	{
		if (value==null)
			value=dflt;
		return value;
	}

	private static final String amino="amino";
	private static final String HELIX="HELIX";
	private static final String STRAND="STRAND";
	private static final String H="H";
	private static final String E="E";
	private static final String GAP="-";
	
	@SuppressWarnings("deprecation")
	protected static String getSecondaryStructure(Chain chain)
    {
		List<Group> aminos = chain.getGroups(amino);
		StringBuilder buffer=new StringBuilder();
		for (int i=0; i < aminos.size(); i++)
		{
			AminoAcid aa = (AminoAcid)aminos.get(i);
			Map<String,String> ss=aa.getSecStruc();
			String feature=(String)ss.get("PDB_AUTHOR_ASSIGNMENT");
			if (!CStringHelper.hasContent(feature))
				feature=GAP;
			else if (HELIX.equals(feature))
				feature=H;
			else if (STRAND.equals(feature))
				feature=E;
			else feature=CStringHelper.parenthesize(feature);
			buffer.append(feature);
		}
		return buffer.toString();
    }
	
	@SuppressWarnings("deprecation")
    protected static String getSequence(Chain chain)
    {
         List<Group> aminos = chain.getGroups(amino);
         StringBuilder buffer=new StringBuilder();
         for (int i=0; i < aminos.size(); i++)
         {
             AminoAcid aa = (AminoAcid)aminos.get(i);
             buffer.append(aa.getAminoType());
         }
         return buffer.toString();
    }
    
    protected static String getSwissprotId(Structure struc)
    {
    	 List<Chain> chains = struc.getModel(0);
         Chain chain=chains.get(0);
         return chain.getSwissprotId();
    }
}
