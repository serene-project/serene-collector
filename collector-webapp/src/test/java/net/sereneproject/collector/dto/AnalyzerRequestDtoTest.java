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
package net.sereneproject.collector.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * Test {@link AnalyzerRequestDto}.
 * 
 * @author gehel
 * 
 */
public class AnalyzerRequestDtoTest {

    /** A dummy test value. */
    private static final Double PROBE_VALUE = 0.4;
    
	/**
	 * Print a JSON serialized {@link AnalyzerRequestDto}. Useful as an example.
	 */
	@Test
	public final void printJson() {
	    Set<ValueDto> values = new HashSet<ValueDto>();
        values.add(new ValueDto("user", 5.0));
        values.add(new ValueDto("system", 5.0));
        values.add(new ValueDto("idle", 90.0));
		AnalyzerRequestDto request = new AnalyzerRequestDto();
		request.setValues(values);
		request.setDate(new Date());
		request.setSavedState("etat courant du model");
		System.out.println(request.toJson());
	}

}
