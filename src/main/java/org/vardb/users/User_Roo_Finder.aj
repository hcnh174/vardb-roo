// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vardb.users;

import java.lang.String;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.vardb.users.User;

privileged aspect User_Roo_Finder {
    
    public static TypedQuery<User> User.findUsersByUsername(String username) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        EntityManager em = User.entityManager();
        TypedQuery<User> q = em.createQuery("SELECT User FROM User AS user WHERE user.username = :username", User.class);
        q.setParameter("username", username);
        return q;
    }
    
}
