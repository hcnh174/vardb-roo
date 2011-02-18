package org.vardb.weka;

import org.vardb.util.CException;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaHelper
{
	 public static void test(String filename)
	 {
		 try
		 {
			 DataSource source = new DataSource(filename);
			 Instances data = source.getDataSet();
			 // setting class attribute if the data format does not provide this information
			 // E.g., the XRFF format saves the class attribute information as well
			 if (data.classIndex() == -1)
			   data.setClassIndex(data.numAttributes() - 1);
		 }
		 catch(Exception e)
		 {
			 throw new CException(e);
		 }
	 }
}