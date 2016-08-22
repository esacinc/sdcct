package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.rfd.RfdFormTransformer;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.transform.all" })
public class RfdFormTransformerUnitTests extends AbstractSdcctUnitTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(RfdFormTransformerUnitTests.class);

    @Resource(name = "formRfdTestSdcCapAdrenal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSdcCapAdrenalForm;

    @Resource(name = "rfdTemplateTransformer")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdFormTransformer rfdTemplateTransformer;

    @Test
    public void testFormTransform() throws Exception {
        String testFormName = testRfdSdcCapAdrenalForm.getName(), testFormIdentifier = testRfdSdcCapAdrenalForm.getIdentifier();
        XdmDocument docOutput = null;
        try {
            docOutput = this.rfdTemplateTransformer.transform(testRfdSdcCapAdrenalForm);
        } catch (Exception e) {
            LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was expected to be transformable.", testFormName, testFormIdentifier), e);
        }
        Assert.assertNotNull(docOutput,
            String.format("Test form (name=%s, identifier=%s) was expected to be transformable.", testFormName, testFormIdentifier));
        LOGGER.trace(String.format("Transformed form (name=%s, identifier=%s) from XML to HTML:\n%s", testFormName, testFormIdentifier, docOutput.toString()));
    }
}
