package net.sereneproject.collector.validation;

import java.util.UUID;

import net.sereneproject.collector.dto.MonitoringMessageDto;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

public class MonitoringMessageDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MonitoringMessageDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MonitoringMessageDto monitoringMessage = (MonitoringMessageDto) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uuid",
				"server.uuid.empty");

		// check if UUID is valid
		try {
			UUID.fromString(monitoringMessage.getUuid());
		} catch (IllegalArgumentException iae) {
			errors.rejectValue("uuid", "server.uuid.invalid");
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
		}
	}

}
