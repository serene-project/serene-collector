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

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

public class HttpAnalyzerPluginCommunication implements
        AnalyzerPluginCommunicationService {

    private final HttpClient client;

    @Autowired
    public HttpAnalyzerPluginCommunication(HttpClient client) {
        this.client = client;
    }

    @Override
    public AnalyzerResponseDto send(URI uri, AnalyzerRequestDto request)
            throws ClientProtocolException, IOException {
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

    private HttpClient getClient() {
        return this.client;
    }

}
