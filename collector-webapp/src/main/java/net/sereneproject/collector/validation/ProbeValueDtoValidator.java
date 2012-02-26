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

import java.util.UUID;

import net.sereneproject.collector.dto.ProbeValueDto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

/**
 * Validator for probe values.
 * 
 * @author gehel
 */
@Component
public class ProbeValueDtoValidator implements Validator {

    @Override
    public final boolean supports(final Class<?> clazz) {
        return ProbeValueDto.class.equals(clazz);
    }

    @Override
    public final void validate(final Object target, final Errors errors) {
        ProbeValueDto probeValue = (ProbeValueDto) target;
        ValidationUtils.rejectIfEmpty(errors, "uuid", "probeValue.uuid.empty");
        ValidationUtils
                .rejectIfEmpty(errors, "value", "probeValue.value.empty");

        // check if UUID is valid
        if (!Strings.isNullOrEmpty(probeValue.getUuid())) {
            try {
                UUID.fromString(probeValue.getUuid());
            } catch (IllegalArgumentException iae) {
                errors.rejectValue("uuid", "probeValue.uuid.invalid");
            }
        }
    }

}
