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
