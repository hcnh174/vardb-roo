// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.sequences;

import java.lang.String;

privileged aspect Sequence_Roo_ToString {
    
    public String Sequence.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Identifier: ").append(getIdentifier()).append(", ");
        sb.append("Accession: ").append(getAccession()).append(", ");
        sb.append("Genome: ").append(getGenome()).append(", ");
        sb.append("Strain: ").append(getStrain()).append(", ");
        sb.append("Taxid: ").append(getTaxid()).append(", ");
        sb.append("Source: ").append(getSource()).append(", ");
        sb.append("Chromosome: ").append(getChromosome()).append(", ");
        sb.append("Sequence: ").append(getSequence()).append(", ");
        sb.append("Cds: ").append(getCds()).append(", ");
        sb.append("Translation: ").append(getTranslation()).append(", ");
        sb.append("Start: ").append(getStart()).append(", ");
        sb.append("End: ").append(getEnd()).append(", ");
        sb.append("Strand: ").append(getStrand()).append(", ");
        sb.append("Numexons: ").append(getNumexons()).append(", ");
        sb.append("Splicing: ").append(getSplicing()).append(", ");
        sb.append("Pseudogene: ").append(getPseudogene()).append(", ");
        sb.append("Method: ").append(getMethod()).append(", ");
        sb.append("Model: ").append(getModel()).append(", ");
        sb.append("Score: ").append(getScore()).append(", ");
        sb.append("Evalue: ").append(getEvalue()).append(", ");
        sb.append("Hmmloc: ").append(getHmmloc()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Disease: ").append(getDisease()).append(", ");
        sb.append("Pathogen: ").append(getPathogen()).append(", ");
        sb.append("Family: ").append(getFamily());
        return sb.toString();
    }
    
}
