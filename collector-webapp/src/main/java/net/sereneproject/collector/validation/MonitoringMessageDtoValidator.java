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

import java.util.UUID;

import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.dto.ProbeValueDto;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

@Component
public class MonitoringMessageDtoValidator implements Validator {

    private final ProbeValueDtoValidator probeValueValidator;

    @Autowired(required = true)
    public MonitoringMessageDtoValidator(
            ProbeValueDtoValidator probeValueValidator) {
        this.probeValueValidator = probeValueValidator;
    }

    @Override
    public final boolean supports(final Class<?> clazz) {
        return MonitoringMessageDto.class.equals(clazz);
    }

    @Override
    public final void validate(final Object target, final Errors errors) {
        MonitoringMessageDto monitoringMessage = (MonitoringMessageDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uuid",
                "server.uuid.empty");

        // check if UUID is valid
        if (!Strings.isNullOrEmpty(monitoringMessage.getUuid())) {
            try {
                UUID.fromString(monitoringMessage.getUuid());
            } catch (IllegalArgumentException iae) {
                errors.rejectValue("uuid", "server.uuid.invalid");
            }
        }

        // hostname and group need to be both empty or both have a value
        if (Strings.isNullOrEmpty(monitoringMessage.getHostname())) {
            if (!Strings.isNullOrEmpty(monitoringMessage.getGroup())) {
                // if hostname is null, group needs to be null as well
                errors.rejectValue("group", "server.group.notempty");
            }
        } else {
            if (Strings.isNullOrEmpty(monitoringMessage.getGroup())) {
                // if hostname is given, we need the group in which the created
                // server will be categorized
                errors.rejectValue("group", "server.group.empty");
            }
        }

        // there should be at least on probe value for a message
        if (CollectionUtils.isEmpty(monitoringMessage.getProbeValues())) {
            errors.rejectValue("probeValues", "probevalues.empty");
        } else {
            int i = 0;
            for (ProbeValueDto probeValue : monitoringMessage.getProbeValues()) {
                errors.pushNestedPath("probeValues[" + i++ + "]");
                ValidationUtils.invokeValidator(getProbeValueValidator(),
                        probeValue, errors);
                errors.popNestedPath();
            }
        }
    }

    private ProbeValueDtoValidator getProbeValueValidator() {
        return this.probeValueValidator;
    }
}
