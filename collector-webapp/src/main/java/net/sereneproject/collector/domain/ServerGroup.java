package net.sereneproject.collector.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findServerGroupsByNameEquals" })
@RooJson
public class ServerGroup {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String name;
}
