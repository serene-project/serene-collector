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

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;

import net.sereneproject.collector.domain.Plugin;
import net.sereneproject.collector.domain.ProbeValue;
import net.sereneproject.collector.dto.AnalyzerRequestDto;
import net.sereneproject.collector.dto.AnalyzerResponseDto;
import net.sereneproject.collector.service.AnalyzerPluginCommunicationService;
import net.sereneproject.collector.service.AnalyzerService;

public class AnalyzerServiceImpl implements AnalyzerService {

    private final AnalyzerPluginCommunicationService analyzerPluginCommunicationService;

    @Autowired
    public AnalyzerServiceImpl(
            AnalyzerPluginCommunicationService analyzerPluginCommunicationService) {
        this.analyzerPluginCommunicationService = analyzerPluginCommunicationService;
    }

    @Override
    public void analyze(ProbeValue probeValue) {
        for (Plugin plugin : probeValue.getProbe().getPlugins()) {
            // create request
            AnalyzerRequestDto request = new AnalyzerRequestDto();
            request.setDate(probeValue.getDate());
            request.setSavedState(plugin.getSavedState());
            request.setValue(probeValue.getValue());

            // send probe to analyzer plugins
            AnalyzerResponseDto response;
            try {
                response = getAnalyzerPluginCommunicationService().send(
                        plugin.getUri(), request);

                // save response
                plugin.setSavedState(response.getNewSavedState());
                plugin.setStatus(response.getStatus());
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private AnalyzerPluginCommunicationService getAnalyzerPluginCommunicationService() {
        return this.analyzerPluginCommunicationService;
    }

}
