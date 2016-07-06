package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.xml.SdcctXmlReporter;
import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import net.sf.saxon.trans.XPathException;
import org.codehaus.stax2.validation.XMLValidationException;
import org.codehaus.stax2.validation.XMLValidationProblem;
import org.xml.sax.SAXParseException;

public abstract class AbstractSdcctXmlReporter implements SdcctXmlReporter {
    @Override
    public void report(String msg, String errorType, @Nullable Object relatedInfo, @Nullable Location loc) throws XMLStreamException {
        if (relatedInfo instanceof XMLValidationProblem) {
            this.report(((XMLValidationProblem) relatedInfo));
        } else if (relatedInfo instanceof SAXParseException) {
            this.error(((SAXParseException) relatedInfo));
        } else {
            this.report(IssueLevel.ERROR, msg, ((loc != null) ? new SdcctLocation(loc) : new SdcctLocation()));
        }
    }

    @Override
    public void reportProblem(XMLValidationProblem problem) throws XMLValidationException {
        try {
            this.report(problem);
        } catch (XMLStreamException ignored) {
        }
    }

    @Override
    public void report(XMLValidationProblem problem) throws XMLStreamException {
        IssueLevel level = IssueLevel.INFORMATION;

        switch (problem.getSeverity()) {
            case XMLValidationProblem.SEVERITY_WARNING:
                level = IssueLevel.WARNING;
                break;

            case XMLValidationProblem.SEVERITY_ERROR:
                level = IssueLevel.ERROR;
                break;

            case XMLValidationProblem.SEVERITY_FATAL:
                level = IssueLevel.FATAL;
                break;
        }

        Location loc = problem.getLocation();

        this.report(level, problem.getMessage(), ((loc instanceof SdcctLocation) ? ((SdcctLocation) loc) : new SdcctLocation(loc)));
    }

    @Override
    public void fatalError(SAXParseException exception) {
        this.report(exception, IssueLevel.FATAL);
    }

    @Override
    public void fatalError(TransformerException exception) {
        this.report(exception, IssueLevel.FATAL);
    }

    @Override
    public void error(SAXParseException exception) {
        this.report(exception, IssueLevel.ERROR);
    }

    @Override
    public void error(TransformerException exception) {
        this.report(exception, IssueLevel.ERROR);
    }

    @Override
    public void warning(SAXParseException exception) {
        this.report(exception, IssueLevel.WARNING);
    }

    @Override
    public void warning(TransformerException exception) {
        this.report(exception, IssueLevel.WARNING);
    }

    protected void report(SAXParseException exception, IssueLevel level) {
        this.report(level, exception.getMessage(), new SdcctLocation(exception));
    }

    protected void report(TransformerException exception, IssueLevel level) {
        this.report(level, exception.getMessage(), new SdcctLocation(exception.getLocator()));

        if (exception instanceof XPathException) {
            ((XPathException) exception).setHasBeenReported(true);
        }
    }
}
