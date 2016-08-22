package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.form.transform.FormTransformer;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.transform.all" })
public class RfdFormTransformerUnitTests extends AbstractSdcctUnitTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(RfdFormTransformerUnitTests.class);

    @Resource(name = "formRfdTestSdcCapAdrenal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSdcCapAdrenalForm;

    @Resource(name = "RfdTemplateTransformer")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FormTransformer<RfdForm> rfdTemplateTransformer;

    @Test
    public void testFormTransform() throws Exception {
        LOGGER.debug(this.rfdTemplateTransformer.transform(testRfdSdcCapAdrenalForm).toString());
    }
}

