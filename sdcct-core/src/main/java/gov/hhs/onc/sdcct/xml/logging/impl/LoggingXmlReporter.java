package gov.hhs.onc.sdcct.xml.logging.impl;

import ch.qos.logback.classic.Logger;
import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.xml.impl.AbstractSdcctXmlReporter;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("xmlReporterLogging")
public class LoggingXmlReporter extends AbstractSdcctXmlReporter {
    private final static Logger LOGGER = ((Logger) LoggerFactory.getLogger(LoggingXmlReporter.class));

    @Override
    public void report(IssueLevel level, String msg, SdcctLocation loc) {
        // TODO: Build Markers.
        LOGGER.log(null, Logger.FQCN, level.getLoggingLevel().toInt(), String.format("XML event (lineNum=%d, columnNum=%d, elemQname=%s, attrQname=%s): %s",
            loc.getLineNumber(), loc.getColumnNumber(), loc.getAttributeQname(), loc.getElementQname(), msg), null, null);
    }
}
