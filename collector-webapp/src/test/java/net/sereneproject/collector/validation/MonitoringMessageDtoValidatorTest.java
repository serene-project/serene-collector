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
package net.sereneproject.collector.validation;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.dto.ProbeValueDto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

/**
 * Test {@link MonitoringMessageDtoValidator}.
 * 
 * @author gehel
 */
public class MonitoringMessageDtoValidatorTest {

    /** The validator to be tested. */
    private MonitoringMessageDtoValidator validator;

    /**
     * Setup the validator to be tested.
     */
    @Before
    public final void setup() {
        this.validator = new MonitoringMessageDtoValidator(
                new ProbeValueDtoValidator());
    }

    /**
     * Make sure the validator supports the right class.
     */
    @Test
    public final void testClassSupported() {
        this.validator.supports(MonitoringMessageDto.class);
    }

    /**
     * Null UUID should not be accepted.
     */
    @Test
    public final void uuidNull() {
        MonitoringMessageDto message = new MonitoringMessageDto();
        message.setUuid((String) null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
                message, "monitoringMessage");
        this.validator.validate(message, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
        assertEquals(1, fieldErrors.size());
        assertEquals("server.uuid.empty", fieldErrors.get(0).getCode());
    }

    /**
     * Empty UUID should not be accepted.
     */
    @Test
    public final void uuidEmpty() {
        MonitoringMessageDto message = new MonitoringMessageDto();
        message.setUuid("");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
                message, "monitoringMessage");
        this.validator.validate(message, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
        assertEquals(1, fieldErrors.size());
        assertEquals("server.uuid.empty", fieldErrors.get(0).getCode());
    }

    /**
     * Invalid format of UUID should not be accepted.
     */
    @Test
    public final void uuidInvalid() {
        MonitoringMessageDto message = new MonitoringMessageDto();
        message.setUuid("abcd");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
                message, "monitoringMessage");
        this.validator.validate(message, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
        assertEquals(1, fieldErrors.size());
        assertEquals("server.uuid.invalid", fieldErrors.get(0).getCode());
    }

    /**
     * List of probe values should not be null.
     */
    @Test
    public final void probeValuesNotNull() {
        MonitoringMessageDto message = new MonitoringMessageDto();
        message.setProbeValues((List<ProbeValueDto>) null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
                message, "monitoringMessage");
        this.validator.validate(message, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors("probeValues");
        assertEquals(1, fieldErrors.size());
        assertEquals("probevalues.empty", fieldErrors.get(0).getCode());
    }

    /**
     * List of probe values should not be null.
     */
    @Test
    public final void probeValuesNotEmpty() {
        MonitoringMessageDto message = new MonitoringMessageDto();
        message.setProbeValues(Collections.<ProbeValueDto> emptyList());
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
                message, "monitoringMessage");
        this.validator.validate(message, errors);
        List<FieldError> fieldErrors = errors.getFieldErrors("probeValues");
        assertEquals(1, fieldErrors.size());
        assertEquals("probevalues.empty", fieldErrors.get(0).getCode());
    }

}
