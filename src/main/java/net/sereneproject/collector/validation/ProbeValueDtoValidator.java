package net.sereneproject.collector.validation;

import java.util.UUID;

import net.sereneproject.collector.dto.ProbeValueDto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProbeValueDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ProbeValueDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProbeValueDto probeValue = (ProbeValueDto) target;
		ValidationUtils
				.rejectIfEmpty(errors, "uuid", "probeValue.uuid.missing");
		ValidationUtils.rejectIfEmpty(errors, "value",
				"probeValue.value.missing");

		// check if UUID is valid
		try {
			UUID.fromString(probeValue.getUuid());
		} catch (IllegalArgumentException iae) {
			errors.rejectValue("uuid", "probeValue.uuid.invalid");
		}
	}

}
