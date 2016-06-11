package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all" }, groups = { "sdcct.test.unit.xml.all", "sdcct.test.unit.xml.codec" })
public class XmlCodecUnitTests extends AbstractSdcctUnitTests {
    private final static String TEST_FORM_OUT_FILE_NAME_SUFFIX = FilenameUtils.EXTENSION_SEPARATOR_STR + SdcctFileNameExtensions.XML;
    private final static String CANONICAL_TEST_FORM_OUT_FILE_NAME_SUFFIX = "_canonical" + TEST_FORM_OUT_FILE_NAME_SUFFIX;
    private final static String PRETTY_TEST_FORM_OUT_FILE_NAME_SUFFIX = "_pretty" + TEST_FORM_OUT_FILE_NAME_SUFFIX;

    private final static Logger LOGGER = LoggerFactory.getLogger(XmlCodecUnitTests.class);

    @Value("${sdcct.data.form.dir.path}")
    private File testFormDir;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private List<SdcctForm<?>> testForms;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private XmlCodec codec;

    @Test
    public void testTranscode() throws Exception {
        if (!this.testFormDir.exists()) {
            Assert.assertTrue(this.testFormDir.mkdir(), String.format("Unable to create test form output directory (path=%s).", this.testFormDir));
        }

        XmlEncodeOptions defaultTestFormEncOpts = this.codec.getDefaultEncodeOptions().clone().setOption(ContentCodecOptions.VALIDATE, true),
            canonicalTestFormEncOpts = defaultTestFormEncOpts.clone().setOption(ContentCodecOptions.CANONICALIZE, true),
            prettyTestFormEncOpts = defaultTestFormEncOpts.clone().setOption(ContentCodecOptions.PRETTY, true);

        for (SdcctForm<?> testForm : this.testForms) {
            this.transcodeForm(testForm, canonicalTestFormEncOpts, CANONICAL_TEST_FORM_OUT_FILE_NAME_SUFFIX);
            this.transcodeForm(testForm, prettyTestFormEncOpts, PRETTY_TEST_FORM_OUT_FILE_NAME_SUFFIX);
        }
    }

    private void transcodeForm(SdcctForm<?> testForm, XmlEncodeOptions testFormEncOpts, String testFormOutFileNameSuffix) throws Exception {
        String testFormName = testForm.getName();
        File testFormOutFile = new File(this.testFormDir, (testFormName + testFormOutFileNameSuffix));

        Assert.assertFalse(testFormOutFile.exists(), String.format("Test form output file (path=%s) already exists.", testFormOutFile));

        String transcodedTestFormContentStr =
            new String(this.codec.encode(this.codec.decode(testForm.getDocument().getSource(), testForm.getBeanImplClass(), null), testFormEncOpts),
                StandardCharsets.UTF_8);

        FileUtils.write(testFormOutFile, transcodedTestFormContentStr, StandardCharsets.UTF_8, false);

        // LOGGER.trace(String.format("Transcoded test form (name=%s) to file (path=%s):\n%s", testFormName, testFormOutFile, transcodedTestFormContentStr));
    }
}
