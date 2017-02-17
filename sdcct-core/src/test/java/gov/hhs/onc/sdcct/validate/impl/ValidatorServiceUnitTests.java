package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.fhir.form.FhirForm;
import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.nio.charset.StandardCharsets;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.transform.all" },
    groups = { "sdcct.test.unit.validate.all", "sdcct.test.unit.validate.service" })
public class ValidatorServiceUnitTests extends AbstractSdcctUnitTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceUnitTests.class);

    @Resource(name = "formFhirA")
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

    @Resource(name = "formRfdA")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdForm;

    @Resource(name = "formRfdTestInvalidXml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdInvalidXmlForm;

    /**
     * TODO: Re-enable if/when the SDC Schematrons' SDC XML namespace URIs are updated.
     */
    // region region
    // @formatter:off
    /*
    @Resource(name = "formRfdTestSchematron")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private RfdForm testRfdSchematronForm;
    */
    // @formatter:on
    // endregion

    @Resource(name = "validatorServiceFhir")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctValidatorService fhirValidatorService;

    @Resource(name = "validatorServiceRfd")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctValidatorService rfdValidatorService;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private XmlCodec xmlCodec;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctXmlInputFactory xmlInFactory;

    @Test(dependsOnMethods = { "testValidateMalformedXml" })
    public void testValidateBadCharsXml() throws Exception {
        buildForm(this.testFhirBadCharsXmlForm, false);
    }

    @Test(dependsOnMethods = { "testValidateInvalidXml" })
    public void testValidateMalformedXml() throws Exception {
        buildForm(this.testFhirMalformedXmlForm, false);
    }

    @Test(dependsOnMethods = { "testValidateValid" })
    public void testValidateInvalidXml() throws Exception {
        this.validateForm(this.fhirValidatorService, buildForm(this.testFhirInvalidXmlForm, true), false);

        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdInvalidXmlForm, true), false);
    }

    /**
     * TODO: Re-enable if/when the SDC Schematrons' SDC XML namespace URIs are updated.
     */
    // region region
    // @formatter:off
    /*
    @Test(dependsOnMethods = { "testValidateValid" })
    public void testValidateSchematron() throws Exception {
        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdSchematronForm, true), false);
    }
    */
    // @formatter:on
    // endregion

    @Test
    public void testValidateValid() throws Exception {
        this.validateForm(this.fhirValidatorService, buildForm(this.testFhirForm, true), true);

        this.validateForm(this.rfdValidatorService, buildForm(this.testRfdForm, true), true);
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
            validatorService.validate(this.xmlInFactory.createXMLStreamReader(testForm.getDocument().getSource()));
        } catch (ValidationException e) {
            String testFormValidationResultStr =
                new String(this.xmlCodec.encode(e.getResult(), this.xmlCodec.getDefaultEncodeOptions().setOption(ContentCodecOptions.PRETTY, true)),
                    StandardCharsets.UTF_8);

            LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was invalid:\n%s", testFormName, testFormIdentifier, testFormValidationResultStr),
                e);

            Assert.assertFalse(validExpected, String.format("Test form (name=%s, identifier=%s) was expected to be valid:\n%s", testFormName,
                testFormIdentifier, testFormValidationResultStr));

            return;
        }

        LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was valid.", testFormName, testFormIdentifier));

        Assert.assertTrue(validExpected, String.format("Test form (name=%s, identifier=%s) was expected to be invalid.", testFormName, testFormIdentifier));
    }
}
