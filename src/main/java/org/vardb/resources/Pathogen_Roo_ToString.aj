// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.resources;

import java.lang.String;

privileged aspect Pathogen_Roo_ToString {
    
    public String Pathogen.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Identifier: ").append(getIdentifier()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Sequences: ").append(getSequences() == null ? "null" : getSequences().size());
        return sb.toString();
    }
    
}
