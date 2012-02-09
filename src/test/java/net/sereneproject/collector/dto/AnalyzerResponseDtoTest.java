package net.sereneproject.collector.dto;

import org.junit.Test;

public class AnalyzerResponseDtoTest {

    @Test
    public void printJson() {
        AnalyzerResponseDto request = new AnalyzerResponseDto();
        request.setStatus("OK");
        request.setNewSavedState("nouvel etat du model");
        System.out.println(request.toJson());
    }
}
