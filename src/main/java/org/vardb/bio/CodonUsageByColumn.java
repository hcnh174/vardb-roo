package org.vardb.bio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vardb.util.CStringHelper;

public class CodonUsageByColumn
{
	public static final int BATCH_SIZE=50;//45;
	public static final int MAX_HEIGHT=400;
	
	private static final String TR_OPEN="<tr>\n";
	private static final String TR_CLOSE="</tr>\n";
	
	protected List<Column> columns=new ArrayList<Column>();
	private static final CountComparator COUNT_COMPARATOR=new CountComparator();
	
	public List<Column> getColumns(){return this.columns;}
	
	public CodonUsageByColumn(Collection<String> sequences)
	{
		int numcolumns=CodonHelper.getNumcolumns(sequences);
		for (int column=1;column<=numcolumns;column++)
		{
			this.columns.add(new Column(column));
		}
		
		for (String sequence : sequences)
		{
			try
			{
				for (int index=0;index<numcolumns;index++)
				{
					Column column=this.columns.get(index);
					int start=(index)*3;
					Codon codon=Codon.find(sequence.substring(start,start+3));
					if (codon!=null)
						column.count(codon);
				}
			}
			catch(Exception e)
			{
				System.err.println("problem counting codons in sequence: "+sequence+"; e"+e.getMessage());
			}
		}
		calculate();
	}
	
	private void calculate()
	{
		for (Column column : this.columns)
		{
			column.calculate();
		}
	}
	
	public String getAminoAcids()
	{
		float ratio=getRatio(getMaxCount(this.columns));
		List<List<Column>> batches=createBatches(this.columns,BATCH_SIZE);
		StringBuilder buffer=new StringBuilder();
		for (List<Column> batch : batches)
		{
			getAminoAcidRow(batch,ratio,buffer);
		}
		return buffer.toString();
	}
		
	public String getCodons()
	{
		return getHtml(this.columns);
	}

	public String getCysteines()
	{
		List<Column> columns=new ArrayList<Column>();
		for (int index=0;index<this.columns.size();index++)
		{
			Column column=this.columns.get(index);
			if (column.getConsensus().getConsensus()==AminoAcid.CYSTEINE)
				columns.add(column);
		}
		return getHtml(columns);
	}
	
	public String getNucleotides()
	{
		float ratio=getRatio(getMaxNucleotideCount(this.columns));
		List<List<Column>> batches=createBatches(this.columns,Math.round(BATCH_SIZE/3));
		StringBuilder buffer=new StringBuilder();
		for (List<Column> batch : batches)
		{
			getNucleotideRow(batch,ratio,buffer);
		}
		return buffer.toString();
	}
	
	public String getHtml(List<Column> columns)
	{
		float ratio=getRatio(getMaxCount(columns));
		List<List<Column>> batches=createBatches(columns,BATCH_SIZE);
		StringBuilder buffer=new StringBuilder();
		for (List<Column> batch : batches)
		{
			getCodonRow(batch,ratio,buffer);
		}
		return buffer.toString();
	}
	
	private static float getRatio(int max)
	{
		return (float)MAX_HEIGHT/(float)max;
	}
	
	private static List<List<Column>> createBatches(List<Column> columns, int batchsize)
	{
		List<List<Column>> batches=new ArrayList<List<Column>>();
		int total=columns.size();
		int numbatches=(int)((float)total/(float)batchsize);
		if (total % batchsize!=0)
			numbatches++;
		for (int batch=0;batch<numbatches;batch++)
		{
			int start=batch*batchsize;
			//System.out.println("batch="+batch+", start="+start);
			int end=start+batchsize;
			if (end>total-1)
				end=total-1;
			batches.add(columns.subList(start,end));
		}
		return batches;
	}
	
