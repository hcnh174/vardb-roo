package org.vardb.bio;

import java.util.ArrayList;
import java.util.List;

import org.vardb.util.CStringHelper;
import org.vardb.util.CTable;

public class AminoAcidUsageTables
{
	protected List<AminoAcidUsageTable> tables=new ArrayList<AminoAcidUsageTable>();
	
	public List<AminoAcidUsageTable> getTables(){return this.tables;}
	public void setTables(final List<AminoAcidUsageTable> tables){this.tables=tables;}
	
	public AminoAcidUsageTable addTable(String name)
	{
		AminoAcidUsageTable table=new AminoAcidUsageTable(name);
		this.tables.add(table);
		return table;
	}
	
	public void add(AminoAcidUsageTable table, String name)
	{
		this.tables.add(table);
		table.setName(name);
	}
	
	public void calculateFrequencies()
	{
		for (AminoAcidUsageTable usage : this.tables)
		{
			usage.calculateFrequencies();
		}
	}
	
	public List<UsageData> getData()
	{
		List<UsageData> data=new ArrayList<UsageData>();
		// use the first table to determine the codon order
		for (AminoAcidUsageTable.Frequency freq : this.tables.get(0).getData())
		{
			data.add(new UsageData(freq.getAa()));
		}

		for (int index=0;index<data.size();index++)
		{
			UsageData item=data.get(index);
			for (AminoAcidUsageTable usage : this.tables)
			{
				AminoAcid aa=item.getAa();
				AminoAcidUsageTable.Frequency freq=new AminoAcidUsageTable.Frequency(aa,usage.getCount(aa),usage.getFrequency(aa));
				item.add(freq);
			}
		}
		return data;
	}
	
	public CTable getTable()
	{
		CTable table=new CTable();
		table.getHeader().add("AA");
		table.getHeader().add("&nbsp;");
		for (AminoAcidUsageTable usage : this.tables)
		{
			table.getHeader().add(usage.getName());
		}
		for (AminoAcid aa : AminoAcid.values(true))
		{
			CTable.Row row=table.addRow();
			row.add(aa.getLongname());
			row.add(aa.getCode());
			for (AminoAcidUsageTable usage : this.tables)
			{
				row.add(CStringHelper.formatDecimal(usage.getFrequency(aa),4));
			}
		}
		return table;
	}
	
	public CTable getGroupTable()
	{
		CTable table=new CTable();
		table.getHeader().add("Group");
		for (AminoAcidUsageTable usage : this.tables)
		{
			table.getHeader().add(usage.getName());
		}
		for (AminoAcidGroup group : AminoAcidGroup.values())
		{
			CTable.Row row=table.addRow();
			row.add(group.getName());
			for (AminoAcidUsageTable usage : this.tables)
			{
				row.add(CStringHelper.formatDecimal(usage.getFrequencyByGroup(group),4));
			}
		}
		return table;
	}
	
	public static class UsageData
	{
		protected AminoAcid aa;
		protected List<AminoAcidUsageTable.Frequency> series=new ArrayList<AminoAcidUsageTable.Frequency>();
		
		public UsageData(AminoAcid aa)
		{
			this.aa=aa;
		}
		
		public void add(AminoAcidUsageTable.Frequency freq)
		{
			this.series.add(freq);
		}
		
		public AminoAcid getAa(){return this.aa;}
		public List<AminoAcidUsageTable.Frequency> getSeries(){return this.series;}
	}
}