package net.sereneproject.collector.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import junit.framework.Assert;

import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.dto.ProbeValueDto;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Test;

import com.google.common.io.ByteStreams;

public class ProbeControllerIntegrationTest {

	private static String CONTEXT = "/serene-collector";

	@Test
	public void sendProbe() throws ClientProtocolException, IOException {
		
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

		HttpHost host = new HttpHost("localhost", 8080, "http");
		DefaultHttpClient client = new DefaultHttpClient();
		client.getCredentialsProvider().setCredentials(
				new AuthScope(host.getHostName(), host.getPort()),
				new UsernamePasswordCredentials("admin", "admin"));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);

		// Add AuthCache to the execution context
		BasicHttpContext localcontext = new BasicHttpContext();
		localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

		HttpPost post = new HttpPost(CONTEXT + "/probe");
		post.addHeader("Accept", "application/json");
		StringEntity body = new StringEntity(m.toJson(), "UTF-8");
		post.setEntity(body);
		HttpResponse response = client.execute(host, post, localcontext);

		ByteStreams.copy(response.getEntity().getContent(), System.out);

		Assert.assertEquals(HttpStatus.SC_CREATED, response.getStatusLine()
				.getStatusCode());
	}

}