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
package net.sereneproject.collector.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

/**
 * Test {@link MonitoringMessageDto}.
 * 
 * @author gehel
 * 
 */
public class MonitoringMessageTest {

    /**
     * Print a {@link MonitoringMessageDto} serialized as JSON.
     */
    @Test
    public final void printJson() {

        System.out.println(createTestMessage().toJson());
    }

    /**
     * Test deserializing a JSON {@link MonitoringMessageDto}.
     */
    @Test
    public final void deserialize() {
        MonitoringMessageDto m = MonitoringMessageDto
                .fromJsonToMonitoringMessageDto("{\"hostname\":\"myhost\","
                        + "\"probes\":[{" + "\"name\":\"CPU\","
                        + "\"values\":["
                        + "{\"name\":\"user\",\"value\":20.0},"
                        + "{\"name\":\"system\",\"value\":10.0},"
                        + "{\"name\":\"idle\",\"value\":80.0}],"
                        + "\"uuid\":\"723d2203-04e8-47b2-addc-c3b96f6ab751\""
                        + "}]}");
        assertEquals("myhost", m.getHostname());
        assertEquals(1, m.getProbes().size());
        assertEquals("CPU", m.getProbes().get(0).getName());

        Set<ValueDto> values = m.getProbes().get(0).getValues();
        assertEquals(3, values.size());
    }

    public static final MonitoringMessageDto createTestMessage() {
        List<ProbeDto> probes = new ArrayList<ProbeDto>();

        Set<ValueDto> pvs = new HashSet<ValueDto>();
        pvs.add(new ValueDto("user", 20.0));
        pvs.add(new ValueDto("system", 10.0));
        pvs.add(new ValueDto("idle", 80.0));
        probes.add(new ProbeDto(UUID.randomUUID().toString(), "CPU", pvs));

        pvs = new HashSet<ValueDto>();
        pvs.add(new ValueDto("/dev/sda1", 50.0));
        pvs.add(new ValueDto("/dev/sda2", 150.0));
        pvs.add(new ValueDto("/dev/sdb1", 10.0));
        probes.add(new ProbeDto(UUID.randomUUID().toString(), "disk", pvs));

        return new MonitoringMessageDto("myhost", probes);
    }
}
