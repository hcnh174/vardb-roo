package org.vardb.bio;

public enum SequenceType
{	
	NT("sequence.sequence"),
	AA("sequence.translation");
	
	protected String field;
	
	SequenceType(String field)
	{
		this.field=field;
	}
	
	public String getField(){return this.field;}
	
	public boolean isProtein()
	{
		return this==AA;
	}
}
