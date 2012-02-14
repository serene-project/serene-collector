package net.sereneproject.collector.dto;

import org.junit.Test;

/**
 * Test {@link AnalyzerResponseDto}.
 * @author gehel
 *
 */
public class AnalyzerResponseDtoTest {

	/**
	 * Print an {@link AnalyzerResponseDto} serialized as JSON.
	 */
    @Test
    public final void printJson() {
        AnalyzerResponseDto request = new AnalyzerResponseDto();
        request.setStatus("OK");
        request.setNewSavedState("nouvel etat du model");
        System.out.println(request.toJson());
    }
}
