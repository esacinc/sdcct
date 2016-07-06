package gov.hhs.onc.sdcct.test.impl;

import gov.hhs.onc.sdcct.context.impl.SdcctApplicationConfiguration;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(classes = { SdcctApplicationConfiguration.class }, loader = SdcctApplicationContextLoader.class)
@Test(groups = { "sdcct.test.all" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class }, inheritListeners = false)
public abstract class AbstractSdcctTests extends AbstractTestNGSpringContextTests {
    protected final static String PRETTY_TEST_OUT_FILE_NAME_SUFFIX = "_pretty";

    public void initializeTestContext() {
        try {
            this.springTestContextBeforeTestClass();
            this.springTestContextPrepareTestInstance();
        } catch (Exception e) {
            throw new ApplicationContextException(String.format("Unable to initialize TestNG test instance (class=%s).", this.getClass().getName()), e);
        }
    }

    protected void writeTestOutputFile(File testOutDir, String testOutFileName, byte ... testOutContent) throws Exception {
        File testOutFile = new File(testOutDir, testOutFileName);

        if (!testOutDir.exists()) {
            Assert.assertTrue(testOutDir.mkdir(), String.format("Unable to create test output directory (path=%s).", testOutDir.getPath()));
        } else {
            Assert.assertFalse(testOutFile.exists(), String.format("Test output file (path=%s) already exists.", testOutFile));
        }

        FileUtils.writeByteArrayToFile(testOutFile, testOutContent);
    }
}
