package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import com.ctc.wstx.msv.BaseSchemaFactory;
import com.ctc.wstx.msv.W3CSchema;
import com.github.sebhoss.warnings.CompilerWarnings;
import com.sun.msv.grammar.ExpressionPool;
import com.sun.msv.grammar.xmlschema.ComplexTypeExp;
import com.sun.msv.grammar.xmlschema.ElementDeclExp;
import com.sun.msv.grammar.xmlschema.SimpleTypeExp;
import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import com.sun.msv.grammar.xmlschema.XMLSchemaSchema;
import com.sun.msv.reader.GrammarReaderController2;
import com.sun.msv.reader.State;
import com.sun.msv.reader.xmlschema.MultiSchemaReader;
import com.sun.msv.reader.xmlschema.SchemaState;
import com.sun.msv.reader.xmlschema.XMLSchemaReader;
import com.sun.msv.reader.xmlschema.XMLSchemaReader.StateFactory;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.SdcctXmlResolver;
import gov.hhs.onc.sdcct.xml.impl.SdcctSaxParserFactory;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.jaxb.JAXBContextCache;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class JaxbContextMetadataImpl extends AbstractJaxbMetadataComponent implements JaxbContextMetadata {
    private class JaxbGrammarReaderController implements GrammarReaderController2, LSResourceResolver {
        @Nullable
        @Override
        public LSInput resolveResource(String type, String nsUri, @Nullable String publicId, @Nullable String sysId, @Nullable String baseUri) {
            ByteArraySource src = JaxbContextMetadataImpl.this.xmlResolver.resolve(nsUri, type, baseUri, publicId, sysId);

            return ((src != null)
                ? src.getLsInput()
                : (JaxbContextMetadataImpl.this.schemaSrcs.containsKey(sysId) ? JaxbContextMetadataImpl.this.schemaSrcs.get(sysId).getLsInput() : null));
        }

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
        public LSResourceResolver getLSResourceResolver() {
            return this;
        }
    }

    private interface MutableSchemaReader {
        public void markSchemaAsDefined(XMLSchemaSchema schema);

        public boolean isSchemaDefined(XMLSchemaSchema schema);

        public XMLSchemaSchema getOrCreateSchema(String nsUri);

        public XMLSchemaSchema getCurrentSchema();

        public void setCurrentSchema(XMLSchemaSchema schema);
    }

    private class JaxbSchemaState extends SchemaState {
        private XMLSchemaSchema schema;

        public JaxbSchemaState(String expectedNsUri) {
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

    private class JaxbStateFactory extends StateFactory {
        @Override
        public State schemaHead(String expectedNsUri) {
            return new JaxbSchemaState(expectedNsUri);
        }
    }

    private class JaxbSchemaReader extends XMLSchemaReader implements MutableSchemaReader {
        private Set<String> sysIds = new TreeSet<>();

        public JaxbSchemaReader() {
            super(new JaxbGrammarReaderController(), JaxbContextMetadataImpl.this.saxParserFactory, new JaxbStateFactory(), new ExpressionPool());
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

    private class JaxbMultiSchemaFactory extends BaseSchemaFactory {
        public JaxbMultiSchemaFactory() {
            super(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
        }

        public XMLSchemaGrammar loadSchemas() throws Exception {
            MultiSchemaReader multiSchemaReader = new MultiSchemaReader(new JaxbSchemaReader());

            JaxbContextMetadataImpl.this.schemaSrcs.values().forEach(multiSchemaReader::parse);

            return multiSchemaReader.getResult();
        }

        @Override
        protected XMLValidationSchema loadSchema(InputSource src, Object sysRef) throws XMLStreamException {
            throw new UnsupportedOperationException();
        }
    }

    private final static String CREATE_OBJ_FACTORY_METHOD_NAME_PREFIX = "create";

    private final static Logger LOGGER = LoggerFactory.getLogger(JaxbContextMetadataImpl.class);

    @Autowired
    private SdcctSaxParserFactory saxParserFactory;

    @Autowired
    private SdcctXmlResolver xmlResolver;

    private Map<String, Object> contextProps = new HashMap<>();
    private Map<String, Package> schemaImplPkgs = new TreeMap<>();
    private Map<String, Package> schemaPkgs = new TreeMap<>();
    private Map<String, ResourceSource> schemaSrcs;
    private JAXBContext context;
    private XMLSchemaGrammar schemaGrammar;
    private W3CSchema validationSchema;
    private Map<String, JaxbSchemaMetadata> schemas = new TreeMap<>();
    private Map<String, Object> schemaObjFactories = new TreeMap<>();

    public JaxbContextMetadataImpl(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void afterPropertiesSet() throws Exception {
        Map<Object, Class<?>> schemaObjFactoryClasses =
            this.schemaObjFactories.values().stream().collect(SdcctStreamUtils.toMap(Function.identity(), Object::getClass, LinkedHashMap::new));

        this.context =
            JAXBContextCache.getCachedContextAndSchemas(new LinkedHashSet<>(schemaObjFactoryClasses.values()), null, contextProps, null, true).getContext();

        this.validationSchema = new W3CSchema((this.schemaGrammar = new JaxbMultiSchemaFactory().loadSchemas()));

        Package schemaPkg, schemaImplPkg;
        JaxbSchemaMetadata schema;
        Class<?> schemaObjFactoryClass, schemaTypeBeanClass, schemaTypeBeanImplClass;
        Map<String, Class<?>> schemaTypeBeanClasses = new LinkedHashMap<>(), schemaTypeBeanImplClasses = new LinkedHashMap<>();
        Map<String, Class<?>> schemaTypeScopes = new LinkedHashMap<>();
        int numSchemaSimpleTypes, numSchemaComplexTypes, numSchemaElems;
        String schemaTypeName, schemaElemName, schemaElemTypeName;
        XmlElementDecl elemDeclAnno;
        Map<String, JaxbTypeMetadata<?, ?>> schemaTypeNames;
        boolean schemaTypeBeanClassAvailable, schemaElemTypeBeanClassAvailable;
        ComplexTypeExp schemaElemTypeExpr;
        JaxbComplexTypeMetadataImpl<?> schemaElemType;

        for (XMLSchemaSchema schemaExpr : IteratorUtils.asIterable(((Iterator<XMLSchemaSchema>) this.schemaGrammar.iterateSchemas()))) {
            if (!this.schemaPkgs.containsKey(schemaExpr.targetNamespace) || !this.schemaImplPkgs.containsKey(schemaExpr.targetNamespace)) {
                continue;
            }

            this.schemas.put(schemaExpr.targetNamespace,
                (schema = new JaxbSchemaMetadataImpl(this, schemaExpr, (schemaPkg = this.schemaPkgs.get(schemaExpr.targetNamespace)),
                    (schemaImplPkg = this.schemaImplPkgs.get(schemaExpr.targetNamespace)), schemaExpr.targetNamespace)));

            schemaTypeBeanClasses.clear();
            schemaTypeBeanImplClasses.clear();
            schemaTypeScopes.clear();

            for (Method schemaObjFactoryMethod : (schemaObjFactoryClass = schemaObjFactoryClasses.get(this.schemaObjFactories.get(schemaExpr.targetNamespace)))
                .getDeclaredMethods()) {
                if (!StringUtils.startsWith(schemaObjFactoryMethod.getName(), CREATE_OBJ_FACTORY_METHOD_NAME_PREFIX)) {
                    continue;
                }

                schemaTypeBeanClass = null;

                if (schemaObjFactoryMethod.getParameterCount() == 0) {
                    schemaTypeBeanImplClasses
                        .put(
                            (!(schemaTypeName = (schemaTypeBeanImplClass = schemaObjFactoryMethod.getReturnType()).getAnnotation(XmlType.class).name())
                                .isEmpty()
                                    ? schemaTypeName
                                    : (schemaTypeBeanImplClass.isAnnotationPresent(XmlRootElement.class)
                                        ? (schemaTypeName = schemaTypeBeanImplClass.getAnnotation(XmlRootElement.class).name())
                                        : (schemaTypeName =
                                            (schemaTypeBeanClass = buildSchemaTypeBeanClass(schemaPkg, schemaTypeBeanImplClass)).getSimpleName()))),
                            schemaTypeBeanImplClass);
                    schemaTypeBeanClasses.put(schemaTypeName,
                        ((schemaTypeBeanClass != null) ? schemaTypeBeanClass : buildSchemaTypeBeanClass(schemaPkg, schemaTypeBeanImplClass)));
                } else {
                    schemaTypeScopes.put((elemDeclAnno = schemaObjFactoryMethod.getAnnotation(XmlElementDecl.class)).name(), elemDeclAnno.scope());
                }
            }

            schemaTypeNames = schema.getTypeNames();
            numSchemaSimpleTypes = numSchemaComplexTypes = numSchemaElems = 0;

            for (SimpleTypeExp schemaSimpleTypeExpr : IteratorUtils.asIterable(((Iterator<SimpleTypeExp>) schemaExpr.simpleTypes.iterator()))) {
                schema.addType(new JaxbSimpleTypeMetadataImpl<>(this, schema, schemaSimpleTypeExpr, schemaSimpleTypeExpr.name,
                    new QName(schemaExpr.targetNamespace, schemaSimpleTypeExpr.name),
                    ((schemaTypeBeanClassAvailable = schemaTypeBeanClasses.containsKey(schemaSimpleTypeExpr.name))
                        ? ((Class<Object>) schemaTypeBeanClasses.get(schemaSimpleTypeExpr.name)) : null),
                    (schemaTypeBeanClassAvailable ? schemaTypeBeanImplClasses.get(schemaSimpleTypeExpr.name) : null)));

                numSchemaSimpleTypes++;
            }

            for (ComplexTypeExp schemaComplexTypeExpr : IteratorUtils.asIterable(((Iterator<ComplexTypeExp>) schemaExpr.complexTypes.iterator()))) {
                schema.addType(new JaxbComplexTypeMetadataImpl<>(this, schema, schemaComplexTypeExpr, schemaComplexTypeExpr.name,
                    new QName(schemaExpr.targetNamespace, schemaComplexTypeExpr.name), schemaComplexTypeExpr.isAbstract(),
                    ((schemaTypeBeanClassAvailable = schemaTypeBeanClasses.containsKey(schemaComplexTypeExpr.name))
                        ? ((Class<Object>) schemaTypeBeanClasses.get(schemaComplexTypeExpr.name)) : null),
                    (schemaTypeBeanClassAvailable ? schemaTypeBeanImplClasses.get(schemaComplexTypeExpr.name) : null)));

                numSchemaComplexTypes++;
            }

            for (ElementDeclExp schemaElemExpr : IteratorUtils.asIterable(((Iterator<ElementDeclExp>) schemaExpr.elementDecls.iterator()))) {
                schemaElemName = schemaElemExpr.getElementExp().elementName.localName;
                schemaElemType = null;

                if ((schemaElemTypeName = (schemaElemTypeExpr = ((ComplexTypeExp) schemaElemExpr.getTypeDefinition())).name) == null) {
                    schemaElemTypeName = schemaElemName;
                }

                if (!schemaTypeNames.containsKey(schemaElemTypeName)) {
                    schemaTypeBeanClassAvailable = schemaTypeBeanClasses.containsKey(schemaElemTypeName);
                    schemaElemTypeBeanClassAvailable = schemaTypeBeanClasses.containsKey(schemaElemName);

                    schema.addType((schemaElemType = new JaxbComplexTypeMetadataImpl<>(this, schema, schemaElemTypeExpr, schemaElemTypeName,
                        new QName(schemaExpr.targetNamespace, schemaElemTypeName), false,
                        ((schemaTypeBeanClassAvailable || schemaElemTypeBeanClassAvailable)
                            ? ((Class<Object>) schemaTypeBeanClasses.get((schemaTypeBeanClassAvailable ? schemaElemTypeName : schemaElemName))) : null),
                        ((schemaTypeBeanClassAvailable || schemaElemTypeBeanClassAvailable)
                            ? schemaTypeBeanImplClasses.get((schemaTypeBeanClassAvailable ? schemaElemTypeName : schemaElemName)) : null))));

                    numSchemaComplexTypes++;
                }

                schema.addElement(new JaxbElementMetadataImpl<>(this, schema, schemaElemExpr, schemaElemName,
                    new QName(schemaExpr.targetNamespace, schemaElemName), schemaElemExpr.isAbstract(),
                    (schemaTypeScopes.containsKey(schemaElemName) ? schemaTypeScopes.get(schemaElemName) : XmlElementDecl.GLOBAL.class),
                    ((JaxbComplexTypeMetadata<?>) ((schemaElemType != null) ? schemaElemType : schemaTypeNames.get(schemaElemTypeName)))));

                numSchemaElems++;
            }

            LOGGER.debug(String.format(
                "Built JAXB context (name=%s) schema (nsUri=%s, pkg=%s, implPkg=%s) metadata (numSimpleTypes=%d, numComplexTypes=%d, numElems=%d) using object factory class (name=%s).",
                this.name, schemaExpr.targetNamespace, schemaPkg, schemaImplPkg, numSchemaSimpleTypes, numSchemaComplexTypes, numSchemaElems,
                schemaObjFactoryClass.getName()));
        }
    }

    private static Class<?> buildSchemaTypeBeanClass(Package schemaPkg, Class<?> schemaTypeBeanImplClass) {
        // noinspection OptionalGetWithoutIsPresent
        return Stream.of(schemaTypeBeanImplClass.getInterfaces())
            .filter(schemaTypeBeanImplInterfaceClass -> schemaTypeBeanImplInterfaceClass.getPackage().equals(schemaPkg)).findFirst().get();
    }

    @Override
    public JAXBContext getContext() {
        return this.context;
    }

    @Override
    public Map<String, Object> getContextProperties() {
        return this.contextProps;
    }

    @Override
    public void setContextProperties(Map<String, Object> contextProps) {
        this.contextProps.clear();
        this.contextProps.putAll(contextProps);
    }

    @Override
    public XMLSchemaGrammar getSchemaGrammar() {
        return this.schemaGrammar;
    }

    @Override
    public Map<String, Package> getSchemaImplPackages() {
        return this.schemaImplPkgs;
    }

    @Override
    public void setSchemaImplPackages(Map<String, Package> schemaImplPkgs) {
        this.schemaImplPkgs.clear();
        this.schemaImplPkgs.putAll(schemaImplPkgs);
    }

    @Override
    public Map<String, Object> getSchemaObjectFactories() {
        return this.schemaObjFactories;
    }

    @Override
    public void setSchemaObjectFactories(Map<String, Object> schemaObjFactories) {
        this.schemaObjFactories.clear();
        this.schemaObjFactories.putAll(schemaObjFactories);
    }

    @Override
    public Map<String, Package> getSchemaPackages() {
        return this.schemaPkgs;
    }

    @Override
    public void setSchemaPackages(Map<String, Package> schemaPkgs) {
        this.schemaPkgs.clear();
        this.schemaPkgs.putAll(schemaPkgs);
    }

    @Override
    public Map<String, JaxbSchemaMetadata> getSchemas() {
        return this.schemas;
    }

    @Override
    public Map<String, ResourceSource> getSchemaSources() {
        return this.schemaSrcs;
    }

    @Override
    public void setSchemaSources(ResourceSource ... schemaSrcs) {
        this.schemaSrcs = Stream.of(schemaSrcs).collect(SdcctStreamUtils.toMap(Source::getSystemId, Function.identity(), TreeMap::new));
    }

    @Override
    public W3CSchema getValidationSchema() {
        return this.validationSchema;
    }
}
