package org.vardb.users;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "username", identifierType = String.class, table = "users")
public class User {

    @NotNull
    private String password;

    private Boolean enabled;

    private String firstname;

    private String lastname;

    private String email;

    private String affiliation;
}
