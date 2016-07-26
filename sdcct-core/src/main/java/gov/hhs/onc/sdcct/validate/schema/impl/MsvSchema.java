package gov.hhs.onc.sdcct.validate.schema.impl;

import com.ctc.wstx.msv.W3CSchema;
import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.ValidationContext;

public class MsvSchema extends W3CSchema implements IdentifiedBean, NamedBean {
    private ContentPathBuilder contentPathBuilder;
    private String id;
    private String name;

    public MsvSchema(ContentPathBuilder contentPathBuilder, String id, String name, XMLSchemaGrammar grammar) {
        super(grammar);

        this.contentPathBuilder = contentPathBuilder;
        this.id = id;
        this.name = name;
    }

    @Override
    public MsvValidator createValidator(ValidationContext context) throws XMLStreamException {
        return new MsvValidator(this, new MsvContext(this.contentPathBuilder, context));
    }

    public XMLSchemaGrammar getGrammar() {
        return this.mGrammar;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
