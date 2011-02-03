package org.vardb.resources;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.Set;
import org.vardb.sequences.Sequence;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Pathogen extends AbstractResource {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "family")
    private Set<Sequence> sequences = new HashSet<Sequence>();
}
