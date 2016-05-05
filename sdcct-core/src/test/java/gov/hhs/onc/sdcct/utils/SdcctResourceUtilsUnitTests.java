package gov.hhs.onc.sdcct.utils;

import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.test.impl.AbstractSdcctUnitTests;
import java.net.MalformedURLException;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.unit.utils.all", "sdcct.test.unit.utils.resource" })
public class SdcctResourceUtilsUnitTests extends AbstractSdcctUnitTests {
    private final static String TEST_RESOURCE_JAR_FILE_PATH_PREFIX = "lib/sdcct";
    private final static String TEST_RESOURCE_PATH_PREFIX = SdcctResourceUtils.META_INF_APP_PATH_PREFIX + "spring/spring-sdcct";

    private final static String TEST_RESOURCE_PATH_SUFFIX = FilenameUtils.EXTENSION_SEPARATOR + SdcctFileNameExtensions.XML;

    private final static String TEST_RESOURCE_PATH_1 = TEST_RESOURCE_PATH_PREFIX + TEST_RESOURCE_PATH_SUFFIX;
    private final static FileSystemResource TEST_FILE_SYS_RESOURCE_1 = new FileSystemResource(TEST_RESOURCE_PATH_1);
    private final static UrlResource TEST_JAR_FILE_RESOURCE_1 = buildTestJarFileResource(TEST_RESOURCE_JAR_FILE_PATH_PREFIX, TEST_RESOURCE_PATH_1);

    private final static String TEST_RESOURCE_PATH_2 = TEST_RESOURCE_PATH_PREFIX + SdcctResourceUtils.TEST_FILE_NAME_SUFFIX + TEST_RESOURCE_PATH_SUFFIX;
    private final static FileSystemResource TEST_FILE_SYS_RESOURCE_2 = new FileSystemResource(TEST_RESOURCE_PATH_2);
    private final static UrlResource TEST_JAR_FILE_RESOURCE_2 = buildTestJarFileResource(
        (TEST_RESOURCE_JAR_FILE_PATH_PREFIX + SdcctResourceUtils.TEST_FILE_NAME_SUFFIX), TEST_RESOURCE_PATH_2);

    private final static String TEST_RESOURCE_PATH_3 = TEST_RESOURCE_PATH_PREFIX + "-data" + TEST_RESOURCE_PATH_SUFFIX;
    private final static FileSystemResource TEST_FILE_SYS_RESOURCE_3 = new FileSystemResource(TEST_RESOURCE_PATH_3);
    private final static UrlResource TEST_JAR_FILE_RESOURCE_3 = buildTestJarFileResource((TEST_RESOURCE_JAR_FILE_PATH_PREFIX + "-data"), TEST_RESOURCE_PATH_3);

    private final static Resource[] TEST_RESOURCES_INITIAL = ArrayUtils.toArray(TEST_FILE_SYS_RESOURCE_3, TEST_JAR_FILE_RESOURCE_3, TEST_FILE_SYS_RESOURCE_2,
        TEST_JAR_FILE_RESOURCE_2, TEST_JAR_FILE_RESOURCE_1, TEST_FILE_SYS_RESOURCE_1);

    private final static Resource[] TEST_RESOURCES_SORTED = ArrayUtils.toArray(TEST_JAR_FILE_RESOURCE_1, TEST_FILE_SYS_RESOURCE_1, TEST_JAR_FILE_RESOURCE_2,
        TEST_FILE_SYS_RESOURCE_2, TEST_JAR_FILE_RESOURCE_3, TEST_FILE_SYS_RESOURCE_3);

    @Test
    public void testSort() throws Exception {
        Resource[] testResources = ArrayUtils.clone(TEST_RESOURCES_INITIAL);

        Arrays.sort(testResources, SdcctResourceUtils.LOC_COMPARATOR);

        Assert.assertEquals(
            testResources,
            TEST_RESOURCES_SORTED,
            String.format("Unable to sort resources: expected=[%s], actual=[%s]", StringUtils.join(TEST_RESOURCES_SORTED, "; "),
                StringUtils.join(testResources, "; ")));
    }

    private static UrlResource buildTestJarFileResource(String testJarFilePath, String testResourcePath) {
        String testResourceUrlPath =
            SdcctResourceUtils.JAR_FILE_PATH_PREFIX + testJarFilePath + FilenameUtils.EXTENSION_SEPARATOR + SdcctFileNameExtensions.JAR
                + SdcctStringUtils.APOS + testResourcePath;

        try {
            return new UrlResource(testResourceUrlPath);
        } catch (MalformedURLException e) {
            throw new ApplicationContextException(String.format("Unable to build test Spring URL resource (path=%s).", testResourceUrlPath), e);
        }
    }
}
