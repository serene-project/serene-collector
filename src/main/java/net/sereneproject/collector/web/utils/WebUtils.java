package net.sereneproject.collector.web.utils;

import org.springframework.validation.Errors;

import flexjson.JSONSerializer;

public final class WebUtils {

	private WebUtils() {
	}

	public static String toJson(Errors errors) {
		return new JSONSerializer().exclude("*.class").serialize(
				errors.getAllErrors());
	}
}
