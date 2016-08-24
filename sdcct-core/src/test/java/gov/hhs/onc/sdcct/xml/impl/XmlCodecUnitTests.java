package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.xml.XmlDecodeOptions;
import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all" },
    groups = { "sdcct.test.unit.transform.all", "sdcct.test.unit.transform.content.all", "sdcct.test.unit.xml.all", "sdcct.test.unit.xml.codec" })
public class XmlCodecUnitTests extends AbstractSdcctUnitTests {
    @Value("${sdcct.test.data.form.dir}")
    private File testFormDir;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private List<SdcctForm<?>> testForms;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private XmlCodec xmlCodec;

    private Map<String, byte[]> testFormContent;
    private Map<String, byte[]> testFormPrettyContent;

    @Test(dependsOnMethods = { "testEncode" })
    public void testDecode() throws Exception {
        XmlDecodeOptions defaultTestFormDecodeOpts = this.xmlCodec.getDefaultDecodeOptions().clone();
        String testFormName;

        for (SdcctForm<?> testForm : this.testForms) {
            this.decodeForm(testForm, defaultTestFormDecodeOpts, this.testFormContent.get((testFormName = testForm.getName())));
            this.decodeForm(testForm, defaultTestFormDecodeOpts, this.testFormPrettyContent.get(testFormName));
        }

        // TODO: Implement common content codec test helper methods.
        // @formatter:off
        /*
        String testFormName, testFormDecodeIdentifier, testFormIdentifier;
        List<Identifier> testFormDecodeIdentifiers;
        int numTestFormDecodeIdentifiers;

        for (SdcctForm<?> testForm : this.testForms) {
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
        */
        // @formatter:on
    }

    @Test
    public void testEncode() throws Exception {
        this.testFormContent = new LinkedHashMap<>(this.testForms.size());
        this.testFormPrettyContent = new LinkedHashMap<>(this.testForms.size());

        XmlEncodeOptions defaultTestFormEncOpts = this.xmlCodec.getDefaultEncodeOptions().clone(),
            prettyTestFormEncOpts = defaultTestFormEncOpts.clone().setOption(ContentCodecOptions.PRETTY, true);
        String testFormName;

        for (SdcctForm<?> testForm : this.testForms) {
            this.testFormContent.put((testFormName = testForm.getName()), this.encodeForm(testForm, defaultTestFormEncOpts));
            this.testFormPrettyContent.put(testFormName, this.encodeForm(testForm, prettyTestFormEncOpts));
        }
    }

    private Object decodeForm(SdcctForm<?> testForm, XmlDecodeOptions testFormDecodeOpts, byte ... testFormContent) throws Exception {
        return this.xmlCodec.decode(testFormContent, testForm.getBeanImplClass(), testFormDecodeOpts);
    }

    private byte[] encodeForm(SdcctForm<?> testForm, XmlEncodeOptions testFormEncOpts) throws Exception {
        testForm.build();

        String testFormName = testForm.getName();
        byte[] testFormContent = this.xmlCodec.encode(testForm.getBean(), testFormEncOpts);

        // noinspection ConstantConditions
        this.writeTestFile(this.testFormDir,
            (testFormName + (testFormEncOpts.getOption(ContentCodecOptions.PRETTY, false) ? PRETTY_TEST_OUT_FILE_NAME_SUFFIX : StringUtils.EMPTY) +
                FilenameUtils.EXTENSION_SEPARATOR_STR + SdcctFileNameExtensions.XML),
            testFormContent);

        return testFormContent;
    }
}
