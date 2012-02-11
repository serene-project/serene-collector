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

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ProbePublishingServiceImpl implements ProbePublishingService {

    @Override
    public void publish(MonitoringMessageDto message) {
        // local caches to keep from accessing the DB at each iteration
        Map<String, Server> serverCache = new HashMap<String, Server>();
        Map<String, ServerGroup> groupCache = new HashMap<String, ServerGroup>();

        for (ProbeValueDto pvto : message.getProbeValues()) {
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

    private Probe findProbe(ProbeValueDto pvDto, MonitoringMessageDto message,
            Map<String, Server> serverCache, Map<String, ServerGroup> groupCache) {
        try {
            return Probe.findProbeByUuidEquals(pvDto.getUuid());
        } catch (EmptyResultDataAccessException dataEx) {
            // probe does not exist
            // TODO: if server does not exist, the group, hostname, etc...
            // should be specified
            Probe probe = new Probe();
            probe.setName(pvDto.getName());
            probe.setServer(findServer(message, serverCache, groupCache));
            probe.setUuid(pvDto.getUuid());
            probe.persist();

            return probe;
        }
    }

    private Server findServer(MonitoringMessageDto message,
            Map<String, Server> serverCache, Map<String, ServerGroup> groupCache) {
        if (serverCache.containsKey(message.getUuid())) {
            return serverCache.get(message.getUuid());
        }
        try {
            Server server = Server.findServerByUuidEquals(message.getUuid());
            serverCache.put(message.getUuid(), server);
            return server;
        } catch (EmptyResultDataAccessException dataEx) {

            // server does not exist
            // TODO: if server does not exist, the group, hostname, etc...
            // should be specified
            Server server = new Server();
            server.setUuid(message.getUuid());
            server.setHostname(message.getHostname());
            server.setServerGroup(findServerGroup(message.getGroup(),
                    groupCache));
            server.persist();
            serverCache.put(message.getUuid(), server);
            return server;
        }
    }

    private ServerGroup findServerGroup(String name,
            Map<String, ServerGroup> groupCache) {
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
