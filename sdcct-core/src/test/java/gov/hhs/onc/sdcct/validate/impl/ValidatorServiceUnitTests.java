package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.validate.ValidationException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.transform.all" },
    groups = { "sdcct.test.unit.validate.all", "sdcct.test.unit.validate.service" })
public class ValidatorServiceUnitTests extends AbstractSdcctUnitTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceUnitTests.class);

    @Resource(name = "formFhir1a")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirForm testFhirForm;

    @Resource(name = "formFhirTestInvalidXml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirForm testFhirInvalidXmlForm;

    @Resource(name = "formRfd1a")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdForm;

    @Resource(name = "formRfdTestInvalidXml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdInvalidXmlForm;

    @Resource(name = "validatorServiceFhir")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctValidatorService fhirValidatorService;

    @Resource(name = "validatorServiceRfd")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctValidatorService rfdValidatorService;

    @Test
    public void testValidate() throws Exception {
        this.validateForm(this.fhirValidatorService, this.testFhirForm, true);
        this.validateForm(this.fhirValidatorService, this.testFhirInvalidXmlForm, false);

        this.validateForm(this.rfdValidatorService, this.testRfdForm, true);
        this.validateForm(this.rfdValidatorService, this.testRfdInvalidXmlForm, false);
    }

    private void validateForm(SdcctValidatorService validatorService, SdcctForm<?> testForm, boolean validExpected) throws Exception {
        String testFormIdentifier = testForm.getIdentifier();

        try {
            // noinspection ConstantConditions
            validatorService.validate(testForm.getDocument().getUnderlyingNode());
        } catch (ValidationException e) {
            LOGGER.trace(String.format("Test form (identifier=%s) was invalid.", testFormIdentifier), e);

            Assert.assertFalse(validExpected, String.format("Test form (identifier=%s) was expected to be valid.", testFormIdentifier));

            return;
        }

        LOGGER.trace(String.format("Test form (identifier=%s) was valid.", testFormIdentifier));

        Assert.assertTrue(validExpected, String.format("Test form (identifier=%s) was expected to be invalid.", testFormIdentifier));
    }
}
