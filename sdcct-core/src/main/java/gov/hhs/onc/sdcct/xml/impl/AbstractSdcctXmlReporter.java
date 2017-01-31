package gov.hhs.onc.sdcct.xml.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import gov.hhs.onc.sdcct.xml.SdcctXmlReporter;
import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import net.sf.saxon.Configuration;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.Logger;
import net.sf.saxon.lib.StandardErrorListener;
import net.sf.saxon.trans.XPathException;
import org.codehaus.stax2.validation.XMLValidationProblem;
import org.xml.sax.SAXParseException;

public abstract class AbstractSdcctXmlReporter<T extends AbstractSdcctXmlReporter<T>> extends StandardErrorListener implements SdcctXmlReporter<T> {
    protected AbstractSdcctXmlReporter() {
        this.setLogger(null);
        this.setMaximumNumberOfWarnings(Integer.MAX_VALUE);
        this.setRecoveryPolicy(Configuration.DO_NOT_RECOVER);
    }

    @Override
    public void fatalError(SAXParseException cause) {
        this.report(SdcctIssueSeverity.FATAL, cause);
    }

    @Override
    public void fatalError(TransformerException cause) {
        this.report(SdcctIssueSeverity.FATAL, cause);
    }

    @Override
    public void error(SAXParseException cause) {
        this.report(SdcctIssueSeverity.ERROR, cause);
    }

    @Override
    public void error(TransformerException cause) {
        this.report(SdcctIssueSeverity.ERROR, cause);
    }

    @Override
    public void warning(SAXParseException cause) {
        this.report(SdcctIssueSeverity.WARNING, cause);
    }

    @Override
    public void warning(TransformerException cause) {
        this.report(SdcctIssueSeverity.WARNING, cause);
    }

    protected void report(SdcctIssueSeverity severity, SAXParseException cause) {
        this.report(severity, cause.getMessage(), new SdcctLocation(cause), cause);
    }

    protected void report(SdcctIssueSeverity severity, TransformerException cause) {
        if (cause instanceof XPathException) {
            ((XPathException) cause).setHasBeenReported(true);
        }

        SourceLocator locator = cause.getLocator();

        this.report(severity, cause.getMessage(), ((locator instanceof SdcctLocation) ? ((SdcctLocation) locator) : new SdcctLocation(locator)), cause);
    }

    @Override
    public void report(String msg, String errorType, @Nullable Object relatedInfo, @Nullable Location loc) throws XMLStreamException {
        this.report(((XMLValidationProblem) relatedInfo));
    }

    @Override
    public void report(XMLValidationProblem problem) throws XMLStreamException {
        SdcctIssueSeverity severity = SdcctIssueSeverity.INFORMATION;

        switch (problem.getSeverity()) {
            case XMLValidationProblem.SEVERITY_FATAL:
                severity = SdcctIssueSeverity.FATAL;
                break;

            case XMLValidationProblem.SEVERITY_ERROR:
                severity = SdcctIssueSeverity.ERROR;
                break;

            case XMLValidationProblem.SEVERITY_WARNING:
                severity = SdcctIssueSeverity.WARNING;
                break;
        }

        Location loc = problem.getLocation();

        this.report(severity, problem.getMessage(), ((loc instanceof SdcctLocation) ? ((SdcctLocation) loc) : new SdcctLocation(loc)), null);
    }

    @Override
    public T makeAnother(int hostLang) {
        return this.clone();
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone", CompilerWarnings.UNCHECKED })
    public T clone() {
        return this.cloneInternal();
    }

    @Override
    protected void outputStackTrace(Logger logger, XPathContext xpathContext) {
    }

    protected abstract T cloneInternal();
}
