package org.vardb.bio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.vardb.util.CStringHelper;

// a consensus object should be an array of positions
// with summary data for each position
public class Consensus
{
	protected int length;
	protected String structure;
	protected List<Consensus.Pos> positions=new ArrayList<Consensus.Pos>();
	
	public Consensus(String coded)
	{
		coded=coded.trim();
		List<String> arr=CStringHelper.splitAsList(coded,"\\|");
		for (int index=0;index<arr.size();index++)
		{
			String str=arr.get(index).trim();
			if (!CStringHelper.hasContent(str))
				continue;
			Consensus.Pos position=new Consensus.Pos(index+1);
			this.positions.add(position);
			if (SequenceHelper.GAP.equals(str))
			{
				position.setConsensus(AminoAcid.GAP);
				continue;
			}
			List<String> a=CStringHelper.splitAsList(str,",");
			if (a.size()!=3)
			{
				System.err.println("can't interpret coded consensus position: ["+str+"]");
				continue;
			}
			position.setConsensus(AminoAcid.find(a.get(0)));
			position.setIdentity(Float.valueOf(a.get(1)));
			position.setVariability(Float.valueOf(a.get(2)));
		}
		this.length=this.positions.size();
	}
	
	public Consensus(int length)
	{
		this.length=length;
		for (int index=0;index<this.length;index++)
		{
			int number=index+1;
			this.positions.add(new Consensus.Pos(number));
		}
	}
	
	public Consensus(Map<String,String> map)
	{
		this(SequenceHelper.findMaximumLength(map));
		for (String sequence : map.values())
		{
			add(sequence);
		}
	}
	
	public Consensus(Collection<String> sequences)
	{
		this(SequenceHelper.findMaximumLength(sequences));
		for (String sequence : sequences)
		{
			add(sequence);
		}
	}
	
	private void add(String sequence)
	{
		if (sequence==null)
			return;
		sequence=SequenceHelper.pad(sequence,this.length);
		for (int index=0;index<this.length;index++)
		{
			Position position=this.positions.get(index);
			String ch=sequence.substring(index,index+1);
			AminoAcid aa=AminoAcid.find(ch);
			position.count(aa);
		}
	}

	public String getStructure(){return this.structure;}
	public void setStructure(String structure){this.structure=structure;}
	
	public List<Consensus.Pos> getPositions(){return this.positions;}
	
	public int getNumcolumns()
	{
		return this.positions.size();
	}
	
	public Position getPosition(int column)
	{
		return this.positions.get(column-1);
	}
	
	public String getSequence()
	{
		StringBuilder buffer=new StringBuilder();
		for (Position position : this.positions)
		{
			buffer.append(position.getConsensus().getCode());
		}
		return buffer.toString();
	}
	
	public String getConservationBitstring(float threshold)
	{
		StringBuilder buffer=new StringBuilder();
		for (Position position : this.positions)
		{
			if (position.getIdentity()>=threshold)
				buffer.append("1");
			else buffer.append("0");
		}
		return buffer.toString();
	}
	
	public float getIdentity()
	{
		float sum=0.0f;
		for (Position position : this.positions)
		{
			sum+=position.getIdentity();
		}
		return sum/(float)this.positions.size();
	}
	
	public float compareSequence(String sequence)
	{
		int diffs=0;
		for (Position position : this.positions)
		{
			String aa1=position.getConsensus().getCode();
			int index=position.getNumber()-1;
			String aa2=sequence.substring(index,index+1);
			if (!aa1.equals(aa2))
				diffs++;
		}
		return (float)diffs/(float)this.positions.size();
	}

	public String getCoded()
	{
		List<String> positions=new ArrayList<String>();
		for (Position position : this.positions)
		{
			positions.add(getCoded(position));
		}
		return CStringHelper.join(positions,"|");
	}

	private String getCoded(Position position)
	{
		if (position.getConsensus()==AminoAcid.GAP)
			return SequenceHelper.GAP;
		StringBuilder buffer=new StringBuilder();
		buffer.append(position.getConsensus().getCode());
		buffer.append(",");
		buffer.append(CStringHelper.formatDecimal(position.getIdentity(),2));
		buffer.append(",");
		buffer.append(CStringHelper.formatDecimal(position.getSimpsonDiversityIndex(),2));
		return buffer.toString();
	}
	
	/*
	public CTable getTable()
	{
		int decimals=3;
		CTable table=new CTable();
		table.getHeader().add("Position");
		table.getHeader().add("Consensus");
		table.getHeader().add("Shannon entropy");
		table.getHeader().add("Simpson diversity");
		table.getHeader().add("Wu-Kabat");
		for (Consensus.Pos position : this.positions)
		{
			CTable.Row row=table.addRow();
			row.add(position.getNumber());
			row.add(position.getConsensus().getCode()).setAlign("center");
			row.add(CStringHelper.formatDecimal(position.getShannonEntropy(),decimals)).setAlign("right");
			row.add(CStringHelper.formatDecimal(position.getSimpsonDiversityIndex(),decimals)).setAlign("right");
			row.add(CStringHelper.formatDecimal(position.getWuKabatVariabilityCoefficient(),decimals)).setAlign("right");
		}
		return table;
	}
	*/
	
	public String getDataframe()
	{
		StringBuilder buffer=new StringBuilder();
		List<String> columns=Arrays.asList("\"position\"","\"shannon\"","\"simpson\"","\"wukabat\"");
		buffer.append(CStringHelper.join(columns," "));
		buffer.append("\n");
		
		for (Consensus.Pos position : this.positions)
		{
			columns=new ArrayList<String>();
			columns.add(String.valueOf(position.getNumber()));
			//columns.add(position.getConsensus().getCode());
			columns.add(String.valueOf(position.getShannonEntropy()));
			columns.add(String.valueOf(position.getSimpsonDiversityIndex()));
			columns.add(String.valueOf(position.getWuKabatVariabilityCoefficient()));
			buffer.append(CStringHelper.join(columns," "));
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public String getVariabilityLocation()
	{
		return getVariabilityLocation(2.0f);
	}
	
	public String getVariabilityLocation(float threshold)
	{
		StringBuilder buffer=new StringBuilder();
		for (Position position : this.positions)
		{
			buffer.append((position.getShannonEntropy()>=threshold) ? 1 : 0);
		}
		return SimpleLocation.convertLocationRun(buffer.toString(),"1");
	}
	
	public String getConservedLocation()
	{
		return getConservedLocation(1.0f);
	}
	
	public String getConservedLocation(float threshold)
	{
		StringBuilder buffer=new StringBuilder();
		for (Position position : this.positions)
		{
			buffer.append((position.getShannonEntropy()<=threshold) ? 1 : 0);
		}
		return SimpleLocation.convertLocationRun(buffer.toString(),"1");
	}
	
	public List<Float> getShannonEntropy()
	{
		List<Float> data=new ArrayList<Float>();
		for (Consensus.Pos position : this.positions)
		{
			data.add(position.getShannonEntropy());
		}
		return data;
	}
	
	public List<Float> getSimpsonDiversityIndex()
	{
		List<Float> data=new ArrayList<Float>();
		for (Consensus.Pos position : this.positions)
		{
			data.add(position.getSimpsonDiversityIndex());
		}
		return data;
	}
	
	public List<Float> getWuKabatVariabilityCoefficient()
	{
		List<Float> data=new ArrayList<Float>();
		for (Consensus.Pos position : this.positions)
		{
			data.add(position.getWuKabatVariabilityCoefficient());
		}
		return data;
	}
	
	public static class Pos extends Position
	{		
		public Pos(int number)
		{
			super(number);
		}
	}
}
