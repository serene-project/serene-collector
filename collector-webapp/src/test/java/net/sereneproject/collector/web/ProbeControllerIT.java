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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Test;

import com.google.common.io.ByteStreams;

/**
 * Test {@link ProbeController}.
 * 
 * @author gehel
 */
public class ProbeControllerIT {

	/** context under which the project is deployed for tests. */
	private static final String CONTEXT = "/collector-webapp";
	
	/** server port. */
	private static final int SERVER_PORT = 9090;

	/**
	 * Test that sending a new probe works.
	 * 
	 * Sends a message containing a server / group / probe that isnt known yet
	 * by the application.
	 * 
	 * @throws IOException in case of communication error
	 */
	@Test
	public final void sendProbe() throws IOException {

		MonitoringMessageDto m = new MonitoringMessageDto();
		m.setUuid(UUID.randomUUID().toString());
		m.setGroup("non existing group");
		m.setHostname("non existing hostname");
		m.setProbeValues(new ArrayList<ProbeValueDto>());

		ProbeValueDto pv = new ProbeValueDto();
		pv.setName("CPU");
		pv.setUuid(UUID.randomUUID().toString());
		pv.setValue(35.0);
		m.getProbeValues().add(pv);

		pv = new ProbeValueDto();
		pv.setName("Disk");
		pv.setUuid(UUID.randomUUID().toString());
		pv.setValue(256.3);
		m.getProbeValues().add(pv);

		HttpHost host = new HttpHost("localhost", SERVER_PORT, "http");
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
