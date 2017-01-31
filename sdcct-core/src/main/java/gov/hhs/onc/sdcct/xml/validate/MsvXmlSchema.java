package gov.hhs.onc.sdcct.xml.validate;

import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.xml.validate.impl.MsvXmlValidator;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.ValidationContext;

public interface MsvXmlSchema extends IdentifiedBean, NamedBean, SdcctXmlSchema {
    @Override
    public MsvXmlValidator createValidator(ValidationContext context) throws XMLStreamException;

    public XMLSchemaGrammar getGrammar();
}
