package gov.hhs.onc.sdcct.validate.schema.impl;

import com.ctc.wstx.msv.BaseSchemaFactory;
import com.sun.msv.grammar.ExpressionPool;
import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import com.sun.msv.grammar.xmlschema.XMLSchemaSchema;
import com.sun.msv.reader.GrammarReaderController2;
import com.sun.msv.reader.State;
import com.sun.msv.reader.xmlschema.MultiSchemaReader;
import com.sun.msv.reader.xmlschema.SchemaState;
import com.sun.msv.reader.xmlschema.XMLSchemaReader;
import com.sun.msv.reader.xmlschema.XMLSchemaReader.StateFactory;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.validate.schema.MsvSchemaBuilder;
import gov.hhs.onc.sdcct.xml.SdcctXmlResolver;
import gov.hhs.onc.sdcct.xml.impl.SdcctSaxParserFactory;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

@Component("msvSchemaBuilder")
public class MsvSchemaBuilderImpl implements MsvSchemaBuilder {
    private class MsvGrammarReaderController implements GrammarReaderController2 {
        @Nullable
        @Override
        public InputSource resolveEntity(@Nullable String publicId, String sysId) throws IOException, SAXException {
            return null;
        }

        @Override
        public void error(Locator[] locators, String msg, Exception exception) {
            LOGGER.error(this.buildMessage(msg, locators), exception);
        }

        @Override
        public void warning(Locator[] locators, String msg) {
            LOGGER.warn(this.buildMessage(msg, locators));
        }

        private String buildMessage(String msg, Locator ... locators) {
            return String
                .format("%s: [%s]", msg,
                    Stream.of(locators)
                        .filter(locator -> (locator != null)).map(locator -> String.format("publicId=%s, sysId=%s, lineNum=%d, columnNum=%d",
                            locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber()))
                        .collect(Collectors.joining("; ")));
        }

        @Override
        public SdcctXmlResolver getLSResourceResolver() {
            return MsvSchemaBuilderImpl.this.xmlResolver;
        }
    }

    private interface MutableSchemaReader {
        public void markSchemaAsDefined(XMLSchemaSchema schema);

        public boolean isSchemaDefined(XMLSchemaSchema schema);

        public XMLSchemaSchema getOrCreateSchema(String nsUri);

        public XMLSchemaSchema getCurrentSchema();

        public void setCurrentSchema(XMLSchemaSchema schema);
    }

    private class MsvSchemaState extends SchemaState {
        private XMLSchemaSchema schema;

        public MsvSchemaState(String expectedNsUri) {
            super(expectedNsUri);
        }

        @Override
        protected void endSelf() {
            super.endSelf();

            ((MutableSchemaReader) this.reader).setCurrentSchema(this.schema);
        }

        @Override
        protected void onTargetNamespaceResolved(String targetNsUri, boolean ignoreContents) {
            MutableSchemaReader reader = ((MutableSchemaReader) this.reader);

            this.schema = reader.getCurrentSchema();

            XMLSchemaSchema readerSchema = reader.getOrCreateSchema(targetNsUri);

            reader.setCurrentSchema(readerSchema);

            if (ignoreContents) {
                return;
            }

            if (!reader.isSchemaDefined(readerSchema)) {
                reader.markSchemaAsDefined(readerSchema);
            }
        }
    }

    private class MsvStateFactory extends StateFactory {
        @Override
        public State schemaHead(String expectedNsUri) {
            return new MsvSchemaState(expectedNsUri);
        }
    }

    private class MsvSchemaReader extends XMLSchemaReader implements MutableSchemaReader {
        private Set<String> sysIds = new TreeSet<>();

        public MsvSchemaReader() {
            super(new MsvGrammarReaderController(), MsvSchemaBuilderImpl.this.saxParserFactory, new MsvStateFactory(), new ExpressionPool());
        }

        @Override
        public void switchSource(Source src, State newState) {
            String srcSysId = src.getSystemId();

            if ((srcSysId == null) || !this.sysIds.contains(srcSysId)) {
                super.switchSource(src, newState);
            }
        }

        @Override
        public void setLocator(@Nullable Locator locator) {
            String locatorSysId;

            if ((locator == null) && (((locator = this.getLocator()) != null))) {
                if ((locatorSysId = locator.getSystemId()) != null) {
                    this.sysIds.add(locatorSysId);
                }

                locator = null;
            }

            super.setLocator(locator);
        }

        @Override
        public XMLSchemaSchema getCurrentSchema() {
            return this.currentSchema;
        }

        @Override
        public void setCurrentSchema(XMLSchemaSchema schema) {
            this.currentSchema = schema;
        }
    }

    private class MsvSchemaFactory extends BaseSchemaFactory {
        private Map<String, ResourceSource> schemaSrcs;

        public MsvSchemaFactory(Map<String, ResourceSource> schemaSrcs) {
            super(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);

            this.schemaSrcs = schemaSrcs;
        }

        public XMLSchemaGrammar loadSchemas() throws Exception {
            MultiSchemaReader multiSchemaReader = new MultiSchemaReader(new MsvSchemaReader());

            this.schemaSrcs.values().forEach(multiSchemaReader::parse);

            return multiSchemaReader.getResult();
        }

        @Override
        protected XMLValidationSchema loadSchema(InputSource src, Object sysRef) throws XMLStreamException {
            throw new UnsupportedOperationException();
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(MsvSchemaBuilderImpl.class);

    @Autowired
    private SdcctSaxParserFactory saxParserFactory;

    @Autowired
    private SdcctXmlResolver xmlResolver;

    @Override
    public MsvSchema build(ContentPathBuilder contentPathBuilder, String id, String name, Map<String, ResourceSource> schemaSrcs) throws Exception {
        return new MsvSchema(contentPathBuilder, id, name, new MsvSchemaFactory(schemaSrcs).loadSchemas());
    }
}
