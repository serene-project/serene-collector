package net.sereneproject.collector.validation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tun all validators tests.
 * 
 * @author gehel
 */
@RunWith(Suite.class)
@SuiteClasses({ MonitoringMessageDtoValidatorTest.class,
		ProbeValueDtoValidatorTest.class })
public class ValidationTests {

}
