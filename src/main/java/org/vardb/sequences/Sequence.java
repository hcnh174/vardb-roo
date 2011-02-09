package org.vardb.sequences;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.vardb.resources.Disease;
import org.vardb.resources.Family;
import org.vardb.resources.Pathogen;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierType = String.class, table = "sequences")
public class Sequence {

    @NotNull private String accession;    
    private String genome;
    private String strain;
    private String taxid;
    private String source;
    private String chromosome;
    @Column(name="seq", columnDefinition="TEXT") private String sequence;
    @Column(columnDefinition="TEXT")  private String cds;
    @Column(columnDefinition="TEXT") private String translation;
    private String start;
    @Column(name = "finish") private String end;
    private String strand;
    private String numexons;
    private String splicing;
    private String pseudogene;
    private String method;
    private String model;
    private String score;
    private String evalue;
    private String hmmloc;
    private String description;
    private Integer domainnum;
    private Integer totaldomainnum;
    @Column(columnDefinition="TEXT") private String domains;

    @ManyToOne
    private Disease disease;
    
    @ManyToOne
    private Pathogen pathogen;

    @ManyToOne
    private Family family;
}
