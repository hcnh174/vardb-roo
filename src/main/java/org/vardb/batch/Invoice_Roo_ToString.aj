// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.batch;

import java.lang.String;

privileged aspect Invoice_Roo_ToString {
    
    public String Invoice.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("CustomerId: ").append(getCustomerId()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("IssueDate: ").append(getIssueDate()).append(", ");
        sb.append("Amount: ").append(getAmount());
        return sb.toString();
    }
    
}
