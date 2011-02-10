package org.vardb.tags;

import org.vardb.util.CAttributeList;
import org.vardb.util.CDataFrame;
import org.vardb.util.CDataType;
import org.vardb.util.CException;
import org.vardb.util.CTable;

public class CTagHelper
{
	public static String getTagType(String identifier)
	{
		int index=identifier.indexOf(CTagType.ATTRIBUTE_DELIMITER);
		return identifier.substring(0,index);
	}
	
	public static String getName(String identifier)
	{
		int index=identifier.indexOf(CTagType.ATTRIBUTE_DELIMITER);
		return identifier.substring(index+1);
	}
	
	public static String normalizeName(String origname)
	{
		String name = origname.trim();
		StringBuilder buffer = new StringBuilder();
		for (char ch : name.toCharArray())
		{
			if (Character.isLetterOrDigit(ch))
				buffer.append(ch);
			else if (Character.isWhitespace(ch) || ch == '_')
				buffer.append("_");
			else if (ch == '.')
				buffer.append(ch);
		}
		name = buffer.toString();
		// collapse multiple underscores
		name = name.replaceAll("_+", "_");
		if (name.length() == 0)
			throw new CException("Normalized name is zero length: "
					+ origname);
		return name;
	}

	public static void normalizeNames(CTable table)
	{
		if (table.getHeader().getCells().size() > 1)
		{
			for (int index = 0; index < table.getHeader().getCells().size(); index++)
			{
				CTable.Cell cell = table.getHeader().getCell(index);
				cell.setValue(normalizeName(cell.getValue().toString()));
			}
		}
		for (CTable.Row row : table.getRows())
		{
			CTable.Cell cell = row.getCell(0);
			cell.setValue(normalizeName(cell.getValue().toString()));
		}
	}
	
	public static CAttributeList getAttributeList(CDataFrame dataframe)
	{
		CAttributeList attlist=new CAttributeList();
		for (Object tagname : dataframe.getRowNames())
		{
			for (String attname : dataframe.getColNames())
			{
				if (attname.equals("name"))
					continue;
				Object value=dataframe.getValue(attname,tagname);
				attlist.addAttribute(tagname.toString(), attname, value.toString());
			}
		}
		return attlist;
	}
	
	public static CAttributeList getAttributeTypeList(CDataFrame dataframe)
	{
		CAttributeList atttypelist=new CAttributeList();
		for (Object attname : dataframe.getRowNames())
		{
			CDataType type=CDataType.valueOf(dataframe.getStringValue("type",attname));
			String description="";
			if (dataframe.hasColumn("description"))
				description=dataframe.getStringValue("description",attname);
			atttypelist.addAttributeType(attname.toString(),type,description);
		}
		return atttypelist;
	}

}