package gov.hhs.onc.sdcct.xml;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import net.sf.saxon.lib.UnfailingErrorListener;
import org.codehaus.stax2.XMLReporter2;
import org.codehaus.stax2.validation.XMLValidationProblem;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public interface SdcctXmlReporter<T extends SdcctXmlReporter<T>> extends Cloneable, ErrorHandler, UnfailingErrorListener, XMLReporter2 {
    @Override
    public void fatalError(SAXParseException cause);

    @Override
    public void error(SAXParseException cause);

    @Override
    public void warning(SAXParseException cause);

    @Override
    public void report(String msg, String errorType, @Nullable Object relatedInfo, @Nullable Location loc) throws XMLStreamException;

    @Override
    public void report(XMLValidationProblem problem) throws XMLStreamException;

    public void report(SdcctIssueSeverity severity, String msg, SdcctLocation loc, @Nullable Throwable cause);

    public T clone();
}
