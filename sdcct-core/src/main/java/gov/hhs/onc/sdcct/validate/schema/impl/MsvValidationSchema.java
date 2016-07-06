package gov.hhs.onc.sdcct.validate.schema.impl;

import com.ctc.wstx.msv.W3CSchema;
import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.ValidationContext;

public class MsvValidationSchema extends W3CSchema {
    private ContentPathBuilder contentPathBuilder;

    public MsvValidationSchema(ContentPathBuilder contentPathBuilder, XMLSchemaGrammar grammar) {
        super(grammar);

        this.contentPathBuilder = contentPathBuilder;
    }

    @Override
    public MsvValidator createValidator(ValidationContext context) throws XMLStreamException {
        return new MsvValidator(this, new MsvValidationContext(this.contentPathBuilder, context));
    }

    public XMLSchemaGrammar getGrammar() {
        return this.mGrammar;
    }
}
