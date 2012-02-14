package net.sereneproject.collector.dto;

import java.util.Date;

import org.junit.Test;

/**
 * Test {@link AnalyzerRequestDto}.
 * 
 * @author gehel
 * 
 */
public class AnalyzerRequestDtoTest {

	/**
	 * Print a JSON serialized {@link AnalyzerRequestDto}. Useful as an example.
	 */
	@Test
	public final void printJson() {
		AnalyzerRequestDto request = new AnalyzerRequestDto();
		request.setValue("0.4");
		request.setDate(new Date());
		request.setSavedState("etat courant du model");
		System.out.println(request.toJson());
	}

}
