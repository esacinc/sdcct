package gov.hhs.onc.sdcct.json.xml.impl;

import gov.hhs.onc.sdcct.fhir.form.FhirForm;
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.json.JsonEncodeOptions;
import gov.hhs.onc.sdcct.json.impl.JsonCodec;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctSerializer;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlOutputFactory;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlStreamWriter;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.xml.all", "sdcct.test.unit.json.codec" }, groups = { "sdcct.test.unit.transform.all",
    "sdcct.test.unit.transform.content.all", "sdcct.test.unit.json.all", "sdcct.test.unit.json.xml.all", "sdcct.test.unit.json.xml.transcoder" })
public class XmlTranscoderUnitTests extends AbstractSdcctUnitTests {
    private final static String TRANSCODED_TEST_OUT_FILE_NAME_SUFFIX = "_transcoded";

    private final static Logger LOGGER = LoggerFactory.getLogger(XmlTranscoderUnitTests.class);

    @Value("${sdcct.test.data.form.dir}")
    private File testFormDir;

    @Resource(name = "formFhirTestTranscode")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private FhirForm testFhirTranscodeForm;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private List<FhirForm> testForms;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private XmlTranscoder xmlTranscoder;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private JsonCodec jsonCodec;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private XmlCodec xmlCodec;

    @Resource(name = "serializerXmlPretty")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctSerializer prettySerializer;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctXmlOutputFactory xmlOutFactory;

    @Test
    public void testTranscode() throws Exception {
        JsonEncodeOptions testFormJsonEncOpts = this.jsonCodec.getDefaultEncodeOptions().clone().setOption(ContentCodecOptions.PRETTY, true);
        XmlEncodeOptions defaultTestFormXmlEncOpts = this.xmlCodec.getDefaultEncodeOptions().clone(),
            prettyTestFormXmlEncOpts = defaultTestFormXmlEncOpts.clone().setOption(ContentCodecOptions.PRETTY, true);
        byte[] testFormContent;

        for (FhirForm testForm : this.testForms) {
            this.transcodeForm(testForm, (testFormContent = this.jsonCodec.encode(testForm.getBean(), testFormJsonEncOpts)), defaultTestFormXmlEncOpts, true);
            this.transcodeForm(testForm, testFormContent, prettyTestFormXmlEncOpts, true);
        }

        this.transcodeForm(this.testFhirTranscodeForm,
            (testFormContent = this.jsonCodec.encode(this.testFhirTranscodeForm.build().getBean(), testFormJsonEncOpts)), defaultTestFormXmlEncOpts, true);
        this.transcodeForm(this.testFhirTranscodeForm, testFormContent, prettyTestFormXmlEncOpts, true);
    }

    private void transcodeForm(FhirForm testForm, byte[] testFormJsonContent, XmlEncodeOptions testFormXmlEncOpts, boolean transcodableExpected)
        throws Exception {
        String testFormName = testForm.getName(), testFormIdentifier = testForm.getIdentifier();
        ByteArrayResult testFormResult = new ByteArrayResult();
        SdcctXmlStreamWriter resultWriter = this.xmlOutFactory.createXMLStreamWriter(testFormResult);

        try {
            this.xmlTranscoder.transcode(testFormJsonContent, testForm.getBeanImplClass(), resultWriter);

            byte[] transcodedTestFormContent = testFormResult.getBytes();

            // noinspection ConstantConditions
            if (testFormXmlEncOpts.getOption(ContentCodecOptions.PRETTY, false)) {
                transcodedTestFormContent = this.prettySerializer.serialize(new ByteArraySource(transcodedTestFormContent), null, null);
            }

            // noinspection ConstantConditions
            this.writeTestFile(this.testFormDir,
                (testFormName + TRANSCODED_TEST_OUT_FILE_NAME_SUFFIX +
                    (testFormXmlEncOpts.getOption(ContentCodecOptions.PRETTY, false) ? PRETTY_TEST_OUT_FILE_NAME_SUFFIX : StringUtils.EMPTY) +
                    FilenameUtils.EXTENSION_SEPARATOR_STR + SdcctFileNameExtensions.XML),
                transcodedTestFormContent);
        } catch (Exception e) {
            LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was not transcoded.", testFormName, testFormIdentifier), e);

            Assert.assertFalse(transcodableExpected,
                String.format("Test form (name=%s, identifier=%s) was expected to be transcodable.", testFormName, testFormIdentifier));

            return;
        }

        LOGGER.trace(String.format("Test form (name=%s, identifier=%s) was transcoded.", testFormName, testFormIdentifier));

        Assert.assertTrue(transcodableExpected,
            String.format("Test form (name=%s, identifier=%s) was expected to not be transcodable.", testFormName, testFormIdentifier));
    }
}
