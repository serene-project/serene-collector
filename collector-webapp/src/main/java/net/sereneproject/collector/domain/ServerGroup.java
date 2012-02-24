package net.sereneproject.collector.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findServerGroupsByNameEquals" })
public class ServerGroup {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String name;
}
