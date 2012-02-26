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
package net.sereneproject.collector.validation;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import net.sereneproject.collector.dto.ProbeValueDto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

/**
 * Test validation rules to {@link ProbeValueDtoValidator}.
 * 
 * @author gehel
 */
public class ProbeValueDtoValidatorTest {

	/** The validator to be tested. */
	private ProbeValueDtoValidator validator;

	/**
	 * Create the {@link ProbeValueDtoValidator} to be validated.
	 */
	@Before
	public final void setup() {
		this.validator = new ProbeValueDtoValidator();
	}

	/**
	 * Null UUID should not be accepted.
	 */
	@Test
	public final void uuidNull() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid((String) null);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.uuid.empty", fieldErrors.get(0).getCode());
	}

	/**
	 * Empty UUID should not be accepted.
	 */
	@Test
	public final void uuidEmpty() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid("");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.uuid.empty", fieldErrors.get(0).getCode());
	}

	/**
	 * UUID sould conform to the Java specification {@link UUID#toString()}.
	 * 
	 * Test that if UUID is not valid, a validation error is thrown.
	 */
	@Test
	public final void uuidNotValid() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid("toto");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.uuid.invalid", fieldErrors.get(0).getCode());
	}

	/**
	 * UUID sould conform to the Java specification {@link UUID#toString()}.
	 * 
	 * Test that if UUID is valid, no validation error is thrown.
	 */
	@Test
	public final void uuidValid() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid(UUID.randomUUID().toString());
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(0, fieldErrors.size());
	}

	/**
	 * Empty value should not be accepted.
	 */
	@Test
	public final void valueEmpty() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setValue((String) null);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("value");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.value.empty", fieldErrors.get(0).getCode());
	}
}
