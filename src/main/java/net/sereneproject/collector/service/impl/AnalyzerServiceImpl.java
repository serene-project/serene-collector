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
