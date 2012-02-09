package net.sereneproject.collector.service;

import net.sereneproject.collector.dto.MonitoringMessageDto;

public interface ProbePublishingService {

    void publish(MonitoringMessageDto message);

}
