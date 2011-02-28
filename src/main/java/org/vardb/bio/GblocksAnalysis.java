package org.vardb.bio;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.vardb.util.CBeanHelper;

//@Entity
//@DiscriminatorValue("GBLOCKS")
public class GblocksAnalysis //extends CAbstractAnalysis
{	
	protected Params params=new Params();
	protected Results results=new Results();
	
	public GblocksAnalysis() {}

	public GblocksAnalysis(Map<String,String> sequences, GBlocksWrapper.Params params)
	{
		super();
		CBeanHelper helper=new CBeanHelper();
		helper.copyProperties(this.params,params); // hack! todo: make sure that enum properties are set with this!
		this.params.setSequences(SequenceFileParser.writeFasta(sequences));
	}
	
	//@Embedded
	public Params getParams(){return this.params;}
	public void setParams(final Params params){this.params=params;}

	//@Embedded
	public Results getResults(){return this.results;}
	public void setResults(final Results results){this.results=results;}
	
	//@Embeddable
	//@SuppressWarnings("serial")
	public static class Params extends GBlocksWrapper.Params
	{
		protected String sequences;
		
		@Enumerated(EnumType.STRING)
		@Column(name="gblocks_params_type")
		public GBlocksWrapper.SequenceTypeEnum getType(){return this.type;}
		public void setType(final GBlocksWrapper.SequenceTypeEnum type){this.type=type;}
	
		@Enumerated(EnumType.STRING)
		@Column(name="gblocks_params_gaps")
		public GBlocksWrapper.GapPositions getGaps(){return this.gaps;}
		public void setGaps(final GBlocksWrapper.GapPositions gaps){this.gaps=gaps;}
	
		@Column(name="gblocks_params_minblock")
		public int getMinBlock(){return this.minBlock;}
		public void setMinBlock(final int minBlock){this.minBlock=minBlock;}
	
		@Column(name="gblocks_params_similaritymatrices")
		public boolean getSimilarityMatrices(){return this.similarityMatrices;}
		public void setSimilarityMatrices(final boolean similarityMatrices){this.similarityMatrices=similarityMatrices;}
	
		@Column(name="gblocks_params_minconserved")
		public float getMinConserved(){return this.minConserved;}
		public void setMinConserved(final float minConserved){this.minConserved=minConserved;}
	
		@Column(name="gblocks_params_minflanking")
		public float getMinFlanking(){return this.minFlanking;}
		public void setMinFlanking(final float minFlanking){this.minFlanking=minFlanking;}
	
		@Column(name="gblocks_params_maxnonconserved")
		public int getMaxNonconserved(){return this.maxNonconserved;}
		public void setMaxNonconserved(final int maxNonconserved){this.maxNonconserved=maxNonconserved;}
	
		@Column(name="gblocks_params_sequences")
		public String getSequences(){return this.sequences;}
		public void setSequences(final String sequences){this.sequences=sequences;}
	}
	
	@Embeddable
	public static class Results
	{
		protected String html;
		protected String location;
		protected String applied;
		
		@Column(name="gblocks_results_html")
		public String getHtml(){return this.html;}
		public void setHtml(final String html){this.html=html;}
		
		@Column(name="gblocks_results_location")
		public String getLocation(){return this.location;}
		public void setLocation(final String location){this.location=location;}
		
		@Column(name="gblocks_results_applied")
		public String getApplied(){return this.applied;}
		public void setApplied(final String applied){this.applied=applied;}
	}
	
	@Transient
	public SimpleLocation getLocation()
	{
		return new SimpleLocation(getResults().getLocation());
	}
}
