package net.sereneproject.collector.web.utils;

import net.sereneproject.collector.domain.ProbeValue;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

/**
 * Test web utils.
 * 
 * @author gehel
 */
public class WebUtilsTest {

    /**
     * Test if serialization of errors is correct.
     */
    @Test
    public final void serializeErrors() {
        BindingResult errors = new BeanPropertyBindingResult(new ProbeValue(),
                "probeValue");
        errors.rejectValue("date", "date.empty");
        errors.rejectValue("value", "value.empty");
        String serializedErrors = WebUtils.toJson(errors);
        Assert.assertTrue("We put a value about date file, "
                + "so we should find it in the serialization",
                serializedErrors.contains("date"));
    }
}
