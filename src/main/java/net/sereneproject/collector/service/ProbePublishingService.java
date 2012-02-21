package net.sereneproject.collector.service;

import java.net.URL;

import net.sereneproject.collector.dto.MonitoringMessageDto;

public interface ProbePublishingService {

    void publish(MonitoringMessageDto message);

}
