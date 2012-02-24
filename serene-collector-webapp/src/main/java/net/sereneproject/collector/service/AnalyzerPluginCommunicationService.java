package net.sereneproject.collector.service;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.ClientProtocolException;

import net.sereneproject.collector.dto.AnalyzerRequestDto;
import net.sereneproject.collector.dto.AnalyzerResponseDto;

public interface AnalyzerPluginCommunicationService {

    AnalyzerResponseDto send(URI uri, AnalyzerRequestDto request) throws ClientProtocolException, IOException;

}