	private static void getCodonRow(List<Column> columns, float ratio, StringBuilder buffer)
	{
		buffer.append("<table class=\"codonusagebycolumn rafiki\" cellspacing=\"0\">\n");
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			buffer.append(getTh(column.getColumn(),3));
		}
		buffer.append(TR_CLOSE);
		
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			buffer.append("<th title=\""+column.getConsensus().getConsensus().getLongname()+"\">");
			buffer.append(column.getConsensus().getConsensus().getCode()+"</th>\n");
		}
		buffer.append(TR_CLOSE);
		
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			buffer.append("<td valign=\"top\">");
			for (Count count : column.getCounts())
			{	
				Codon codon=count.getCodon();
				AminoAcid aa=codon.getAminoAcid();
				count.getHtml(ratio,aa.getCode(),aa.getLongname(),codon.name(),buffer);
			}			
			buffer.append("</td>\n");
		}
		buffer.append(TR_CLOSE);
		buffer.append("</table>\n");
		buffer.append("<br/>\n");
	}
	
	private static void getAminoAcidRow(List<Column> columns, float ratio, StringBuilder buffer)
	{
		buffer.append("<table class=\"codonusagebycolumn zappo\" cellspacing=\"0\">\n");
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			buffer.append(getTh(column.getColumn(),3));
		}
		buffer.append(TR_CLOSE);
		
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			buffer.append("<th title=\""+column.getConsensus().getConsensus().getLongname()+"\">");
			buffer.append(column.getConsensus().getConsensus().getCode()+"</th>\n");
		}
		buffer.append(TR_CLOSE);
		
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			buffer.append("<td valign=\"top\">");
			for (AminoAcidCount count : column.getAminoAcidCounts())
			{	
				AminoAcid aa=count.getAminoAcid();
				count.getHtml(ratio,aa.getCode(),aa.getLongname(),aa.getCode(),buffer);
			}			
			buffer.append("</td>\n");
		}
		buffer.append(TR_CLOSE);
		buffer.append("</table>\n");
		buffer.append("<br/>\n");
	}
	
	private static void getNucleotideRow(List<Column> columns, float ratio, StringBuilder buffer)
	{
		buffer.append("<table class=\"codonusagebycolumn nucleotide\" cellspacing=\"0\">\n");
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			for (int index=0;index<3;index++)
			{
				int number=(column.getColumn()-1)*3+index+1;
				buffer.append(getTh(number,4));
			}
		}
		buffer.append(TR_CLOSE);		
		buffer.append(TR_OPEN);
		for (Column column : columns)
		{
			for (int index=0;index<3;index++)
			{
				buffer.append("<td valign=\"top\">");
				for (NucleotideCount count : column.getNucleotideCounts(index))
				{	
					String nt=count.getNucleotide();
					//System.out.println("column="+column.getColumn()+", nt="+count.getNucleotide()+", "+count.getCount());
					count.getHtml(ratio,nt,nt,nt,buffer);
				}
				buffer.append("</td>\n");
			}			
		}
		buffer.append(TR_CLOSE);
		buffer.append("</table>\n");
		buffer.append("<br/>\n");
	}
	
	private static String getTh(int number, int padlength)
	{
		String padded=CStringHelper.padLeft(String.valueOf(number),' ',padlength);
		padded=CStringHelper.replace(padded," ","&nbsp;");
		return "<th title=\""+number+"\">"+padded+"</th>\n";
	}
	
	private static int getMaxCount(List<Column> columns)
	{
		int max=0;
		for (int i=0;i<columns.size();i++)
		{
			Column column=columns.get(i);
			int colsum=column.getSum();
			if (colsum>max)
				{max=colsum;}
		}
		return max;
	}
	
	private static int getMaxNucleotideCount(List<Column> columns)
	{
		int max=0;
		for (int i=0;i<columns.size();i++)
		{
			Column column=columns.get(i);
			for (int index=0;index<3;index++)
			{
				int colsum=column.getNucleotideSum(index);
				if (colsum>max)
					{max=colsum;}
			}
		}
		return max;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	public class Column
	{
		protected Integer column;
		protected Map<Codon,Count> counts=new LinkedHashMap<Codon,Count>();
		protected int sum=0;
		protected Position consensus=null;
		protected Float shannonEntropy=null;
		protected Float simpsonDiversityIndex=null;
		protected Float wuKabatVariabilityCoefficient=null;
	
		public Column(int column)
		{
			this.column=column;
			initializeCounts();
		}
		
		public int getColumn(){return this.column;}
		public Count getCount(Codon codon){return this.counts.get(codon);}
		public int getSum(){return this.sum;}
		public Position getConsensus(){return this.consensus;}		
		public Float getShannonEntropy(){return this.shannonEntropy;}
		public Float getSimpsonDiversityIndex(){return this.simpsonDiversityIndex;}
		public Float getWuKabatVariabilityCoefficient(){return this.wuKabatVariabilityCoefficient;}
		
		public void calculate()
		{
			this.consensus=new Position(this.column);
			Map<Codon,Integer> counts=new LinkedHashMap<Codon,Integer>();
			for (Count count : this.counts.values())
			{
				this.sum+=count.getCount();
				this.consensus.count(count.getCodon().getAminoAcid(),count.getCount());
				counts.put(count.getCodon(),count.getCount());
			}
			this.shannonEntropy=VariabilityHelper.getShannonEntropy(counts);
			this.simpsonDiversityIndex=VariabilityHelper.getSimpsonDiversityIndex(counts);
			this.wuKabatVariabilityCoefficient=VariabilityHelper.getWuKabatVariabilityCoefficient(counts);
		}

		public Collection<Count> getCounts()
		{			
			List<Count> counts=new ArrayList<Count>();
			counts.addAll(this.counts.values());
			Collections.sort(counts,COUNT_COMPARATOR);
			return counts;
		}
		
		public Collection<AminoAcidCount> getAminoAcidCounts()
		{
			List<AminoAcidCount> counts=new ArrayList<AminoAcidCount>();
			for (AminoAcid aa : this.consensus.getCounts().keySet())
			{
				counts.add(new AminoAcidCount(aa,this.consensus.getCounts().get(aa)));
			}
			Collections.sort(counts,COUNT_COMPARATOR);
			return counts;
		}
		
		public Collection<NucleotideCount> getNucleotideCounts(int position)
		{
			List<NucleotideCount> counts=new ArrayList<NucleotideCount>();
			Map<String,Integer> map=new LinkedHashMap<String,Integer>();
			for (Nucleotide nt : Nucleotide.values())
			{
				map.put(nt.name(),0);
			}
			for (Count count : this.counts.values())
			{
				Codon codon=count.getCodon();
				String nt=codon.getNucleotide(position);
				int num=map.get(nt);
				map.put(nt,num+count.getCount());
			}
			//System.out.println("map="+CStringHelper.toJson(true,map));
			for (String nt : map.keySet())
			{
				counts.add(new NucleotideCount(nt,map.get(nt)));
			}
			Collections.sort(counts,COUNT_COMPARATOR);
			return counts;
		}
		
		public int getNucleotideSum(int position)
		{
			int sum=0;
			for (NucleotideCount count : getNucleotideCounts(position))
			{	
				sum+=count.getCount();
			}
			return sum;
		}
		
		public int size()
		{
			return this.counts.size(); 
		}
		
		public int count(Codon codon)
		{
			Count count=this.counts.get(codon);
			count.increment();
			this.counts.put(codon,count);
			return count.getCount();
		}
		
		private void initializeCounts()
		{
			for (Codon codon : Codon.values())
			{
				this.counts.put(codon,new Count(codon));
			}
		}
	}

	public interface ICount
	{
		Integer getCount();
		void setCount(final Integer count);
	}
	
	public abstract class AbstractCount implements ICount
	{
		protected Integer count=0;

		public AbstractCount(){}
		
		public AbstractCount(int count)
		{
			this.count=count;
		}

		public Integer getCount(){return this.count;}
		public void setCount(final Integer count){this.count=count;}
		
		public void increment(){this.count=this.count+1;}
		
		public int getHeight(float ratio)
		{
			return Math.round(ratio*(float)this.count);
		}
		
		public void getHtml(float ratio, String cls, String title, String content, StringBuilder buffer)
		{
			if (this.count<1)
				return;
			String style="height:"+getHeight(ratio)+"px;";
			title+=" ("+this.count+")";
			buffer.append("<div class=\""+cls+"\" style=\""+style+"\" title=\""+title+"\">"+content+"</div>");
		}
	}
	
	public class Count extends AbstractCount implements ICount
	{
		protected Codon codon;
		
		public Count(Codon codon)
		{
			this.codon=codon;
		}
		
		public Count(Codon codon, int count)
		{
			super(count);
			this.codon=codon;
		}
		
		public Codon getCodon(){return this.codon;}
		public void setCodon(final Codon codon){this.codon=codon;}
	}
	
	public class AminoAcidCount extends AbstractCount implements ICount
	{
		protected AminoAcid aminoAcid;
		
		public AminoAcidCount(AminoAcid aa)
		{
			this.aminoAcid=aa;
		}
		
		public AminoAcidCount(AminoAcid aa, int count)
		{
			super(count);
			this.aminoAcid=aa;
		}
		
		public AminoAcid getAminoAcid(){return this.aminoAcid;}
		public void setAminoAcid(final AminoAcid aa){this.aminoAcid=aa;}
	}
	
	public class NucleotideCount extends AbstractCount implements ICount
	{
		protected String nt;
		
		public NucleotideCount(String nt)
		{
			this.nt=nt;
		}
		
		public NucleotideCount(String nt, int count)
		{
			super(count);
			this.nt=nt;
		}
		
		public String getNucleotide(){return this.nt;}
		public void setNucleotide(final String nt){this.nt=nt;}
	}
	
	@SuppressWarnings("serial")
	public static class CountComparator implements Comparator<ICount>, Serializable
	{
		public int compare(ICount c1, ICount c2)
		{
			return c2.getCount()-c1.getCount();
		}
	}
}