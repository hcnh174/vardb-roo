// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.resources;

import java.lang.String;
import java.util.Date;

privileged aspect Comment_Roo_JavaBean {
    
    public String Comment.getUsername() {
        return this.username;
    }
    
    public void Comment.setUsername(String username) {
        this.username = username;
    }
    
    public String Comment.getType() {
        return this.type;
    }
    
    public void Comment.setType(String type) {
        this.type = type;
    }
    
    public String Comment.getIdentifier() {
        return this.identifier;
    }
    
    public void Comment.setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String Comment.getText() {
        return this.text;
    }
    
    public void Comment.setText(String text) {
        this.text = text;
    }
    
    public Date Comment.getDate() {
        return this.date;
    }
    
    public void Comment.setDate(Date date) {
        this.date = date;
    }
    
}
