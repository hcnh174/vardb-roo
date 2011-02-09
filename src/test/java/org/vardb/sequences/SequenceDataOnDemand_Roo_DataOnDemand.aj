// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.sequences;

import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;
import org.vardb.sequences.Sequence;

privileged aspect SequenceDataOnDemand_Roo_DataOnDemand {
    
    declare @type: SequenceDataOnDemand: @Component;
    
    private Random SequenceDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<Sequence> SequenceDataOnDemand.data;
    
    public Sequence SequenceDataOnDemand.getNewTransientSequence(int index) {
        org.vardb.sequences.Sequence obj = new org.vardb.sequences.Sequence();
        obj.setIdentifier("identifier_" + index);
        obj.setAccession("accession_" + index);
        obj.setGenome("genome_" + index);
        obj.setStrain("strain_" + index);
        obj.setTaxid("taxid_" + index);
        obj.setSource("source_" + index);
        obj.setChromosome("chromosome_" + index);
        obj.setSequence(null);
        obj.setCds(null);
        obj.setTranslation(null);
        obj.setStart("start_" + index);
        obj.setEnd(null);
        obj.setStrand("strand_" + index);
        obj.setNumexons("numexons_" + index);
        obj.setSplicing("splicing_" + index);
        obj.setPseudogene("pseudogene_" + index);
        obj.setMethod("method_" + index);
        obj.setModel("model_" + index);
        obj.setScore("score_" + index);
        obj.setEvalue("evalue_" + index);
        obj.setHmmloc("hmmloc_" + index);
        obj.setDescription("description_" + index);
        obj.setDisease(null);
        obj.setPathogen(null);
        obj.setFamily(null);
        return obj;
    }
    
    public Sequence SequenceDataOnDemand.getSpecificSequence(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Sequence obj = data.get(index);
        return Sequence.findSequence(obj.getId());
    }
    
    public Sequence SequenceDataOnDemand.getRandomSequence() {
        init();
        Sequence obj = data.get(rnd.nextInt(data.size()));
        return Sequence.findSequence(obj.getId());
    }
    
    public boolean SequenceDataOnDemand.modifySequence(Sequence obj) {
        return false;
    }
    
    public void SequenceDataOnDemand.init() {
        data = org.vardb.sequences.Sequence.findSequenceEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Sequence' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<org.vardb.sequences.Sequence>();
        for (int i = 0; i < 10; i++) {
            org.vardb.sequences.Sequence obj = getNewTransientSequence(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
