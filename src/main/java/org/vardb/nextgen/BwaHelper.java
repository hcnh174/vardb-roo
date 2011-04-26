package org.vardb.nextgen;

import java.util.List;

// index, aln, samse
public class BwaHelper
{
	public static void index(Params params, List<String> samples, String ref_file)
	{
		for (String sample : samples)
		{
			index(params, sample);
		}
	}
	
	public static void index(Params params, String samples)
	{
		
	}
}
