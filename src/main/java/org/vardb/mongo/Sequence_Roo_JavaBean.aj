// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.mongo;

import java.lang.Integer;
import java.lang.String;
import java.util.List;

privileged aspect Sequence_Roo_JavaBean {
    
    public String Sequence.getAccession() {
        return this.accession;
    }
    
    public void Sequence.setAccession(String accession) {
        this.accession = accession;
    }
    
    public String Sequence.getSequence() {
        return this.sequence;
    }
    
    public void Sequence.setSequence(String sequence) {
        this.sequence = sequence;
    }
    
    public Integer Sequence.getNtlength() {
        return this.ntlength;
    }
    
    public void Sequence.setNtlength(Integer ntlength) {
        this.ntlength = ntlength;
    }
    
    public List<Feature> Sequence.getFeatures() {
        return this.features;
    }
    
    public void Sequence.setFeatures(List<Feature> features) {
        this.features = features;
    }
    
}