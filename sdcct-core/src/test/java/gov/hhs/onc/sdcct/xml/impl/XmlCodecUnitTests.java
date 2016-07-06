package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all" },
    groups = { "sdcct.test.unit.transform.all", "sdcct.test.unit.transform.content.all", "sdcct.test.unit.xml.all", "sdcct.test.unit.xml.codec" })
public class XmlCodecUnitTests extends AbstractSdcctUnitTests {
    @Value("${sdcct.data.form.dir.path}")
    private File testFormDir;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private List<SdcctForm<?>> testForms;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private XmlCodec xmlCodec;

    @Test
    public void testTranscode() throws Exception {
        XmlEncodeOptions defaultTestFormEncOpts = this.xmlCodec.getDefaultEncodeOptions().clone(),
            prettyTestFormEncOpts = defaultTestFormEncOpts.clone().setOption(ContentCodecOptions.PRETTY, true);

        for (SdcctForm<?> testForm : this.testForms) {
            this.transcodeForm(testForm, defaultTestFormEncOpts);
            this.transcodeForm(testForm, prettyTestFormEncOpts);
        }
    }

    private void transcodeForm(SdcctForm<?> testForm, XmlEncodeOptions testFormEncOpts) throws Exception {
        String testFormName = testForm.getName();

        testForm.build();

        // noinspection ConstantConditions
        this.writeTestOutputFile(this.testFormDir,
            (testFormName + (testFormEncOpts.getOption(ContentCodecOptions.PRETTY, false) ? PRETTY_TEST_OUT_FILE_NAME_SUFFIX : StringUtils.EMPTY) +
                FilenameUtils.EXTENSION_SEPARATOR_STR + SdcctFileNameExtensions.XML),
            this.xmlCodec.encode(testForm.getBean(), testFormEncOpts));
    }
}
