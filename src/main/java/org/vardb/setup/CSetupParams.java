package org.vardb.setup;

import org.kohsuke.args4j.Option;
import org.vardb.util.CFileHelper;
import org.vardb.util.CSpringHelper;

public class CSetupParams extends CAbstractSetupParams
{
	protected String sequenceDir;
	protected String domainDir;
	protected String othersequenceDir;
	protected String alignmentDir;
	protected String pdbDir;
	protected String bundleDir;
	
	@Option(name="-sequenceDir")
	public void setSequenceDir(final String sequenceDir){this.sequenceDir=sequenceDir;}
	public String getSequenceDir(){return this.sequenceDir;}
	
	@Option(name="-domainDir")
	public void setDomainDir(final String domainDir){this.domainDir=domainDir;}
	public String getDomainDir(){return this.domainDir;}
	
	@Option(name="-othersequenceDir")
	public void setOthersequenceDir(final String othersequenceDir){this.othersequenceDir=othersequenceDir;}
	public String getOthersequenceDir(){return this.othersequenceDir;}
	
	@Option(name="-alignmentDir")
	public void setAlignmentDir(final String alignmentDir){this.alignmentDir=alignmentDir;}
	public String getAlignmentDir(){return this.alignmentDir;}
	
	@Option(name="-pdbDir")
	public void setPdbDir(final String pdbDir){this.pdbDir=pdbDir;}
	public String getPdbDir(){return this.pdbDir;}
	
	@Option(name="-bundleDir")
	public void setBundleDir(final String bundleDir){this.bundleDir=bundleDir;}
	public String getBundleDir(){return this.bundleDir;}
	
	public CSetupParams(){}
	
	public CSetupParams(String[] argv)
	{
		super(argv);
	}	
	
	@Override
    public void validate()
	{
		super.validate();
		sequenceDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("sequenceDir",sequenceDir));
		domainDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("domainDir",domainDir));
		othersequenceDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("othersequenceDir",othersequenceDir));
		alignmentDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("alignmentDir",alignmentDir));
		pdbDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("pdbDir",pdbDir));
		bundleDir=CFileHelper.normalizeDirectory(CSpringHelper.checkResolvedProperty("bundleDir",bundleDir));
	}
}