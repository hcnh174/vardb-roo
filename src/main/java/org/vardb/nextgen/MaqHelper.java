package org.vardb.nextgen;

import java.util.List;

public class MaqHelper
{
	public static String easyrun(Params params, String ref, List<String> samples)
	{
		StringBuilder buffer=new StringBuilder();
		for (String sample : samples)
		{
			String cmd=easyrun(params, ref, sample);
			buffer.append(cmd).append("\n");
		}
		return buffer.toString();
	}
	
	public static String easyrun(Params params, String ref, String sample)
	{
		String cmd="maq.pl easyrun -d "+sample+" "+ref+" "+sample+".fq";
		System.out.println(cmd);
		return cmd;
	}
}
