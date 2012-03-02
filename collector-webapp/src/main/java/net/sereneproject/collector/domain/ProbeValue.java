/**
 * Copyright (c) 2012, Serene Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sereneproject.collector.domain;

import java.util.Date;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * The value of a probe at a specific date.
 * 
 * Each probe contains a list of {@link ProbeValue}.
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findProbeValuesByProbe" })
@RooJson
public class ProbeValue {

    /** Value of the probe. */
    @NotNull
    private Double value;

    /** Date at which the sample was measured. */
    @NotNull
    @DateTimeFormat(pattern = "yyyy.MM.dd HH.mm.ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    /** The {@link Probe} to which this value belongs. */
    @NotNull
    @ManyToOne
    private Probe probe;

    /**
     * Find all values for a {@link Probe}.
     * 
     * @param uuid
     *            the {@link UUID} of the {@link Probe}
     * @return the list of values
     */
    public static TypedQuery<ProbeValue> findProbeValuesByProbeUUID(
            final UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        EntityManager em = ProbeValue.entityManager();
        TypedQuery<ProbeValue> q = em.createQuery(
                "SELECT o FROM ProbeValue AS o JOIN o.probe AS p "
                        + "  WHERE p.uuidMostSigBits = :uuidMostSigBits "
                        + "    AND p.uuidLeastSigBits = :uuidLeastSigBits",
                ProbeValue.class);
        q.setParameter("uuidMostSigBits", uuid.getMostSignificantBits());
        q.setParameter("uuidLeastSigBits", uuid.getLeastSignificantBits());
        return q;
    }
}
