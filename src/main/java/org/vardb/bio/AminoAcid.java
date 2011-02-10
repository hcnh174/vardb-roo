package org.vardb.bio;

import java.util.ArrayList;
import java.util.List;

//GASTCVLIMPFYWDENQHKR
public enum AminoAcid
{
	GLYCINE("G","Gly","Glycine",AminoAcidGroup.SMALL,AminoAcidType.TINY),
	ALANINE("A","Ala","Alanine",AminoAcidGroup.SMALL,AminoAcidType.TINY),
	SERINE("S","Ser","Serine",AminoAcidGroup.NUCLEOPHILIC,AminoAcidType.TINY),
	THREONINE("T","Thr","Threonine",AminoAcidGroup.NUCLEOPHILIC,AminoAcidType.TINY),
	CYSTEINE("C","Cys","Cysteine",AminoAcidGroup.NUCLEOPHILIC,AminoAcidType.CYSTEINE),
	VALINE("V","Val","Valine",AminoAcidGroup.HYDROPHOBIC,AminoAcidType.ALIPHATIC),
	LEUCINE("L","Leu","Leucine",AminoAcidGroup.HYDROPHOBIC,AminoAcidType.ALIPHATIC),
	ISOLEUCINE("I","Ile","Isoleucine",AminoAcidGroup.HYDROPHOBIC,AminoAcidType.ALIPHATIC),
	METHIONINE("M","Met","Methionine",AminoAcidGroup.HYDROPHOBIC,AminoAcidType.OTHER),
	PROLINE("P","Pro","Proline",AminoAcidGroup.HYDROPHOBIC,AminoAcidType.OTHER),
	PHENYLALANINE("F","Phe","Phenylalanine",AminoAcidGroup.AROMATIC,AminoAcidType.AROMATIC),
	TYROSINE("Y","Tyr","Tyrosine",AminoAcidGroup.AROMATIC,AminoAcidType.POLAR),
	TRYPTOPHAN("W","Trp","Tryptophan",AminoAcidGroup.AROMATIC,AminoAcidType.AROMATIC),
	ASPARTIC_ACID("D","Asp","Aspartic Acid",AminoAcidGroup.ACIDIC,AminoAcidType.POLAR),
	GLUTAMIC_ACID("E","Glu","Glutamic Acid",AminoAcidGroup.ACIDIC,AminoAcidType.POLAR),
	ASPARAGINE("N","Asn","Asparagine",AminoAcidGroup.AMIDE,AminoAcidType.POLAR),
	GLUTAMINE("Q","Gln","Glutamine",AminoAcidGroup.AMIDE,AminoAcidType.POLAR),
	HISTIDINE("H","His","Histidine",AminoAcidGroup.BASIC,AminoAcidType.POLAR),
	LYSINE("K","Lys","Lysine",AminoAcidGroup.BASIC,AminoAcidType.POLAR),
	ARGININE("R","Arg","Arginine",AminoAcidGroup.BASIC,AminoAcidType.POLAR),
	GAP("-","Gap","Gap",null,null),
	X("X","X","X",null,null),
	STOP("*","Stop","Stop",null,null);
	
	private final String code;
	private final String shortname;
	private final String longname;
	private final AminoAcidGroup group;
	private final AminoAcidType type;

	AminoAcid(String code, String shortname, String longname, AminoAcidGroup group, AminoAcidType type)
	{
		this.code=code;
		this.shortname=shortname;
		this.longname=longname;
		this.group=group;
		this.type=type;
	}
	
	public String getCode(){return this.code;}
	public String getShort(){return this.shortname;}
	public String getLongname(){return this.longname;}
	public AminoAcidGroup getGroup(){return this.group;}
	public AminoAcidType getType(){return this.type;}
	

	public static AminoAcid find(char code)
	{
		return find(String.valueOf(code));
	}
	
	public static AminoAcid find(String code)
	{
		for (AminoAcid aa : AminoAcid.values())
		{
			if (aa.getCode().equals(code))
				return aa;
		}
		System.err.println("can't find AminoAcid ["+code+"]");
		return AminoAcid.X;
		//throw new CException("can't find AminoAcid ["+code+"]");
	}
	
	public static String getCodes(boolean strict)
	{
		StringBuilder builder=new StringBuilder();
		for (AminoAcid aa : AminoAcid.values())
		{
			if (strict && (aa==GAP || aa==X || aa==STOP))
				continue;
			builder.append(aa.getCode());
		}
		return builder.toString();
	}
	
	public static List<AminoAcid> values(boolean strict)
	{
		List<AminoAcid> list=new ArrayList<AminoAcid>();
		for (AminoAcid aa : AminoAcid.values())
		{
			if (strict && (aa==GAP || aa==X || aa==STOP))
				continue;
			list.add(aa);
		}
		return list;
	}
	
	public static List<AminoAcid> getCoding()
	{
		List<AminoAcid> list=new ArrayList<AminoAcid>();
		for (AminoAcid aa : AminoAcid.values())
		{
			if (aa==GAP || aa==X)
				continue;
			list.add(aa);
		}
		return list;
	}
	
	public static List<AminoAcid> findByGroup(AminoAcidGroup group)
	{
		List<AminoAcid> list=new ArrayList<AminoAcid>();
		for (AminoAcid aa : AminoAcid.values())
		{
			if (aa.getGroup()!=null && aa.getGroup()==group)
				list.add(aa);
		}
		return list;
	}
	
	public static List<AminoAcid> findByType(AminoAcidType type)
	{
		List<AminoAcid> list=new ArrayList<AminoAcid>();
		for (AminoAcid aa : AminoAcid.values())
		{
			if (aa.getType()!=null && aa.getType()==type)
				list.add(aa);
		}
		return list;
	}
	
	public String getZappo()
	{
		switch(this)
		{
		case ISOLEUCINE:
		case LEUCINE:
		case VALINE:
		case ALANINE:
		case METHIONINE:
			return "#ffafaf";
		case PHENYLALANINE:
		case TRYPTOPHAN:
		case TYROSINE:
			return "#ffc800";
		case LYSINE:
		case ARGININE:
		case HISTIDINE:
		case ASPARTIC_ACID:
		case GLUTAMIC_ACID:
			return "#6464ff";
		case SERINE:
		case THREONINE:
		case ASPARAGINE:
		case GLUTAMINE:
			return "#00ff00";
		case PROLINE:
		case GLYCINE:
			return "#ff00ff";
		case CYSTEINE:
			return "#ffff00";
		default:
			return "gray";
		}
	}
	
	public String getHydrophobicity()
	{
		switch(this)
		{
		case ISOLEUCINE: return "#ff00d6";
		case LEUCINE: return "#ffad00";
		case VALINE: return "#ff8400";
		case ALANINE: return "#ffef00";
		case METHIONINE: return "#ffc600";
		case PHENYLALANINE: return "#ff0000";
		case TRYPTOPHAN: return "#ffd600";
		case TYROSINE: return "#7bff00";
		case LYSINE: return "#0000ff";
		case ARGININE: return "#bd00ff";
		case HISTIDINE: return "#00ffad";
		case ASPARTIC_ACID: return "#00adff";
		case GLUTAMIC_ACID: return "#00ffc6";
		case SERINE: return "#00ff94";
		case THREONINE: return "#00ff6b";
		case ASPARAGINE: return "#00ffff";
		case GLUTAMINE: return "#00efff";
		case PROLINE: return "#00ff00";
		case GLYCINE: return "#ffff00";
		case CYSTEINE: return "#c6ff00";
		default: return "gray";
		}
	}
}