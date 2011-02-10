package org.vardb.bio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vardb.util.CStringHelper;

public class ClustalMultipleAlignmentParser
{
	public Map<String,String> parse(String temp)
	{
		//System.out.println("parsing ="+temp);
		// parse text
		List<String> list=CStringHelper.splitAsList(temp,"\n");
		Map<String,List<String>> map=new LinkedHashMap<String,List<String>>();
		// skip the first line
		for(int index=1;index<list.size();index++)
		{
			String line=(String)list.get(index);
			line=line.trim();
			if (!isSequence(line))
			{
				//System.out.println("not a sequence line, skipping: "+line);
				continue;
			}
			//System.out.println("sequence line, keeping: "+line);
			int i=line.indexOf(' ');
			String name=SequenceFileParser.getName(line.substring(0,i));
			String seq=clean(line.substring(i+1));
			//System.out.println("aligned name="+sequenceid);
			//System.out.println("aligned sequenceid="+sequenceid+", seq="+seq);			
			List<String> seqs=(List<String>)map.get(name);
			if (seqs==null)
			{
				seqs=new ArrayList<String>();
				map.put(name,seqs);
			}
			seqs.add(seq);
		}
		
		Map<String,String> sequences=new LinkedHashMap<String,String>();		
		// accumulate fragments
		for (String name : map.keySet())
		{
			List<String> seqs=(List<String>)map.get(name);
			StringBuilder buffer=new StringBuilder();
			for (String seq : seqs)
			{
				buffer.append(seq);
			}
			String aligned=buffer.toString();
			//System.out.println("alignment parser: "+name+"="+aligned);
			sequences.put(name,aligned);
		}
		return sequences;
	}
	
	private boolean isSequence(String line)
	{
		if (CStringHelper.isEmpty(line))
			return false;
		char ch=line.charAt(0);
		if (Character.isWhitespace(ch))
			return false;
		if (Character.isLetterOrDigit(ch))
			return true;
		return false;
	}
	
	//if there is a number at the end, strip it out
	protected String clean(String str)
	{
		String seq=str.trim();
		if (seq.contains(" "))
			seq=seq.substring(0,seq.indexOf(' ')).trim();
		return seq;
	}
}
