package gov.hhs.onc.sdcct.web.test.impl;

import gov.hhs.onc.sdcct.context.impl.SdcctApplicationConfiguration;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctItTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(classes = { SdcctApplicationConfiguration.class }, loader = SdcctWebApplicationContextLoader.class)
@Test(groups = { "sdcct.test.web.all", "sdcct.test.it.web.all" })
public abstract class AbstractSdcctWebItTests extends AbstractSdcctItTests {
}
