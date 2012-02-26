/**
 * Copyright (c) 2012, Serene Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
import org.springframework.transaction.annotation.Transactional;

/**
 * Test {@link ProbePublishingServiceImpl}.
 * 
 * @author gehel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class ProbePublishingServiceTest {

	/** The {@link ProbePublishingServiceImpl} to test. */
	@Autowired(required = true)
	private ProbePublishingService probePublishingService;

	/**
	 * Test publishing new values.
	 * 
	 * This test ensures that publishing probe values where the server / probe /
	 * group are not known by the system works.
	 */
	@Test
	@Transactional
	public final void publishingBrandNewValue() {
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
