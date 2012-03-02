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

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * A specific "thing" we take measurements from.
 * 
 * For example a {@link Probe} can be the disk IO, or memory consumed, or ...
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findProbesByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals" })
@RooJson
public class Probe {

    /** Name of the {@link Probe}. */
    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String name;

    /**
     * {@link UUID} is stored in 2 longs, this is the most significant bits.
     */
    @NotNull
    @Column
    private Long uuidMostSigBits;

    /**
     * {@link UUID} is stored in 2 longs, this is the least significant bits.
     */
    @NotNull
    @Column
    private Long uuidLeastSigBits;

    /**
     * {@link Server} to which this {@link Probe} belongs.
     * 
     * {@link Probe}s from a specific server are grouped together.
     */
    @NotNull
    @ManyToOne
    private Server server;

    /** List of analyzer {@link Plugin}s used whith this probe. */
    @NotNull
    @OneToMany
    private Set<Plugin> plugins = new HashSet<Plugin>();

    /**
     * Set the {@link UUID} of the {@link Probe} from its {@link String}
     * representation.
     * 
     * @param uuid
     *            the {@link String} representation of the UUID
     */
    public final void setUuid(final String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    /**
     * Set the {@link UUID} of the {@link Probe}.
     * 
     * @param uuid
     *            the {@link UUID} to set
     */
    public final void setUuid(final UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    /**
     * Get the {@link UUID} of the {@link Probe}.
     * 
     * @return the {@link UUID}
     */
    public final UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    /**
     * Find a {@link Probe} by its {@link UUID}.
     * 
     * @param uuid
     *            the {@link String} representation of the {@link UUID} to look
     *            for
     * @return the {@link Probe}
     */
    public static Probe findProbeByUuidEquals(final String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return findProbeByUuidEquals(UUID.fromString(uuid));
    }

    /**
     * Find a {@link Probe} by its {@link UUID}.
     * 
     * @param uuid
     *            the {@link UUID} to look for
     * @return the {@link Probe}
     */
    public static Probe findProbeByUuidEquals(final UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return Probe
                .findProbesByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(
                        uuid.getMostSignificantBits(),
                        uuid.getLeastSignificantBits()).getSingleResult();
    }
}
