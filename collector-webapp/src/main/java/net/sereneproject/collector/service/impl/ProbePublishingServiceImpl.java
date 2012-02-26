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
package net.sereneproject.collector.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sereneproject.collector.domain.Probe;
import net.sereneproject.collector.domain.ProbeValue;
import net.sereneproject.collector.domain.Server;
import net.sereneproject.collector.domain.ServerGroup;
import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.dto.ProbeValueDto;
import net.sereneproject.collector.service.ProbePublishingService;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * Service used to publish monitoring values.
 * 
 * @author gehel
 */
@Service
public class ProbePublishingServiceImpl implements ProbePublishingService {

    /** Logger. */
    private static final Logger LOG = Logger
            .getLogger(ProbePublishingServiceImpl.class);

    @Override
    public final void publish(final MonitoringMessageDto message) {
        // local caches to keep from accessing the DB at each iteration
        Map<String, Server> serverCache = new HashMap<String, Server>();
        Map<String, ServerGroup> groupCache = new HashMap<String, ServerGroup>();

        for (ProbeValueDto pvDto : message.getProbeValues()) {
            // add value to probe
            ProbeValue pv = new ProbeValue();
            pv.setProbe(findOrCreateProbe(pvDto, message, serverCache,
                    groupCache));
            pv.setValue(pvDto.getValue());
            pv.setDate(new Date());
            pv.persist();
        }

        // TODO: send values to analyzers, should be done asynchronously
        // (message queue ?)
    }

    /**
     * Find a {@link Probe}, create it if it doesnt exist.
     * 
     * @param pvDto
     *            the probe value whose probe we are looking for
     * @param message
     *            the current message, used to retreive the server / server
     *            group if needed
     * @param serverCache
     *            cache local to the call, if a server is found or created, it
     *            is stored in this cache
     * @param groupCache
     *            cache local to the call, if a server group is found or
     *            created, it is stored in this cache
     * @return the {@link Probe} we are looking for, either found in the DB or
     *         freshly created
     */
    private Probe findOrCreateProbe(final ProbeValueDto pvDto,
            final MonitoringMessageDto message,
            final Map<String, Server> serverCache,
            final Map<String, ServerGroup> groupCache) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Looking for probe [" + pvDto.getUuid() + "]");
            }
            return Probe.findProbeByUuidEquals(pvDto.getUuid());
        } catch (EmptyResultDataAccessException dataEx) {
            // probe does not exist
            // TODO: if server does not exist, the group, hostname, etc...
            // should be specified
            if (LOG.isDebugEnabled()) {
                LOG.debug("Probe [" + pvDto.getUuid()
                        + "] not found, creating it.");
            }
            Probe probe = new Probe();
            probe.setName(pvDto.getName());
            probe.setServer(findOrCreateServer(message, serverCache, groupCache));
            probe.setUuid(pvDto.getUuid());
            probe.persist();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Probe [" + probe + "] created.");
            }
            return probe;
        }
    }

    /**
     * Finds a {@link Server}, create it if it doesnt exist.
     * 
     * @param message
     *            the message whose server we are looking for
     * @param serverCache
     *            cache local to the call, if a server is found or created, it
     *            is stored in this cache
     * @param groupCache
     *            cache local to the call, if a server group is found or
     *            created, it is stored in this cache
     * @return the {@link Server} we are looking for, either found in the DB or
     *         freshly created
     */
    private Server findOrCreateServer(final MonitoringMessageDto message,
            final Map<String, Server> serverCache,
            final Map<String, ServerGroup> groupCache) {
        if (serverCache.containsKey(message.getUuid())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Returning server [" + message.getUuid()
                        + "] from local cache.");
            }
            return serverCache.get(message.getUuid());
        }
        try {
            Server server = Server.findServerByUuidEquals(message.getUuid());
            serverCache.put(message.getUuid(), server);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Returning server [" + message.getUuid()
                        + "] from DB.");
            }
            return server;
        } catch (EmptyResultDataAccessException dataEx) {

            // server does not exist
            // TODO: if server does not exist, the group, hostname, etc...
            // should be specified
            if (LOG.isDebugEnabled()) {
                LOG.debug("Server [" + message.getUuid()
                        + "] not found, creating it.");
            }
            Server server = new Server();
            server.setUuid(message.getUuid());
            server.setHostname(message.getHostname());
            server.setServerGroup(findOrCreateServerGroup(message.getGroup(),
                    groupCache));
            server.persist();
            serverCache.put(message.getUuid(), server);
            LOG.debug("Server created.");
            return server;
        }
    }

    /**
     * Finds a {@link ServerGroup}, create it if it doesnt exist.
     * 
     * @param name
     *            the name of the group
     * @param groupCache
     *            cache local to the call, if a server group is found or
     *            created, it is stored in this cache
     * @return the {@link ServerGroup} we are looking for, either found in the
     *         DB or freshly created
     */
    private ServerGroup findOrCreateServerGroup(final String name,
            final Map<String, ServerGroup> groupCache) {
        if (groupCache.containsKey(name)) {
            return groupCache.get(name);
        }
        try {
            ServerGroup group = ServerGroup.findServerGroupsByNameEquals(name)
                    .getSingleResult();
            groupCache.put(name, group);
            return group;
        } catch (EmptyResultDataAccessException dataExGroup) {
            // create group if it does not exist
            ServerGroup group = new ServerGroup();
            group.setName(name);
            group.persist();
            groupCache.put(name, group);
            return group;
        }
    }

}
