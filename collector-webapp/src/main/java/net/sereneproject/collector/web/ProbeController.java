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
package net.sereneproject.collector.web;

import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.service.ProbePublishingService;
import net.sereneproject.collector.validation.MonitoringMessageDtoValidator;
import net.sereneproject.collector.web.utils.WebUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for posting probe values.
 * 
 * 
 * 
 * @author gehel
 */
@RequestMapping("/probe")
@Controller
public class ProbeController {

    private static final Logger LOG = Logger.getLogger(ProbeController.class);

    @Autowired(required = true)
    private ProbePublishingService probePublishingService;

    @Autowired(required = true)
    private MonitoringMessageDtoValidator validator;

    /**
     * Post a set of new probe values.
     * 
     * @param json
     *            the {@link MonitoringMessageDto} in JSON format
     * @return HTTP 201 (CREATED) if successful, HTTP 500 in case of error. In
     *         case of error an error message in JSON format will also be
     *         returned.
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public final ResponseEntity<String> postMonitoring(@RequestBody final String json) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received message : " + json);
        }

        MonitoringMessageDto message = MonitoringMessageDto
                .fromJsonToMonitoringMessageDto(json);

        // validation
        BindingResult bindingResult = new BeanPropertyBindingResult(message,
                "message");
        getValidator().validate(message, bindingResult);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                LOG.error(error.getObjectName() + " : " + error.getCode());
            }
            return new ResponseEntity<String>(WebUtils.toJson(bindingResult),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        getProbePublishingService().publish(message);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    private ProbePublishingService getProbePublishingService() {
        return this.probePublishingService;
    }

    private MonitoringMessageDtoValidator getValidator() {
        return this.validator;
    }
}
