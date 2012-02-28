package net.sereneproject.collector.web;

import net.sereneproject.collector.domain.Probe;
import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.service.ProbePublishingService;
import net.sereneproject.collector.validation.MonitoringMessageDtoValidator;
import net.sereneproject.collector.web.utils.WebUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/probes")
@Controller
@RooWebScaffold(path = "probes", formBackingObject = Probe.class)
public class ProbeController {

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(ProbeController.class);

    /** Service used to publish probe values. */
    @Autowired(required = true)
    private ProbePublishingService probePublishingService;

    /** Validator. */
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
    public final ResponseEntity<String> postMonitoring(
            @RequestBody final String json) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received message : [" + json + "]");
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

    /**
     * The publishing service.
     * 
     * @return the publishing service
     */
    private ProbePublishingService getProbePublishingService() {
        return this.probePublishingService;
    }

    /**
     * The validator.
     * 
     * @return the validator
     */
    private MonitoringMessageDtoValidator getValidator() {
        return this.validator;
    }
}
