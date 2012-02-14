package net.sereneproject.collector;

import net.sereneproject.collector.domain.DomainTests;
import net.sereneproject.collector.dto.DtoTests;
import net.sereneproject.collector.service.impl.ServiceTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Run all unit tests.
 * 
 * @author gehel
 */
@RunWith(Suite.class)
@SuiteClasses({ DomainTests.class, DtoTests.class, ServiceTests.class })
public class AllUnitTests {

}
