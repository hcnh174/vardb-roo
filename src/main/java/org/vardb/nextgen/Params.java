package org.vardb.nextgen;

import java.util.List;

import org.vardb.util.CFileHelper;

import com.google.common.collect.Lists;

public class Params
{
	protected String dir="c:/projects/analysis/nextgen/data/";
	protected String outdir="c:/Documents and Settings/All Users/Documents/nextgen/";//"c:/temp/nextgen/";
	protected String tempdir="c:/temp/";
	protected String sampleRegex="sample[0-9]+";
	
	protected String qseq2fastq_path="c:/research/software/bin/qseq2fastq.pl";
	protected String fastq_btrim_path="c:/research/software/bin/fastq_btrim.pl";
	
	//velvet
	protected int hash_length=25;
}
