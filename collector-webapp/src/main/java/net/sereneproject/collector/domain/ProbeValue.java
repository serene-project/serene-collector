package net.sereneproject.collector.domain;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * The probe values.
 * 
 * An new {@link ProbeValue} is created each time a sample is received.
 * 
 * @author gehel
 * 
 */
@RooJavaBean
@RooToString
@RooEntity
public class ProbeValue {

    /**
     * The value of the probe.
     * 
     * The datatype will probably be modified to be more flexible.
     */
    @NotNull
    private Long value;

    /** Date the sample was received. */
    @NotNull
    private Date date;

    /** Probe owning this sample. */
    @NotNull
    @ManyToOne
    private Probe probe;

}
