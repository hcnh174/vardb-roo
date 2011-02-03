package org.vardb.sequences;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import org.vardb.resources.Pathogen;
import javax.persistence.ManyToOne;
import org.vardb.resources.Family;

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
    private Pathogen pathogen;

    @ManyToOne
    private Family family;
}
