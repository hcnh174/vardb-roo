package org.vardb.setup;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import org.vardb.sequences.Sequence;

public class SequenceFieldSetMapper implements FieldSetMapper<Sequence>
{
	public Sequence mapFieldSet(FieldSet fieldSet) throws BindException {
		Sequence sequence = new Sequence();
		sequence.setIdentifier(fieldSet.readString("SEQUENCE"));
		sequence.setAccession(fieldSet.readString("SEQUENCE"));
		//sequence.setFamily(fieldSet.readString("family"));		
		sequence.setGenome(fieldSet.readString("genome"));
		sequence.setStrain(fieldSet.readString("strain"));
		sequence.setTaxid(fieldSet.readString("taxid"));
		sequence.setSource(fieldSet.readString("source"));
		sequence.setChromosome(fieldSet.readString("chromosome"));
		sequence.setSequence(fieldSet.readString("sequence"));
		sequence.setCds(fieldSet.readString("cds"));
		sequence.setTranslation(fieldSet.readString("translation"));
		sequence.setStart(fieldSet.readString("start"));
		sequence.setEnd(fieldSet.readString("end"));
		sequence.setStrand(fieldSet.readString("strand"));
		sequence.setNumexons(fieldSet.readString("numexons"));
		sequence.setSplicing(fieldSet.readString("splicing"));
		sequence.setPseudogene(fieldSet.readString("pseudogene"));
		sequence.setMethod(fieldSet.readString("method"));
		sequence.setModel(fieldSet.readString("model"));
		sequence.setScore(fieldSet.readString("score"));
		sequence.setEvalue(fieldSet.readString("evalue"));
		sequence.setHmmloc(fieldSet.readString("hmmloc"));
		sequence.setDescription(fieldSet.readString("description"));	
		return sequence;
	}
}
