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
package net.sereneproject.collector.rrd;

import static org.rrd4j.ConsolFun.AVERAGE;
import static org.rrd4j.DsType.GAUGE;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import junit.framework.Assert;
import net.sereneproject.collector.domain.Probe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class RrdJpaTest {

    @Autowired
    private RrdJpaBackendFactory rrdBackend;

    @Test
    public void testPublishingAndRetreivingData() throws IOException {
        String uuid = UUID.randomUUID().toString();
        Probe probe = new Probe();
        probe.setUuid(uuid);
        probe.setName("probe for rrd tests");
        probe.persist();
        RrdDef rrdDef = new RrdDef(uuid);
        rrdDef.addArchive(AVERAGE, 0.5, 1, 600);
        rrdDef.addArchive(AVERAGE, 0.5, 6, 700);
        rrdDef.addArchive(AVERAGE, 0.5, 24, 775);
        rrdDef.addArchive(AVERAGE, 0.5, 288, 797);
        rrdDef.addDatasource("test-datasource", GAUGE, 600, Double.NaN,
                Double.NaN);
        RrdDb rrdDb = new RrdDb(rrdDef, this.rrdBackend);

        long now = new Date().getTime();

        Sample sample = rrdDb.createSample(now);
        sample.setValue("test-datasource", 0);
        sample.update();

        sample = rrdDb.createSample(now + 60000);
        sample.setValue("test-datasource", 1);
        sample.update();

        sample = rrdDb.createSample(now + 120000);
        sample.setValue("test-datasource", 2);
        sample.update();

        Assert.assertEquals(2.0, rrdDb.getDatasource("test-datasource")
                .getLastValue());
    }

}
