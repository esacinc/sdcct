package gov.hhs.onc.sdcct.test.impl;

import gov.hhs.onc.sdcct.context.impl.SdcctApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(classes = { SdcctApplicationConfiguration.class }, loader = SdcctApplicationContextLoader.class)
@Test(groups = { "sdcct.test.all" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class }, inheritListeners = false)
public abstract class AbstractSdcctTests extends AbstractTestNGSpringContextTests {
}
