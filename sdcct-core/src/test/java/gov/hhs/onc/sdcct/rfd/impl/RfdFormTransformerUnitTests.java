package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.transform.all" })
public class RfdFormTransformerUnitTests extends AbstractSdcctUnitTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(RfdFormTransformerUnitTests.class);

    @Resource(name = "formRfdTestSdcCapAdrenal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSdcCapAdrenalForm;

    @Resource(name = "rfdTemplateTransformer")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdFormTransformerImpl rfdTemplateTransformer;

    @Test
    public void testFormTransform() throws Exception {
        XdmDocument docOutput = this.rfdTemplateTransformer.transform(testRfdSdcCapAdrenalForm);
        assert docOutput != null;
        LOGGER.debug("Transformed form CapAdrenal from XML to HTML:\n" + docOutput.toString());
    }
}
