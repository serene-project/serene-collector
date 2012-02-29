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

@RequestMapping("/collector")
@Controller
public class CollectorController {

    private static final Logger LOG = Logger.getLogger(CollectorController.class);

    @Autowired(required = true)
    private ProbePublishingService probePublishingService;

    @Autowired(required = true)
    private MonitoringMessageDtoValidator validator;

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public final ResponseEntity<java.lang.String> postMonitoring(
            @RequestBody String json) {
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

    private ProbePublishingService getProbePublishingService() {
        return this.probePublishingService;
    }

    private MonitoringMessageDtoValidator getValidator() {
        return this.validator;
    }
}