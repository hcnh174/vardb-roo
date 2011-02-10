package org.vardb.bio;

import java.util.ArrayList;
import java.util.List;

import org.vardb.util.CException;

public enum Codon
{	
	ATT(AminoAcid.ISOLEUCINE),
	ATC(AminoAcid.ISOLEUCINE),
	ATA(AminoAcid.ISOLEUCINE),
	CTT(AminoAcid.LEUCINE),
	CTC(AminoAcid.LEUCINE),
	CTA(AminoAcid.LEUCINE),
	CTG(AminoAcid.LEUCINE),
	TTA(AminoAcid.LEUCINE),
	TTG(AminoAcid.LEUCINE),
	GTT(AminoAcid.VALINE),
	GTC(AminoAcid.VALINE),
	GTA(AminoAcid.VALINE),
	GTG(AminoAcid.VALINE),
	TTT(AminoAcid.PHENYLALANINE),
	TTC(AminoAcid.PHENYLALANINE),
	ATG(AminoAcid.METHIONINE),
	TGT(AminoAcid.CYSTEINE),
	TGC(AminoAcid.CYSTEINE),
	GCT(AminoAcid.ALANINE),
	GCC(AminoAcid.ALANINE),
	GCA(AminoAcid.ALANINE),
	GCG(AminoAcid.ALANINE),
	GGT(AminoAcid.GLYCINE),
	GGC(AminoAcid.GLYCINE),
	GGA(AminoAcid.GLYCINE),
	GGG(AminoAcid.GLYCINE),
	CCT(AminoAcid.PROLINE),
	CCC(AminoAcid.PROLINE),
	CCA(AminoAcid.PROLINE),
	CCG(AminoAcid.PROLINE),
	ACT(AminoAcid.THREONINE),
	ACC(AminoAcid.THREONINE),
	ACA(AminoAcid.THREONINE),
	ACG(AminoAcid.THREONINE),
	TCT(AminoAcid.SERINE),
	TCC(AminoAcid.SERINE),
	TCA(AminoAcid.SERINE),
	TCG(AminoAcid.SERINE),
	AGT(AminoAcid.SERINE),
	AGC(AminoAcid.SERINE),
	TAT(AminoAcid.TYROSINE),
	TAC(AminoAcid.TYROSINE),
	TGG(AminoAcid.TRYPTOPHAN),
	CAA(AminoAcid.GLUTAMINE),
	CAG(AminoAcid.GLUTAMINE),
	AAT(AminoAcid.ASPARAGINE),
	AAC(AminoAcid.ASPARAGINE),
	CAT(AminoAcid.HISTIDINE),
	CAC(AminoAcid.HISTIDINE),
	GAA(AminoAcid.GLUTAMIC_ACID),
	GAG(AminoAcid.GLUTAMIC_ACID),
	GAT(AminoAcid.ASPARTIC_ACID),
	GAC(AminoAcid.ASPARTIC_ACID),
	AAA(AminoAcid.LYSINE),
	AAG(AminoAcid.LYSINE),
	CGT(AminoAcid.ARGININE), 
	CGC(AminoAcid.ARGININE), 
	CGA(AminoAcid.ARGININE), 
	CGG(AminoAcid.ARGININE), 
	AGA(AminoAcid.ARGININE), 
	AGG(AminoAcid.ARGININE),
	TAA(AminoAcid.STOP),
	TAG(AminoAcid.STOP),
	TGA(AminoAcid.STOP);
	
	private final AminoAcid aminoAcid;

	Codon(AminoAcid aminoAcid)
	{
		this.aminoAcid=aminoAcid;
	}
	
	public AminoAcid getAminoAcid(){return this.aminoAcid;}
	
	public boolean isStopCodon(){return (this.aminoAcid==AminoAcid.STOP);}
	
	public String getRna()
	{
		return name().replace('T','U');
	}
	
	public static Codon find(String value)
	{
		value=value.toUpperCase();
		for (Codon codon : Codon.values())
		{
			if (codon.name().equals(value) || codon.getRna().equals(value))
				return codon;
		}
		return null;
	}
	
	public static List<Codon> getCodons(AminoAcid aa)
	{
		List<Codon> list=new ArrayList<Codon>();
		for (Codon codon : Codon.values())
		{
			if (codon.getAminoAcid()==aa)
				list.add(codon);
		}
		return list;
	}
	
	public String getNucleotide(int position)
	{
		if (position<0 || position>2)
			throw new CException("codon position should be between 0 and 2: "+position);
		return name().substring(position,position+1);
	}
	
	public static boolean isStopCodon(String value)
	{
		value=value.toUpperCase();
		return ("TAA".equals(value) || "TAG".equals(value) || "TGA".equals(value));
	}
}