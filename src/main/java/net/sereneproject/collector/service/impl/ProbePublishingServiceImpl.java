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

@Service
public class ProbePublishingServiceImpl implements ProbePublishingService {

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
            pv.setProbe(findProbe(pvDto, message, serverCache, groupCache));
            pv.setValue(Long.parseLong(pvDto.getValue()));
            pv.setDate(new Date());
            pv.persist();
        }

        // TODO: send values to analyzers, should be done asynchronously
        // (message queue ?)
    }

    private Probe findProbe(final ProbeValueDto pvDto,
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
            probe.setServer(findServer(message, serverCache, groupCache));
            probe.setUuid(pvDto.getUuid());
            probe.persist();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Probe [" + probe + "] created.");
            }
            return probe;
        }
    }

    private Server findServer(final MonitoringMessageDto message,
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
            server.setServerGroup(findServerGroup(message.getGroup(),
                    groupCache));
            server.persist();
            serverCache.put(message.getUuid(), server);
            LOG.debug("Server created.");
            return server;
        }
    }

    private ServerGroup findServerGroup(final String name,
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
