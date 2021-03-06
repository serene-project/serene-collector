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
package net.sereneproject.collector.web;

import java.io.IOException;

import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.service.ProbePublishingService;
import net.sereneproject.collector.web.utils.WebUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Custom controller collecting ProbeValues.
 * 
 * @author gehel
 */
@RequestMapping("/collector")
@Controller
public class CollectorController {

    /** Logger. */
    private static final Logger LOG = Logger
            .getLogger(CollectorController.class);

    /** Publishing service doing the actuall collection. */
    @Autowired(required = true)
    private ProbePublishingService probePublishingService;

    /** Validator used to validate requests. */
    @Autowired(required = true)
    private Validator validator;

    /**
     * Collect probe values.
     * 
     * Agents can publish values for different probes using this service. They
     * should send POST requests to this URL. The body of the POST should
     * contain a monitoring message in JSON format.
     * 
     * Element not already existing will be created automatically.
     * 
     * TODO: better documentation of the exact mechanism
     * 
     * The message is a JSON serialized {@link MonitoringMessageDto}. It looks
     * like :
     * 
     * <pre>
     * {
     *     "hostname":"myhost",
     *     "probes":[{
     *         "name":"CPU",
     *         "uuid":"1aebd508-c0f1-4d3c-8019-68268492ee44",
     *         "values":[
     *             {"name":"user","value":20.0},
     *             {"name":"system","value":10.0},
     *             {"name":"idle","value":80.0}
     *         ]
     *     },{
     *         "name":"disk",
     *         "uuid":"770d021b-38d4-4a57-8d92-536873860661",
     *         "values":[
     *             {"name":"/dev/sda1","value":50.0},
     *             {"name":"/dev/sda2","value":150.0},
     *             {"name":"/dev/sdb1","value":10.0}
     *         ]
     *     }]
     * }
     * </pre>
     * 
     * Requests should be sent with a "Accept: application/json" header. The
     * body should be of content type "application/json" and be encoded in
     * UTF-8. The "Content-Type: application/json; charset=UTF-8" header is not
     * required, but should be used anyway for clarity.
     * 
     * @param json
     *            a message contining the new probe values
     * @return HTTP status 201 (CREATED) if post is successfull, HTTP status 400
     *         (BAD REQUEST) if the message was not valid. In case the message
     *         is not valid, a JSON response containing the error's detail is
     *         returned
     * @throws IOException 
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public final ResponseEntity<String> postMonitoring(
            @RequestBody final String json) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received message : [" + json + "]");
        }
        MonitoringMessageDto message = MonitoringMessageDto
                .fromJsonToMonitoringMessageDto(json);
        BindingResult bindingResult = new BeanPropertyBindingResult(message,
                "message");
        getValidator().validate(message, bindingResult);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                LOG.error(error.getObjectName() + " : " + error.getCode());
            }
            return new ResponseEntity<String>(WebUtils.toJson(bindingResult),
                    HttpStatus.BAD_REQUEST);
        }
        getProbePublishingService().publish(message);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    /**
     * Probe publishing service.
     * 
     * @return the service
     */
    private ProbePublishingService getProbePublishingService() {
        return this.probePublishingService;
    }

    /**
     * Monitoring message validator.
     * 
     * @return the validator
     */
    private Validator getValidator() {
        return this.validator;
    }
}
