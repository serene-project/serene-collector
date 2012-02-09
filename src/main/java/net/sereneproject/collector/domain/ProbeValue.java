package net.sereneproject.collector.domain;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class ProbeValue {

    @NotNull
    private Long value;

    @NotNull
    private Date date;

    @NotNull
    @ManyToOne
    private Probe probe;

}
