package gov.hhs.onc.sdcct.xml.html.impl;

import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.xml.html.HtmlTranscodeOptions;
import gov.hhs.onc.sdcct.xml.xslt.saxon.impl.SdcctXsltExecutable;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.xml.codec" }, groups = { "sdcct.test.unit.transform.all",
    "sdcct.test.unit.transform.content.all", "sdcct.test.unit.xml.all", "sdcct.test.unit.xml.html.all", "sdcct.test.unit.xml.html.transcoder" })
public class HtmlTranscoderUnitTests extends AbstractSdcctUnitTests {
    @Value("${sdcct.test.data.form.dir}")
    private File testFormDir;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private List<RfdForm> testForms;

    @Resource(name = "xsltExecRfdFormHtml")
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private SdcctXsltExecutable testFormHtmlXsltExec;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection", "SpringJavaAutowiredMembersInspection" })
    private HtmlTranscoder htmlTranscoder;

    @Test
    public void testTranscode() throws Exception {
        HtmlTranscodeOptions defaultTestFormTranscodeOpts = this.htmlTranscoder.getDefaultTranscodeOptions().clone(),
            prettyTestFormTranscodeOpts = defaultTestFormTranscodeOpts.clone().setOption(ContentCodecOptions.PRETTY, true);

        for (RfdForm testForm : this.testForms) {
            this.transcodeForm(testForm, defaultTestFormTranscodeOpts);
            this.transcodeForm(testForm, prettyTestFormTranscodeOpts);
        }
    }

    private byte[] transcodeForm(RfdForm testForm, HtmlTranscodeOptions testFormTranscodeOpts) throws Exception {
        String testFormName = testForm.getName();
        byte[] transcodedTestFormContent = this.htmlTranscoder.transcode(this.testFormHtmlXsltExec, testForm.getSource(), testFormTranscodeOpts);

        // noinspection ConstantConditions
        this.writeTestFile(this.testFormDir,
            (testFormName + (testFormTranscodeOpts.getOption(ContentCodecOptions.PRETTY, false) ? PRETTY_TEST_OUT_FILE_NAME_SUFFIX : StringUtils.EMPTY) +
                FilenameUtils.EXTENSION_SEPARATOR_STR + SdcctFileNameExtensions.HTML),
            transcodedTestFormContent);

        return transcodedTestFormContent;
    }
}
