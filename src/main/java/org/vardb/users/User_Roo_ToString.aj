// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.users;

import java.lang.String;

privileged aspect User_Roo_ToString {
    
    public String User.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Enabled: ").append(isEnabled()).append(", ");
        sb.append("AccountNonExpired: ").append(isAccountNonExpired()).append(", ");
        sb.append("AccountNonLocked: ").append(isAccountNonLocked()).append(", ");
        sb.append("CredentialsNonExpired: ").append(isCredentialsNonExpired()).append(", ");
        sb.append("Roles: ").append(getRoles() == null ? "null" : getRoles().size()).append(", ");
        sb.append("Authorities: ").append(getAuthorities() == null ? "null" : getAuthorities().size()).append(", ");
        sb.append("Username: ").append(getUsername()).append(", ");
        sb.append("Password: ").append(getPassword()).append(", ");
        sb.append("Administrator: ").append(getAdministrator()).append(", ");
        sb.append("Firstname: ").append(getFirstname()).append(", ");
        sb.append("Lastname: ").append(getLastname()).append(", ");
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("Affiliation: ").append(getAffiliation()).append(", ");
        sb.append("Created: ").append(getCreated()).append(", ");
        sb.append("Updated: ").append(getUpdated());
        return sb.toString();
    }
    
}
