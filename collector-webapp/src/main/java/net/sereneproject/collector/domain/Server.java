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
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Store data about servers.
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooEntity(finders = { "findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals" })
public class Server {

    /** Hostname of the server. */
    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 50)
    private String hostname;

    /** UUID is split in 2 {@link Long} fields. */
    @NotNull
    @Column
    private Long uuidMostSigBits;

    /** UUID is split in 2 {@link Long} fields. */
    @NotNull
    @Column
    private Long uuidLeastSigBits;

    /**
     * The group to which this server belongs.
     * 
     * Groups are used for classification. For example you can group together
     * all servers running the same application, or have a group for all
     * database servers and another group for all web servers.
     */
    @NotNull
    @ManyToOne
    private ServerGroup serverGroup;

    /**
     * Set the UUID of the server from its String representation.
     * 
     * @param uuid
     *            must conform to specifications from {@link UUID#toString()}.
     */
    public final void setUuid(final String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    /**
     * Set the UUID of the server.
     * 
     * @param uuid
     *            the UUID to set
     */
    public final void setUuid(final UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    /**
     * Get the UUID of the server.
     * 
     * @return this server's UUID
     */
    public final UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    /**
     * Find a server by it's UUID.
     * 
     * @param uuid
     *            the String representation of the UUID to look for, must
     *            conform to specifications from {@link UUID#toString()}.
     * @return the server
     * @throws EmptyResultDataAccessException
     *             in case the probe is not found
     */
    public static Server findServerByUuidEquals(final String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return findServerByUuidEquals(UUID.fromString(uuid));
    }

    /**
     * Find a server by it's UUID.
     * 
     * @param uuid
     *            the UUID to look for.
     * @return the server
     * @throws EmptyResultDataAccessException
     *             in case the probe is not found
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
