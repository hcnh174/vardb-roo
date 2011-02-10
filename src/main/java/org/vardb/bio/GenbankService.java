package org.vardb.bio;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.vardb.util.CMessageWriter;

@Transactional
public interface GenbankService
{
	enum EntrezDatabase{taxonomy,protein,nucleotide,pubmed,pmc,genome,gene,genomeprj;}

	final String GENBANK_SUFFIX=".gbk";
	final String GENPEPT_SUFFIX=".gpt";
	
	void downloadGenbankEntries(Collection<String> ids, EntrezDatabase database, String filename, int batchsize, CMessageWriter writer);
	Collection<Taxon> getTaxa(List<Integer> ids, CMessageWriter writer);
	Map<Integer,Ref> getRefs(List<Integer> ids, CMessageWriter writer);
}
