package org.vardb.setup;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.vardb.util.CMessageWriter;

public class CLoadTableParams
{
	protected boolean overwrite=true;
	protected Date date=null;
	protected Map<String,String> excluded=new HashMap<String,String>();
	protected CMessageWriter writer=new CMessageWriter();
	
	public boolean getOverwrite(){return this.overwrite;}
	public void setOverwrite(final boolean overwrite){this.overwrite=overwrite;}

	public Date getDate(){return this.date;}
	public void setDate(final Date date){this.date=date;}
	
	public Map<String,String> getExcluded(){return this.excluded;}
	public void setExcluded(final Map<String,String> excluded){this.excluded=excluded;}

	public CMessageWriter getWriter(){return this.writer;}
	public void setWriter(final CMessageWriter writer){this.writer=writer;}
	
	public CLoadTableParams(){}
	
	public CLoadTableParams(CMessageWriter writer){this.writer=writer;}
}