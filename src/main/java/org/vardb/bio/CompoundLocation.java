package org.vardb.bio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.vardb.util.CException;
import org.vardb.util.CStringHelper;

public class CompoundLocation
{
	private List<NamedLocation> locations=new ArrayList<NamedLocation>();
	
	public CompoundLocation(){}
	
	public CompoundLocation(String value)
	{
		if (!CStringHelper.hasContent(value))
			throw new CException("compound location value is empty: ["+value+"]");//return;
		for (String str : CStringHelper.split(value,";"))
		{
			String name="";
			String location=str;
			if (str.indexOf(':')!=-1)
			{
				List<String> parts=CStringHelper.splitAsList(str,":");
				name=parts.get(0);
				location=parts.get(1);
			}			
			this.locations.add(new NamedLocation(name,location));
		}
	}
	
	public List<NamedLocation> getLocations(){return this.locations;}
	
	public void add(NamedLocation location)
	{
		this.locations.add(location);
	}
	
	public NamedLocation getLocation(String name)
	{
		for (NamedLocation location : this.locations)
		{
			if (location.getName()!=null && location.getName().equals(name))
				return location;
		}
		return null;
	}
	
	public List<NamedLocation> namedlocations(String identifier)
	{
		List<NamedLocation> locations=new ArrayList<NamedLocation>();
		for (NamedLocation location : this.locations)
		{
			if (location.getName()!=null && location.getName().equals(identifier))
				locations.add(location);
		}
		return locations;
	}

	public List<ScoredSubLocation> getSortedSublocations()
	{
		List<ScoredSubLocation> sublocations=new ArrayList<ScoredSubLocation>();
		for (NamedLocation location : this.locations)
		{
			for (SimpleLocation.SubLocation sublocation : location.getSublocations())
			{
				if (sublocation instanceof ScoredSubLocation)
					sublocations.add((ScoredSubLocation)sublocation);
			}
		}
		Collections.sort(sublocations,new ScoredSubLocationComparator());
		return sublocations;
	}
	
	public List<String> getArchitecture()
	{
		List<String> names=new ArrayList<String>();
		for (CompoundLocation.NamedLocation namedlocation : this.locations)
		{
			names.add(namedlocation.getName());
		}
		return names;//CStringHelper.join(names,";");
	}
	
	public String toString()
	{
		List<String> parts=new ArrayList<String>();
		for (NamedLocation location : this.locations)
		{
			parts.add(location.toString());
		}
		return CStringHelper.join(parts,";");
	}

	public static class NamedLocation extends SimpleLocation
	{
		private String name;
		
		public NamedLocation(){}
		
		public NamedLocation(String name)
		{
			this.name=name;
		}
		
		public NamedLocation(String name, String location)
		{
			this(name);
			for (String sublocation : CStringHelper.split(location,","))
			{
				String comments=null;
				int index=sublocation.indexOf('[');
				if (index!=-1)
				{
					comments=sublocation.substring(index+1,sublocation.indexOf(']'));
					sublocation=sublocation.substring(0,index);
				}
				index=sublocation.indexOf("..");
				if (index==-1)
					throw new CException("can't parse sublocation: ["+sublocation+"], location=["+location+"]");
				int start=Integer.parseInt(sublocation.substring(0,index));
				int end=Integer.parseInt(sublocation.substring(index+2));
				//System.out.println("LOCATION.start=["+start+","+end+","+label+"]");
				if (comments!=null)
					add(start,end,comments);
				else add(start,end);
			}
		}

		public String getName(){return this.name;}
		public void setName(String name){this.name=name;}
		
		public void add(int start, int end, String comments)
		{
			add(new ScoredSubLocation(start,end,comments));
		}
		
		@Override
		public String toString()
		{
			String str=super.toString();
			if (CStringHelper.hasContent(this.name))
				return this.name+":"+str;
			else return str;
		}
	}
	
	public static class ScoredSubLocation extends SimpleLocation.SubLocation
	{
		private Double score;
		private Double evalue;
		
		public ScoredSubLocation(int start, int end)
		{
			super(start,end);
		}
		
		public ScoredSubLocation(int start, int end, String comments)
		{
			super(start,end);
			comments=comments.trim();
			if (CStringHelper.isEmpty(comments))
				return;
			String arr[]=comments.split("\\|");
			this.score=Double.parseDouble(arr[0]);
			this.evalue=Double.parseDouble(arr[1]);
		}
		
		public ScoredSubLocation(int start, int end, Double score, Double evalue)
		{
			super(start,end);
			this.score=score;
			this.evalue=evalue;
		}
		
		public Double getScore(){return this.score;}
		public void setScore(Double score){this.score=score;}

		public Double getEvalue(){return this.evalue;}
		public void setEvalue(Double evalue){this.evalue=evalue;}
		
		@Override
		public String toString()
		{
			String separator=(this.score!=null && this.evalue!=null) ? "|" : "";
			return super.toString()+"["+this.score+separator+CStringHelper.formatScientificNotation(this.evalue,1)+"]";
		}
	}
	
	@SuppressWarnings("serial")
	public static class ScoredSubLocationComparator implements Comparator<ScoredSubLocation>, Serializable
	{
		public int compare(ScoredSubLocation l1, ScoredSubLocation l2)
		{
			Double score1=l1.getScore();
			Double score2=l2.getScore();
			return score2.compareTo(score1);
		}
	}
}