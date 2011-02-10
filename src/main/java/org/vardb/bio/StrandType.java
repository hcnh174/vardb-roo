package org.vardb.bio;

public enum StrandType
{
	forward("+","1"),
	reverse("-","-1");
	
	protected String sign;
	protected String direction;
	
	StrandType(final String sign, final String direction)
	{
		this.sign=sign;
		this.direction=direction;
	}		
	
	public String getSign() {return this.sign;}
	public String getDirection(){return this.direction;}
	
	public static StrandType parse(final String str)
	{
		for (StrandType strand : StrandType.values())
		{
			if (str.equals(strand.getSign()) || str.equals(strand.name()) || str.equals(strand.getDirection()))
				return strand;
		}
		return null;
	}
}
