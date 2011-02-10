package org.vardb.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.vardb.util.CCounts;
import org.vardb.util.CException;

public class CTagCloud
{
	protected double base=8;
	protected double max=36;
	protected double range=max-base;
	protected List<Facet> facets=new ArrayList<Facet>();
	
	public CTagCloud(CCounts counts)
	{
		this(counts,8,36);
	}
	
	public CTagCloud(CCounts counts, int base, int max)
	{
		this.base=base;
		this.max=max;
		this.range=this.max-this.base;
		for (String name : counts.getTypes().keySet())
		{
			List<CCounts.Count> items=counts.getTypes().get(name);
			add(new Facet(name,items));
		}
		this.calculate();
	}
	
	@JsonProperty
	public List<Facet> getFacets()
	{
		List<Facet> facets=new ArrayList<Facet>();
		for (Facet facet : this.facets)
		{
			if (facet.getVisible())
				facets.add(facet);
		}
		return facets;
	}
	
	public void add(Facet facet)
	{
		this.facets.add(facet);
	}
	
	private double getMin()
	{
		double min=Integer.MAX_VALUE;
		for (Facet facet : this.facets)
		{
			double logcount=facet.getMin();
			if (logcount<min)
				{min=logcount;}
		}
		return min;
	}
	
	private double getMax()
	{
		double max=0;
		for (Facet facet : this.facets)
		{
			double logcount=facet.getMax();
			if (logcount>max)
				{max=logcount;}
		}
		return max;
	}
	
	private void calculate()
	{
		double min=getMin();
		double max=getMax();
		System.out.println("min="+min+", max="+max);
		for (Facet facet : this.facets)
		{
			facet.calculate(min,max);
		}
	}		
	
	public class Facet
	{
		protected String name;
		protected boolean visible=true;
		protected List<Tag> tags=new ArrayList<Tag>();
		
		@JsonProperty
		public String getName(){return this.name;}
		public void setName(final String name){this.name=name;}

		@JsonProperty
		public boolean getVisible(){return this.visible;}
		
		@JsonProperty
		public List<Tag> getTags(){return this.tags;}		
		
		public Facet(String name, List<CCounts.Count> items)
		{
			this.name=name;
			for (CCounts.Count item : items)
			{
				add(new Tag(item));
			}
		}
		
		public void add(Tag tag)
		{
			this.tags.add(tag);
		}
		
		@JsonProperty
		public double getMin()
		{
			double min=Integer.MAX_VALUE;
			for (Tag tag : this.tags)
			{
				double logcount=tag.getLogcount();
				if (logcount<min)
					{min=logcount;}
			}
			return min;
		}
		
		@JsonProperty
		public double getMax()
		{
			double max=0;
			for (Tag tag : this.tags)
			{
				double logcount=tag.getLogcount();
				if (logcount>max)
					{max=logcount;}
			}
			return max;
		}
		
		private void calculate(double min, double max)
		{
			if (this.tags.isEmpty())
			{
				this.visible=false;
				return;
			}
			for (Tag tag : this.tags)
			{
				if (max-min==0)
					tag.setFontsize(base+range);
				else tag.setFontsize(Math.floor(base+((tag.getLogcount()-min)*(range/(max-min)))));
				if (tag.getFontsize()==0)
					throw new CException("numsequences="+tag.getCount()+", logcount="+tag.getLogcount()+", min="+min+", max="+max);
			}
			
			Collections.sort(this.tags,new TagComparator());
		}
	}
	
	public static class Tag
	{
		protected String name;
		protected String value;
		protected Integer count;
		protected double logcount;
		protected double fontsize;

		@JsonProperty
		public String getName(){return this.name;}
		public void setName(final String name){this.name=name;}

		@JsonProperty
		public String getValue(){return this.value;}
		public void setValue(final String value){this.value=value;}

		@JsonProperty
		public Integer getCount(){return this.count;}
		public void setCount(final Integer count){this.count=count;}

		@JsonProperty
		public double getLogcount(){return this.logcount;}
		public void setLogcount(final double logcount){this.logcount=logcount;}

		@JsonProperty
		public double getFontsize(){return this.fontsize;}
		public void setFontsize(final double fontsize){this.fontsize=fontsize;}
		
		public Tag(CCounts.Count count)
		{
			this(count.getName(),count.getValue(),count.getCount());
		}
		
		public Tag(String name, String value, int count)
		{
			this.name=name;			
			this.value=value;
			this.count=count;
			this.logcount=Math.ceil(Math.log(count));
		}
	}

	//@SuppressWarnings("serial")
	public static class TagComparator implements Comparator<Tag>//, Serializable
	{
		public int compare(Tag t1, Tag t2)
		{
			return t2.getCount().compareTo(t1.getCount());
		}
	}
}