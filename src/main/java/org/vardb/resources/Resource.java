package org.vardb.resources;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity
public abstract class Resource {

    @NotNull
    private String identifier;

    @NotNull
    private String name;

    @NotNull
    private String description;
}
