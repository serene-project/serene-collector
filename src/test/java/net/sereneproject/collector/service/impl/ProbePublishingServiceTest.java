package net.sereneproject.collector.service.impl;

import java.util.ArrayList;
import java.util.UUID;

import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.dto.ProbeValueDto;
import net.sereneproject.collector.service.ProbePublishingService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class ProbePublishingServiceTest {

    @Autowired(required = true)
    private ProbePublishingService probePublishingService;

    @Test
    public void publishingBrandNewValue() {
        MonitoringMessageDto m = new MonitoringMessageDto();
        m.setUuid(UUID.randomUUID().toString());
        m.setGroup("non existing group");
        m.setHostname("non existing hostname");
        m.setProbeValues(new ArrayList<ProbeValueDto>());

        ProbeValueDto pv = new ProbeValueDto();
        pv.setName("CPU");
        pv.setUuid(UUID.randomUUID().toString());
        pv.setValue("35");
        m.getProbeValues().add(pv);

        pv = new ProbeValueDto();
        pv.setName("Disk");
        pv.setUuid(UUID.randomUUID().toString());
        pv.setValue("256");
        m.getProbeValues().add(pv);

        getProbePublishingService().publish(m);
    }

    private ProbePublishingService getProbePublishingService() {
        return this.probePublishingService;
    }
}
