package gov.hhs.onc.sdcct.xml.validate.impl;

import com.ctc.wstx.msv.GenericMsvValidator;
import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import com.sun.msv.verifier.regexp.xmlschema.XSREDocDecl;
import gov.hhs.onc.sdcct.xml.validate.MsvXmlSchema;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.ValidationContext;

public class MsvXmlSchemaImpl extends AbstractSdcctXmlSchema implements MsvXmlSchema {
    private String id;
    private String name;
    private XMLSchemaGrammar grammar;

    public MsvXmlSchemaImpl(String id, String name, XMLSchemaGrammar grammar) {
        this.id = id;
        this.name = name;
        this.grammar = grammar;
    }

    @Override
    public MsvXmlValidator createValidator(ValidationContext context) throws XMLStreamException {
        return new MsvXmlValidator(new GenericMsvValidator(this, context, new XSREDocDecl(this.grammar)));
    }

    public XMLSchemaGrammar getGrammar() {
        return this.grammar;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSchemaType() {
        return SCHEMA_ID_W3C_SCHEMA;
    }
}
