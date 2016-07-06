package gov.hhs.onc.sdcct.xml;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import net.sf.saxon.lib.UnfailingErrorListener;
import org.codehaus.stax2.XMLReporter2;
import org.codehaus.stax2.validation.ValidationProblemHandler;
import org.codehaus.stax2.validation.XMLValidationException;
import org.codehaus.stax2.validation.XMLValidationProblem;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public interface SdcctXmlReporter extends ErrorHandler, UnfailingErrorListener, ValidationProblemHandler, XMLReporter2 {
    @Override
    public void report(String msg, String errorType, @Nullable Object relatedInfo, @Nullable Location loc) throws XMLStreamException;

    @Override
    public void reportProblem(XMLValidationProblem problem) throws XMLValidationException;

    @Override
    public void report(XMLValidationProblem problem) throws XMLStreamException;

    @Override
    public void fatalError(SAXParseException exception);

    @Override
    public void error(SAXParseException exception);

    @Override
    public void warning(SAXParseException exception);

    public void report(IssueLevel level, String msg, SdcctLocation loc);
}
