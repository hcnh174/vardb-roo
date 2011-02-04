package org.vardb.sequences;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.vardb.resources.Disease;
import org.vardb.resources.Family;
import org.vardb.resources.Pathogen;

@RooJavaBean
@RooToString
@RooEntity
public class Sequence {

    @NotNull
    private String identifier;

    @NotNull
    private String accession;

    @NotNull
    private String sequence;

    @ManyToOne
    private Disease disease;
    
    @ManyToOne
    private Pathogen pathogen;

    @ManyToOne
    private Family family;
}
