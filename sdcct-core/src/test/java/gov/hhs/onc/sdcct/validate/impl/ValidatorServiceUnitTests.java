package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.validate.ValidationException;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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

    @Resource(name = "formFhirTestBadCharsXml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirForm testFhirBadCharsXmlForm;

    @Resource(name = "formFhirTestInvalidXml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirForm testFhirInvalidXmlForm;

    @Resource(name = "formFhirTestMalformedXml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirForm testFhirMalformedXmlForm;

    @Resource(name = "formRfd1a")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdForm;

    @Resource(name = "formRfdTestSdcCapAdrenal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSdcCapAdrenalForm;

    @Resource(name = "formRfdTestSdcCapBreastBmk")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSdcCapBreastBmkForm;

    @Resource(name = "formRfdTestSdcCapBreastRes")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSdcCapBreastResForm;

    @Resource(name = "formRfdTestSdcCtLungScreeningTemplate")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSdcCtLungScreeningTemplateForm;

    @Resource(name = "formRfdTestInvalidXml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdInvalidXmlForm;

    @Resource(name = "formRfdTestSchematron")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSchematronForm;

    @Resource(name = "validatorServiceFhir")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctValidatorService fhirValidatorService;

    @Resource(name = "validatorServiceRfd")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctValidatorService rfdValidatorService;

    @Test(dependsOnMethods = { "testValidateMalformedXml" })
    public void testValidateBadCharsXml() throws Exception {
        buildForm(this.testFhirBadCharsXmlForm, false);
    }

    @Test(dependsOnMethods = { "testValidateInvalidXml" })
    public void testValidateMalformedXml() throws Exception {
        buildForm(this.testFhirMalformedXmlForm, false);
    }

    @Test(dependsOnMethods = { "testValidateSchematron" })
    public void testValidateInvalidXml() throws Exception {
        this.validateForm(this.fhirValidatorService, buildForm(this.testFhirInvalidXmlForm, true), false);

        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdInvalidXmlForm, true), false);
    }

    @Test(dependsOnMethods = { "testValidateValid" })
    public void testValidateSchematron() throws Exception {
        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdSchematronForm, true), false);
    }

    @Test
    public void testValidateValid() throws Exception {
        this.validateForm(this.fhirValidatorService, buildForm(this.testFhirForm, true), true);

        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdForm, true), true);
        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdSdcCapAdrenalForm, true), true);
        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdSdcCapBreastBmkForm, true), true);
        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdSdcCapBreastResForm, true), true);
        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdSdcCtLungScreeningTemplateForm, true), true);
    }

    private static SdcctForm<?> buildForm(SdcctForm<?> testForm, boolean buildableExpected) throws Exception {
        String testFormName = testForm.getName(), testFormIdentifier = testForm.getIdentifier();

        if (testForm.hasDocument()) {
            return testForm;
        }

        try {
            testForm.build();
        } catch (Exception e) {
            LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was not buildable.", testFormName, testFormIdentifier), e);

            Assert.assertFalse(buildableExpected,
                String.format("Test form (name=%s, identifier=%s) was expected to be buildable.", testFormName, testFormIdentifier));

            return testForm;
        }

        Assert.assertTrue(buildableExpected,
            String.format("Test form (name=%s, identifier=%s) was expected to not be buildable.", testFormName, testFormIdentifier));

        return testForm;
    }

    private void validateForm(SdcctValidatorService validatorService, SdcctForm<?> testForm, boolean validExpected) throws Exception {
        String testFormName = testForm.getName(), testFormIdentifier = testForm.getIdentifier();

        try {
            // noinspection ConstantConditions
            validatorService.validate(testForm.getDocument());
        } catch (ValidationException e) {
            LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was invalid:%s", testFormName, testFormIdentifier,
                e.getIssues().stream().map(testFormIssue -> String.format("\n{%s}", testFormIssue)).collect(Collectors.joining(StringUtils.EMPTY))), e);

            Assert.assertFalse(validExpected, String.format("Test form (name=%s, identifier=%s) was expected to be valid.", testFormName, testFormIdentifier));

            return;
        }

        LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was valid.", testFormName, testFormIdentifier));

        Assert.assertTrue(validExpected, String.format("Test form (name=%s, identifier=%s) was expected to be invalid.", testFormName, testFormIdentifier));
    }
}
