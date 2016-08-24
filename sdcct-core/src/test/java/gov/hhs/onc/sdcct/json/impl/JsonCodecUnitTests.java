package gov.hhs.onc.sdcct.json.impl;

import gov.hhs.onc.sdcct.fhir.Identifier;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.form.FhirForm;
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.json.JsonDecodeOptions;
import gov.hhs.onc.sdcct.json.JsonEncodeOptions;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.xml.all" },
    groups = { "sdcct.test.unit.transform.all", "sdcct.test.unit.transform.content.all", "sdcct.test.unit.json.all", "sdcct.test.unit.json.codec" })
public class JsonCodecUnitTests extends AbstractSdcctUnitTests {
    @Value("${sdcct.test.data.form.dir}")
    private File testFormDir;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private List<FhirForm> testForms;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private JsonCodec jsonCodec;

    private Map<String, byte[]> testFormContent;
    private Map<String, byte[]> testFormPrettyContent;

    @Test(enabled = false, dependsOnMethods = { "testEncode" })
    public void testDecode() throws Exception {
        JsonDecodeOptions defaultTestFormDecodeOpts = this.jsonCodec.getDefaultDecodeOptions().clone();
        String testFormName, testFormDecodeIdentifier, testFormIdentifier;
        List<Identifier> testFormDecodeIdentifiers;
        int numTestFormDecodeIdentifiers;

        for (FhirForm testForm : this.testForms) {
            Assert.assertEquals(
                (numTestFormDecodeIdentifiers = CollectionUtils.size((testFormDecodeIdentifiers =
                    this.decodeForm(testForm, defaultTestFormDecodeOpts, this.testFormContent.get((testFormName = testForm.getName()))).getIdentifiers()))),
                1, String.format("Number of encoded test form (name=%s) identifiers do not match: %d != 1", testFormName, numTestFormDecodeIdentifiers));

            Assert.assertEquals((testFormDecodeIdentifier = testFormDecodeIdentifiers.get(0).getValue().getValue()),
                (testFormIdentifier = testForm.getIdentifier()),
                String.format("Encoded test form (name=%s) identifiers do not match: %s != %s", testFormName, testFormDecodeIdentifier, testFormIdentifier));

            Assert.assertEquals(
                (numTestFormDecodeIdentifiers = CollectionUtils.size((testFormDecodeIdentifiers =
                    this.decodeForm(testForm, defaultTestFormDecodeOpts, this.testFormPrettyContent.get(testFormName)).getIdentifiers()))),
                1, String.format("Number of pretty encoded test form (name=%s) identifiers do not match: %d != 1", testFormName, numTestFormDecodeIdentifiers));

            Assert.assertEquals((testFormDecodeIdentifier = testFormDecodeIdentifiers.get(0).getValue().getValue()), testFormIdentifier, String
                .format("Pretty encoded test form (name=%s) identifiers do not match: %s != %s", testFormName, testFormDecodeIdentifier, testFormIdentifier));
        }
    }

    @Test
    public void testEncode() throws Exception {
        this.testFormContent = new LinkedHashMap<>(this.testForms.size());
        this.testFormPrettyContent = new LinkedHashMap<>(this.testForms.size());

        JsonEncodeOptions defaultTestFormEncOpts = this.jsonCodec.getDefaultEncodeOptions().clone(),
            prettyTestFormEncOpts = defaultTestFormEncOpts.clone().setOption(ContentCodecOptions.PRETTY, true);
        String testFormName;

        for (FhirForm testForm : this.testForms) {
            this.testFormContent.put((testFormName = testForm.getName()), this.encodeForm(testForm, defaultTestFormEncOpts));
            this.testFormPrettyContent.put(testFormName, this.encodeForm(testForm, prettyTestFormEncOpts));
        }
    }

    private Questionnaire decodeForm(FhirForm testForm, JsonDecodeOptions testFormDecodeOpts, byte ... testFormContent) throws Exception {
        return this.jsonCodec.decode(testFormContent, testForm.getBeanImplClass(), testFormDecodeOpts);
    }

    private byte[] encodeForm(FhirForm testForm, JsonEncodeOptions testFormEncOpts) throws Exception {
        String testFormName = testForm.getName();
        byte[] testFormContent = this.jsonCodec.encode(testForm.getBean(), testFormEncOpts);

        // noinspection ConstantConditions
        this.writeTestFile(this.testFormDir,
            (testFormName + (testFormEncOpts.getOption(ContentCodecOptions.PRETTY, false) ? PRETTY_TEST_OUT_FILE_NAME_SUFFIX : StringUtils.EMPTY) +
                FilenameUtils.EXTENSION_SEPARATOR_STR + SdcctFileNameExtensions.JSON),
            testFormContent);

        return testFormContent;
    }
}
