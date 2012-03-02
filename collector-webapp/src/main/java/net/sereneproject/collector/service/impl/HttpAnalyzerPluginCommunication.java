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
package net.sereneproject.collector.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import net.sereneproject.collector.dto.AnalyzerRequestDto;
import net.sereneproject.collector.dto.AnalyzerResponseDto;
import net.sereneproject.collector.service.AnalyzerPluginCommunicationService;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

/**
 * A service to communicate with plugins.
 * 
 * This service does no treatment of the request or the response and cares only
 * about the communication itself.
 * 
 * @author gehel
 */
@Service
public class HttpAnalyzerPluginCommunication implements
        AnalyzerPluginCommunicationService {

    /** The HTTP client used for communciation. */
    private final HttpClient client;

    /**
     * Construct the service.
     * 
     * @param client
     *            the HTTP client used for communication
     */
    @Autowired
    public HttpAnalyzerPluginCommunication(final HttpClient client) {
        this.client = client;
    }

    /**
     * Implementation of
     * {@link AnalyzerPluginCommunicationService#send(URI, AnalyzerRequestDto)}.
     * 
     * @param uri
     *            the URI of the analyzer
     * @param request
     *            the message to send
     * @return the response from the analyzer
     * @throws ClientProtocolException
     *             in case of protocol violation
     * @throws IOException
     *             in case of generic communication errors
     */
    @Override
    public final AnalyzerResponseDto send(final URI uri,
            final AnalyzerRequestDto request) throws ClientProtocolException,
            IOException {
        HttpPut putRequest = new HttpPut(uri);
        putRequest.setHeader(new BasicHeader("Accept", "application/json"));

        try {
            putRequest.setEntity(new StringEntity(request.toJson(),
                    CharEncoding.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(
                    "UTF-8 should always be supported", e);
        }

        HttpResponse httpResponse = getClient().execute(putRequest);

        // TODO: check HTTP status
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            // TODO: throw some kind of exception
        }

        if (httpResponse.getEntity() == null) {
            // TODO: throw some kind of exception
        }

        Reader responseStream = new InputStreamReader(httpResponse.getEntity()
                .getContent(), CharEncoding.UTF_8);
        try {
            String jsonResponse = CharStreams.toString(responseStream);
            return AnalyzerResponseDto
                    .fromJsonToAnalyzerResponseDto(jsonResponse);
        } catch (IOException ioe) {
            throw ioe;
        } catch (RuntimeException re) {
            putRequest.abort();
            throw re;
        } finally {
            Closeables.closeQuietly(responseStream);
        }
    }

    /**
     * The HTTP client used for communication.
     * 
     * @return the HTTP client
     */
    private HttpClient getClient() {
        return this.client;
    }

}
