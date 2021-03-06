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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = {
        "findProbesByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals",
        "findProbesByNode" })
public class Probe {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Column
    private Long uuidMostSigBits;

    @NotNull
    @Column
    private Long uuidLeastSigBits;

    @NotNull
    @OneToMany
    private Set<Plugin> plugins = new HashSet<Plugin>();

    @Nullable
    @ManyToOne
    private Node node;

    @Lob
    private byte[] rrd;

    public final void setUuid(String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    public final void setUuid(UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    public final UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    public static Probe findProbeByUuidEquals(String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return findProbeByUuidEquals(UUID.fromString(uuid));
    }

    public static TypedQuery<Probe> findProbesByNodeIsNull() {
        EntityManager em = Probe.entityManager();
        TypedQuery<Probe> q = em.createQuery(
                "SELECT o FROM Probe AS o WHERE o.node is null", Probe.class);
        return q;
    }

    public static Probe findProbeByUuidEquals(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return Probe
                .findProbesByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(
                        uuid.getMostSignificantBits(),
                        uuid.getLeastSignificantBits()).getSingleResult();
    }

    public static boolean exists(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        TypedQuery<Long> q = entityManager().createQuery(
                "SELECT COUNT(o) FROM Probe o"
                        + "  WHERE o.uuidMostSigBits = :uuidMostSigBits"
                        + "    AND o.uuidLeastSigBits = :uuidLeastSigBits",
                Long.class);
        q.setParameter("uuidMostSigBits", uuid.getMostSignificantBits());
        q.setParameter("uuidLeastSigBits", uuid.getLeastSignificantBits());
        Long count = q.getSingleResult();
        if (count.equals(0)) {
            return false;
        } else if (count.equals(1)) {
            return true;
        } else {
            throw new DataIntegrityViolationException(
                    "There is more than one probe with UUID [" + uuid + "].");
        }
    }
}
