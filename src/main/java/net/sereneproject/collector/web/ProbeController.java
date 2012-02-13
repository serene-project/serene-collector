package net.sereneproject.collector.web;

import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.service.ProbePublishingService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/probe")
@Controller
public class ProbeController {

	private static final Logger LOG = Logger.getLogger(ProbeController.class);

	@Autowired(required = true)
	private ProbePublishingService probePublishingService;

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> postMonitoring(@RequestBody String json) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received message : " + json);
		}
		MonitoringMessageDto message = MonitoringMessageDto
				.fromJsonToMonitoringMessageDto(json);
		getProbePublishingService().publish(message);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	private ProbePublishingService getProbePublishingService() {
		return this.probePublishingService;
	}
}
