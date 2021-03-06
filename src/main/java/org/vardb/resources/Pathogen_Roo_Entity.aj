// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.resources;

import java.lang.Long;
import java.util.List;
import javax.persistence.Entity;
import org.vardb.resources.Pathogen;

privileged aspect Pathogen_Roo_Entity {
    
    declare @type: Pathogen: @Entity;
    
    public static long Pathogen.countPathogens() {
        return entityManager().createQuery("select count(o) from Pathogen o", Long.class).getSingleResult();
    }
    
    public static List<Pathogen> Pathogen.findAllPathogens() {
        return entityManager().createQuery("select o from Pathogen o", Pathogen.class).getResultList();
    }
    
    public static Pathogen Pathogen.findPathogen(Long id) {
        if (id == null) return null;
        return entityManager().find(Pathogen.class, id);
    }
    
    public static List<Pathogen> Pathogen.findPathogenEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from Pathogen o", Pathogen.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
