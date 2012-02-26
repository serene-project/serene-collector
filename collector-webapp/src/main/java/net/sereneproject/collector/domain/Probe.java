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

import static net.sereneproject.collector.domain.DomainConstants.NAME_MAX_SIZE;
import static net.sereneproject.collector.domain.DomainConstants.NAME_MIN_SIZE;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Store data about Probes.
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooEntity(finders = { "findProbesByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals" })
public class Probe {

    /** Human readable probe name. */
    @NotNull
    @Column(unique = true)
    @Size(min = NAME_MIN_SIZE, max = NAME_MAX_SIZE)
    private String name;

    /** UUID is split in 2 {@link Long} fields. */
    @NotNull
    @Column
    private Long uuidMostSigBits;

    /** UUID is split in 2 {@link Long} fields. */
    @NotNull
    @Column
    private Long uuidLeastSigBits;

    /**
     * Saved state from the analyzer.
     * 
     * @todo: This needs to be refactored to support multiple analyzers per
     *        probe.
     */
    private String savedState;

    /** Server to which this probe belongs. */
    @NotNull
    @ManyToOne
    private Server server;
    
    /** Plugins to use with this probe. */
    @NotNull
    @OneToMany
    private Set<Plugin> plugins = new HashSet<Plugin>();

    /**
     * Set the UUID of the probe from its String representation.
     * 
     * @param uuid
     *            must conform to specifications from {@link UUID#toString()}.
     */
    public final void setUuid(final String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    /**
     * Set the UUID of the probe.
     * 
     * @param uuid
     *            the UUID to set
     */
    public final void setUuid(final UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    /**
     * Get the UUID of the probe.
     * 
     * @return this probe's UUID
     */
    public final UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    /**
     * Find a probe by it's UUID.
     * 
     * @param uuid
     *            the String representation of the UUID to look for, must
     *            conform to specifications from {@link UUID#toString()}.
     * @return the probe
     * @throws EmptyResultDataAccessException
     *             in case the probe is not found
     */
    public static Probe findProbeByUuidEquals(final String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        // TODO : document exception thrown when UUID does not conform to spec
        return findProbeByUuidEquals(UUID.fromString(uuid));
    }

    /**
     * Find a probe by it's UUID.
     * 
     * @param uuid
     *            the UUID to look for.
     * @return the probe
     * @throws EmptyResultDataAccessException
     *             in case the probe is not found
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
