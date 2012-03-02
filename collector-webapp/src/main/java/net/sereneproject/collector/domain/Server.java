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

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * A server.
 * 
 * This object is used mainly for logical grouping and better user interface.
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals" })
@RooJson
public class Server {

    /** The name of the server. */
    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 50)
    private String hostname;

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
     * The ServerGroup to which this server belongs.
     */
    @NotNull
    @ManyToOne
    private ServerGroup serverGroup;

    /**
     * Set the {@link UUID} of this server from its {@link String}
     * representation.
     * 
     * @param uuid
     *            the {@link String} representation of the {@link UUID}
     */
    public final void setUuid(final String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    /**
     * Set the {@link UUID} of a server.
     * 
     * @param uuid
     *            the {@link UUID} to set
     */
    public final void setUuid(final UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    /**
     * Get the {@link UUID} of the server.
     * 
     * @return the {@link UUID} of the server
     */
    public final UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    /**
     * Find a server by its UUID.
     * 
     * @param uuid
     *            the {@link String} representation of the {@link UUID}
     * @return the server
     */
    public static Server findServerByUuidEquals(final String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return findServerByUuidEquals(UUID.fromString(uuid));
    }

    /**
     * Find a server by its UUID.
     * 
     * @param uuid
     *            the {@link UUID}
     * @return the server
     */
    public static Server findServerByUuidEquals(final UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return Server
                .findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(
                        uuid.getMostSignificantBits(),
                        uuid.getLeastSignificantBits()).getSingleResult();
    }
}
