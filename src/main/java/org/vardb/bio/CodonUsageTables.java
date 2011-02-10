package org.vardb.bio;

import java.util.ArrayList;
import java.util.List;

import org.vardb.util.CStringHelper;

public class CodonUsageTables
{
	protected List<CodonUsageTable> tables=new ArrayList<CodonUsageTable>();
	
	public List<CodonUsageTable> getTables(){return this.tables;}
	public void setTables(final List<CodonUsageTable> tables){this.tables=tables;}
	
	public CodonUsageTable addTable(String name)
	{
		CodonUsageTable table=new CodonUsageTable();
		table.setName(name);
		this.tables.add(table);
		return table;
	}
	
	public void add(CodonUsageTable table, String name)
	{
		this.tables.add(table);
		table.setName(name);
	}
	
	public void calculateFrequencies()
	{
		for (CodonUsageTable usage : this.tables)
		{
			usage.calculate();
		}
	}
	
	public List<UsageData> getData()
	{
		List<UsageData> data=new ArrayList<UsageData>();
		// use the first table to determine the codon order
		for (CodonUsageTable.Frequency freq : this.tables.get(0).getData())
		{
			data.add(new UsageData(freq.getCodon()));
		}

		for (int index=0;index<data.size();index++)
		{
			UsageData item=data.get(index);
			for (CodonUsageTable usage : this.tables)
			{
				CodonUsageTable.Frequency freq=usage.getFrequency(item.getCodon());
				item.add(freq);
			}
		}
		return data;
	}

	public String getWcaDataFrame()
	{
		StringBuilder buffer=new StringBuilder();
		for (int counter=1;counter<=getData().get(0).getSeries().size();counter++)
		{
			if (counter>1)
				buffer.append(" ");
			buffer.append("\""+counter+"\"");
		}
		buffer.append("\n");
		
		for (UsageData item : getData())
		{
			buffer.append("\""+item.getCodon().name()+"\"");			
			for (CodonUsageTable.Frequency freq : item.getSeries())
			{
				buffer.append(" ");
				buffer.append(freq.getCount());
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public String getChiSquareDataFrame()
	{
		String delimiter=" ";
		List<String> names=new ArrayList<String>();
		for (CodonUsageTable table : this.tables)
		{
			names.add("\""+table.getName()+"\"");
		}
		StringBuilder buffer=new StringBuilder();
		buffer.append(CStringHelper.join(names,delimiter));
		buffer.append("\n");

		for (Codon codon : CodonUsageTable.getCodonOrder())
		{
			buffer.append("\""+codon.name()+"\"");
			for (CodonUsageTable table : this.tables)
			{
				CodonUsageTable.Frequency freq=table.getFrequency(codon);
				buffer.append(" ");
				buffer.append(freq.getCount());
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	/*
	public CTable getTable()
	{
		CTable table=new CTable();
		table.setShowHeader(false);
		table.getHeader().add("Set");
		table.getHeader().add("AA");
		for (int num=1;num<=6;num++)
		{
			table.getHeader().add("Codon "+num);	
		}
		boolean useRelativeAdaptiveness=false;
		CodonUsageTable.FrequencyComparator comparator=new CodonUsageTable.FrequencyComparator(useRelativeAdaptiveness);
		for (CAminoAcid aa : CAminoAcid.values())
		{
			if (aa==CAminoAcid.GAP || aa==CAminoAcid.X)
				continue;
			for (CodonUsageTable usage : this.tables)
			{
				CTable.Row row=table.addRow();
				row.add(usage.getName());
				row.add(aa.getShort());
				List<CodonUsageTable.Frequency> list=usage.getFrequencies(aa);
				Collections.sort(list,comparator);
				for (CodonUsageTable.Frequency freq : list)
				{
					Codon codon=freq.getCodon();
					if (useRelativeAdaptiveness)
						row.add(codon.getRna()+" ("+String.format("%.2f",freq.getRelativeAdaptiveness())+")");
					else row.add(codon.getRna()+" ("+String.format("%.4f",freq.getFrequency())+")");
				}
				for (int num=list.size()+1;num<=6;num++)
				{
					//row.add("&nbsp;");
				}
			}
		}
		return table;
	}
	*/
	
	public static class UsageData
	{
		protected Codon codon;
		protected List<CodonUsageTable.Frequency> series=new ArrayList<CodonUsageTable.Frequency>();
		
		public UsageData(Codon codon)
		{
			this.codon=codon;
		}
		
		public void add(CodonUsageTable.Frequency freq)
		{
			this.series.add(freq);
		}
		
		public Codon getCodon(){return this.codon;}
		public List<CodonUsageTable.Frequency> getSeries(){return this.series;}
	}
}