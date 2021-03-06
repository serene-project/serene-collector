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
package net.sereneproject.collector.service;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.ClientProtocolException;

import net.sereneproject.collector.dto.AnalyzerRequestDto;
import net.sereneproject.collector.dto.AnalyzerResponseDto;

/**
 * Service used to send messages to analyzers.
 * 
 * This service is responsible only of the communication, not of the treatment
 * of data.
 * 
 * @author gehel
 */
public interface AnalyzerPluginCommunicationService {

    /**
     * Send a message to an analyzer.
     * 
     * @param uri
     *            the URI of the analyzer
     * @param request
     *            the message to send
     * @return the response from the analyzer
     * @throws ClientProtocolException in case of protocol violation
     * @throws IOException in case of generic communication errors
     */
    AnalyzerResponseDto send(URI uri, AnalyzerRequestDto request)
            throws ClientProtocolException, IOException;

}
