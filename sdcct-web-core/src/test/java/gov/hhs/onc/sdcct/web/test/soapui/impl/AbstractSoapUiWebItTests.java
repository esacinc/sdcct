package gov.hhs.onc.sdcct.web.test.soapui.impl;

import ch.qos.logback.ext.spring.ApplicationContextHolder;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.http.HttpClientSupport;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCaseRunner;
import com.eviware.soapui.impl.wsdl.teststeps.HttpTestRequestStep;
import com.eviware.soapui.model.propertyexpansion.PropertyExpander;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.Assertable.AssertionStatus;
import com.eviware.soapui.model.testsuite.AssertionError;
import com.eviware.soapui.model.testsuite.TestAssertion;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestRunner.Status;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.factory.impl.EmbeddedPlaceholderResolver;
import gov.hhs.onc.sdcct.transform.ResourceSourceResolver;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebItTests;
import gov.hhs.onc.sdcct.web.test.soapui.SdcctSoapUiException;
import gov.hhs.onc.sdcct.web.test.soapui.SoapUiTest;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebItTests.SoapUiMethodInterceptor;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.xmlbeans.XmlError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.internal.MethodInstance;
import org.testng.internal.TestNGMethod;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

@Listeners({ SoapUiMethodInterceptor.class })
@Test(groups = { "sdcct.test.it.web.soapui.all" })
public abstract class AbstractSoapUiWebItTests extends AbstractSdcctWebItTests {
    public static class SoapUiMethodInterceptor implements IMethodInterceptor {
        @Override
        @SuppressWarnings({ CompilerWarnings.UNCHECKED })
        public List<IMethodInstance> intercept(List<IMethodInstance> initialMethodInstances, ITestContext testContext) {
            if (initialMethodInstances.isEmpty()) {
                return initialMethodInstances;
            }

            ApplicationContext appContext;
            IAnnotationFinder annoFinder = testContext.getSuite().getAnnotationFinder();
            List<IMethodInstance> methodInstances = new ArrayList<>();
            ITestNGMethod initialMethodModel, methodModel;
            Method method;
            ITestClass testClass;
            XmlTest xmlTest;
            AbstractSoapUiWebItTests initialInstance, instance;
            boolean soapUiInitialized = false;
            Class<? extends AbstractSoapUiWebItTests> instanceClass;
            Constructor<? extends AbstractSoapUiWebItTests> instanceConstructor;
            String projectFileLoc, projectName, suiteName;
            WsdlProject project;
            Logger logger;

            for (IMethodInstance initialMethodInstance : initialMethodInstances) {
                method = (initialMethodModel = initialMethodInstance.getMethod()).getConstructorOrMethod().getMethod();
                testClass = initialMethodModel.getTestClass();
                xmlTest = initialMethodModel.getXmlTest();

                (initialInstance = ((AbstractSoapUiWebItTests) initialMethodInstance.getInstance())).initializeTestContext();

                instanceClass = initialInstance.getClass();

                try {
                    instanceConstructor = instanceClass.getConstructor();
                } catch (NoSuchMethodException e) {
                    throw new SdcctSoapUiException(
                        String.format("Unable to determine SoapUI project TestNG tests instance constructor (class=%s).", instanceClass.getName()), e);
                }

                projectFileLoc = (ResourceUtils.FILE_URL_PREFIX + (appContext = ApplicationContextHolder.getApplicationContext())
                    .getBean(EmbeddedPlaceholderResolver.class).resolvePlaceholders(instanceClass.getAnnotation(SoapUiTest.class).projectFile()));

                // noinspection ConstantConditions
                try (InputStream projectFileInStream = appContext.getBean(ResourceSourceResolver.class).resolve(projectFileLoc).getInputStream()) {
                    project = new WsdlProject(projectFileInStream, null);
                } catch (Exception e) {
                    throw new SdcctSoapUiException(String.format("Unable to build SoapUI project (loc=%s).", projectFileLoc), e);
                }

                projectName = project.getName();

                logger = LoggerFactory.getLogger(instanceClass);

                if (project.isDisabled()) {
                    logger.debug(String.format("Skipping disabled SoapUI project (name=%s).", projectName));

                    continue;
                }

                if (!soapUiInitialized) {
                    HttpClientSupport.getHttpClient().setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));

                    PropertyExpander.getDefaultExpander().addResolver(appContext.getBean(SdcctSoapUiPropertyResolver.class));

                    soapUiInitialized = true;
                }

