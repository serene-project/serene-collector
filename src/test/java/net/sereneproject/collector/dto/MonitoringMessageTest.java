package net.sereneproject.collector.dto;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MonitoringMessageTest {

    @Test
    public void printJson() {
        MonitoringMessageDto m = new MonitoringMessageDto();
        m.setGroup("My Group");
        m.setHostname("myhost");
        m.setUuid(UUID.randomUUID().toString());
        m.setProbeValues(new ArrayList<ProbeValueDto>());
        ProbeValueDto pv = new ProbeValueDto();
        pv.setName("CPU");
        pv.setUuid(UUID.randomUUID().toString());
        pv.setValue("0.7");
        m.getProbeValues().add(pv);
        pv = new ProbeValueDto();
        pv.setName("disk");
        pv.setUuid(UUID.randomUUID().toString());
        pv.setValue("21");
        m.getProbeValues().add(pv);
        System.out.println(m.toJson());
    }

    @Test
    public void deserialize() {
        MonitoringMessageDto m = MonitoringMessageDto
                .fromJsonToMonitoringMessageDto("{\"group\":\"My Group\",\"hostname\":\"myhost\",\"probeValues\":[{\"name\":\"CPU\",\"uuid\":\"3ab61912-761b-4e68-9c92-a61f55daa310\",\"value\":\"0.7\"},{\"name\":\"disk\",\"uuid\":\"07185ca2-954a-4a79-8b17-da44251f9751\",\"value\":\"21\"}],\"uuid\":\"410f8c3d-f8d2-4597-ad49-2ad23bde306f\"}");
        assertEquals("My Group", m.getGroup());
        assertEquals(2, m.getProbeValues().size());
        assertEquals("CPU", m.getProbeValues().get(0).getName());
    }
}
