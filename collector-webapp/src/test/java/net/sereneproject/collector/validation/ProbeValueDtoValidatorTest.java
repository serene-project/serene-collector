package net.sereneproject.collector.validation;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import net.sereneproject.collector.dto.ProbeValueDto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

/**
 * Test validation rules to {@link ProbeValueDtoValidator}.
 * 
 * @author gehel
 */
public class ProbeValueDtoValidatorTest {

	/** The validator to be tested. */
	private ProbeValueDtoValidator validator;

	/**
	 * Create the {@link ProbeValueDtoValidator} to be validated.
	 */
	@Before
	public final void setup() {
		this.validator = new ProbeValueDtoValidator();
	}

	/**
	 * Null UUID should not be accepted.
	 */
	@Test
	public final void uuidNull() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid((String) null);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.uuid.empty", fieldErrors.get(0).getCode());
	}

	/**
	 * Empty UUID should not be accepted.
	 */
	@Test
	public final void uuidEmpty() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid("");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.uuid.empty", fieldErrors.get(0).getCode());
	}

	/**
	 * UUID sould conform to the Java specification {@link UUID#toString()}.
	 * 
	 * Test that if UUID is not valid, a validation error is thrown.
	 */
	@Test
	public final void uuidNotValid() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid("toto");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.uuid.invalid", fieldErrors.get(0).getCode());
	}

	/**
	 * UUID sould conform to the Java specification {@link UUID#toString()}.
	 * 
	 * Test that if UUID is valid, no validation error is thrown.
	 */
	@Test
	public final void uuidValid() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setUuid(UUID.randomUUID().toString());
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("uuid");
		assertEquals(0, fieldErrors.size());
	}

	/**
	 * Empty value should not be accepted.
	 */
	@Test
	public final void valueEmpty() {
		ProbeValueDto pv = new ProbeValueDto();
		pv.setValue((String) null);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(pv,
				"propertyValue");
		this.validator.validate(pv, errors);
		List<FieldError> fieldErrors = errors.getFieldErrors("value");
		assertEquals(1, fieldErrors.size());
		assertEquals("probeValue.value.empty", fieldErrors.get(0).getCode());
	}
}
