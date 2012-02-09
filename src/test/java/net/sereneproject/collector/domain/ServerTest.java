package net.sereneproject.collector.domain;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ServerTest {

    @Test
    public void doNothing() {
        // stub so that test passes, will be removed once actual tests are
        // implemented
    }

    // @Test
    public void findByUuid() {
        ServerGroup group = new ServerGroup();
        group.setName("my group");
        group.persist();

        String uuid = UUID.randomUUID().toString();
        Server server = new Server();
        server.setUuid(uuid);
        server.setHostname("my host name");
        server.setServerGroup(group);
        server.persist();

        Server saveServer = Server.findServerByUuidEquals(uuid);
        assertNotNull(saveServer);
    }
}
