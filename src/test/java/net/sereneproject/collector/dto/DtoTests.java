package net.sereneproject.collector.dto;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AnalyzerRequestDtoTest.class, AnalyzerResponseDtoTest.class,
        MonitoringMessageTest.class })
public class DtoTests {

}