                for (TestSuite suite : project.getTestSuiteList()) {
                    suiteName = suite.getName();

                    if (suite.isDisabled()) {
                        logger.debug(String.format("Skipping disabled SoapUI project (name=%s) suite (name=%s).", projectName, suiteName));

                        continue;
                    }

                    for (TestCase testcase : suite.getTestCaseList()) {
                        if (testcase.isDisabled()) {
                            logger.debug(String.format("Skipping disabled SoapUI project (name=%s) suite (name=%s) testcase (name=%s).", projectName, suiteName,
                                testcase.getName()));

                            continue;
                        }

                        try {
                            instance = instanceConstructor.newInstance();
                        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            throw new SdcctSoapUiException(String.format("Unable to create SoapUI TestNG tests instance (class=%s).", instanceClass.getName()), e);
                        }

                        instance.logger = logger;
                        instance.project = project;
                        instance.suite = suite;
                        instance.testcase = ((WsdlTestCase) testcase);

                        (methodModel = new TestNGMethod(method, annoFinder, xmlTest, instance)).setTestClass(testClass);

                        methodInstances.add(new MethodInstance(methodModel));
                    }
                }
            }

            return methodInstances;
        }
    }

    protected Logger logger;
    protected WsdlProject project;
    protected TestSuite suite;
    protected WsdlTestCase testcase;

    protected void testTestCase() throws Exception {
        String projectName = this.project.getName(), suiteName = this.suite.getName(), testcaseName = this.testcase.getName();

        this.logger.info(String.format("Starting SoapUI project (name=%s) suite (name=%s) testcase (name=%s).", projectName, suiteName, testcaseName));

        WsdlTestCaseRunner runner = null;
        Status status = null;
        Throwable exception = null;

        try {
            status = (runner = this.testcase.run(new PropertiesMap(), false)).getStatus();
        } catch (Exception e) {
            exception = e;
        }

        if (status == Status.FINISHED) {
            this.logger.info(String.format("SoapUI project (name=%s) suite (name=%s) testcase (name=%s) finished.", projectName, suiteName, testcaseName));
        } else {
            String reason = null, msg;
            List<String> stepMsgs = new ArrayList<>(), assertionMsgs = new ArrayList<>();

            if (runner != null) {
                reason = runner.getReason();

                TestStepResult failedStepResult =
                    runner.getResults().stream().filter(result -> (result.getStatus() == TestStepStatus.FAILED)).findFirst().orElse(null);

                if (failedStepResult != null) {
                    exception = failedStepResult.getError();

                    Stream.of(failedStepResult.getMessages()).forEach(stepMsgs::add);

                    TestStep failedStep = failedStepResult.getTestStep();

                    if (failedStep instanceof HttpTestRequestStep) {
                        TestAssertion failedAssertion = ((HttpTestRequestStep) failedStep).getAssertionList().stream()
                            .filter(assertion -> (assertion.getStatus() == AssertionStatus.FAILED)).findFirst().orElse(null);

                        if (failedAssertion != null) {
                            String failedAssertionMsgPrefix = ("{" + failedAssertion.getLabel() + "} ");
                            StrBuilder failedAssertionMsgBuilder = new StrBuilder();
                            XmlError failedAssertionXmlError;

                            for (AssertionError failedAssertionError : failedAssertion.getErrors()) {
                                failedAssertionMsgBuilder.clear();

                                failedAssertionMsgBuilder.append(failedAssertionMsgPrefix);
                                failedAssertionMsgBuilder.append(failedAssertionError.getMessage());

                                if ((failedAssertionXmlError = failedAssertionError.getXmlError()) != null) {
                                    failedAssertionMsgBuilder.append(" (severity=");

                                    switch (failedAssertionXmlError.getSeverity()) {
                                        case XmlError.SEVERITY_INFO:
                                            failedAssertionMsgBuilder.append("info");
                                            break;

                                        case XmlError.SEVERITY_WARNING:
                                            failedAssertionMsgBuilder.append("warning");
                                            break;

                                        case XmlError.SEVERITY_ERROR:
                                            failedAssertionMsgBuilder.append("error");
                                            break;
                                    }

                                    failedAssertionMsgBuilder.append(", code=");
                                    failedAssertionMsgBuilder.append(failedAssertionXmlError.getErrorCode());
                                    failedAssertionMsgBuilder.append(", srcName=");
                                    failedAssertionMsgBuilder.append(failedAssertionXmlError.getSourceName());
                                    failedAssertionMsgBuilder.append(", lineNum=");
                                    failedAssertionMsgBuilder.append(failedAssertionXmlError.getLine());
                                    failedAssertionMsgBuilder.append(", columnNum=");
                                    failedAssertionMsgBuilder.append(failedAssertionXmlError.getColumn());
                                }

                                assertionMsgs.add(failedAssertionMsgBuilder.build());
                            }
                        }
                    }
                }
            }

            this.logger
                .error(
                    ((msg = String.format("SoapUI project (name=%s) suite (name=%s) testcase (name=%s) failed (status=%s, reason=%s)", projectName, suiteName,
                        testcaseName, ((status != null) ? status.name() : null), reason)) +
                        String.format(": stepMsgs=[%s], assertionMsgs=[%s]", StringUtils.join(stepMsgs, "; "), StringUtils.join(assertionMsgs, "; "))),
                    exception);

            throw new SdcctSoapUiException((msg + SdcctStringUtils.PERIOD_CHAR));
        }
    }
}
