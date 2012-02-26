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

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
		MonitoringMessageDto m = new MonitoringMessageDto();
		m.setGroup("My Group");
		m.setHostname("myhost");
		m.setUuid(UUID.randomUUID().toString());
		m.setProbeValues(new ArrayList<ProbeValueDto>());
		ProbeValueDto pv = new ProbeValueDto();
		pv.setName("CPU");
		pv.setUuid(UUID.randomUUID().toString());
		pv.setValue(0.7);
		m.getProbeValues().add(pv);
		pv = new ProbeValueDto();
		pv.setName("disk");
		pv.setUuid(UUID.randomUUID().toString());
		pv.setValue(21.0);
		m.getProbeValues().add(pv);
		System.out.println(m.toJson());
	}

	/**
	 * Test deserializing a JSON {@link MonitoringMessageDto}.
	 */
	@Test
	public final void deserialize() {
		MonitoringMessageDto m = MonitoringMessageDto
				.fromJsonToMonitoringMessageDto("{\"" + "group\":\"My Group\","
						+ "\"hostname\":\"myhost\"," + "\"probeValues\":[{\""
						+ "name\":\"CPU\","
						+ "\"uuid\":\"3ab61912-761b-4e68-9c92-a61f55daa310\","
						+ "\"value\":\"0.7\"" + "},{\"" + "name\":\"disk\","
						+ "\"uuid\":\"07185ca2-954a-4a79-8b17-da44251f9751\","
						+ "\"value\":\"21\"" + "}],"
						+ "\"uuid\":\"410f8c3d-f8d2-4597-ad49-2ad23bde306f\"}");
		assertEquals("My Group", m.getGroup());
		assertEquals(2, m.getProbeValues().size());
		assertEquals("CPU", m.getProbeValues().get(0).getName());
	}
}
