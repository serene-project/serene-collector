package net.sereneproject.collector.dto;

import java.util.Date;

import org.junit.Test;

public class AnalyzerRequestDtoTest {

    @Test
    public void printJson() {
        AnalyzerRequestDto request = new AnalyzerRequestDto();
        request.setValue("0.4");
        request.setDate(new Date());
        request.setSavedState("etat courant du model");
        System.out.println(request.toJson());
    }

}
