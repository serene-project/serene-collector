package net.sereneproject.collector.validation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.sereneproject.collector.dto.MonitoringMessageDto;

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
	 * Null UUID should not be accepted.
	 */
	@Test
	public final void uuidNull() {
		MonitoringMessageDto message = new MonitoringMessageDto();
		message.setUuid((String) null);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(
				message, "propertyValue");
		this.validator.validate(message, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(1, fieldErrors.size());
		assertEquals("server.uuid.empty", fieldErrors.get(0).getCode());
	}

}
