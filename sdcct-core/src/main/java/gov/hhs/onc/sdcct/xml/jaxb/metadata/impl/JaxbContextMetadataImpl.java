package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import com.sun.msv.grammar.xmlschema.ComplexTypeExp;
import com.sun.msv.grammar.xmlschema.ElementDeclExp;
import com.sun.msv.grammar.xmlschema.SimpleTypeExp;
import com.sun.msv.grammar.xmlschema.XMLSchemaSchema;
import com.sun.msv.grammar.xmlschema.XMLSchemaTypeExp;
import com.sun.xml.bind.api.JAXBRIContext;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import gov.hhs.onc.sdcct.xml.validate.MsvXmlSchemaBuilder;
import gov.hhs.onc.sdcct.xml.validate.impl.MsvXmlSchemaImpl;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.jaxb.JAXBContextCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class JaxbContextMetadataImpl extends AbstractJaxbMetadataComponent implements JaxbContextMetadata {
    private final static String CREATE_OBJ_FACTORY_METHOD_NAME_PREFIX = "create";

    private final static Logger LOGGER = LoggerFactory.getLogger(JaxbContextMetadataImpl.class);

    @Autowired
    private MsvXmlSchemaBuilder msvSchemaBuilder;

    private Map<String, Object> contextProps = new HashMap<>();
    private Map<String, Package> schemaImplPkgs = new TreeMap<>();
    private Map<String, Package> schemaPkgs = new TreeMap<>();
    private Map<String, String> schemaPrefixes = new TreeMap<>();
    private Map<String, ResourceSource> schemaSrcs;
    private Map<String, String> schemaXpathPrefixes = new TreeMap<>();
    private JAXBRIContext context;
    private MsvXmlSchemaImpl validationSchema;
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

        this.context = ((JAXBRIContext) JAXBContextCache
            .getCachedContextAndSchemas(new LinkedHashSet<>(schemaObjFactoryClasses.values()), null, contextProps, null, true).getContext());

        this.validationSchema = this.msvSchemaBuilder.build(this.name, this.name, this.schemaSrcs);

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
        XMLSchemaTypeExp schemaElemTypeExpr;
        ComplexTypeExp schemaElemComplexTypeExpr;
        JaxbComplexTypeMetadataImpl<?> schemaElemType;

        for (XMLSchemaSchema schemaExpr : IteratorUtils.asIterable(((Iterator<XMLSchemaSchema>) this.validationSchema.getGrammar().iterateSchemas()))) {
            if (!this.schemaPkgs.containsKey(schemaExpr.targetNamespace) || !this.schemaImplPkgs.containsKey(schemaExpr.targetNamespace)) {
                continue;
            }

            this.schemas.put(schemaExpr.targetNamespace,
                (schema = new JaxbSchemaMetadataImpl(this, schemaExpr, (schemaPkg = this.schemaPkgs.get(schemaExpr.targetNamespace)),
                    (schemaImplPkg = this.schemaImplPkgs.get(schemaExpr.targetNamespace)), schemaExpr.targetNamespace,
                    this.schemaPrefixes.get(schemaExpr.targetNamespace), this.schemaXpathPrefixes.get(schemaExpr.targetNamespace))));

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

                if ((schemaElemTypeExpr = schemaElemExpr.getTypeDefinition()) instanceof SimpleTypeExp) {
                    continue;
                }

                if ((schemaElemTypeName = (schemaElemComplexTypeExpr = ((ComplexTypeExp) schemaElemTypeExpr)).name) == null) {
                    schemaElemTypeName = schemaElemName;
                }

                if (!schemaTypeNames.containsKey(schemaElemTypeName)) {
                    schemaTypeBeanClassAvailable = schemaTypeBeanClasses.containsKey(schemaElemTypeName);
                    schemaElemTypeBeanClassAvailable = schemaTypeBeanClasses.containsKey(schemaElemName);

                    schema.addType((schemaElemType = new JaxbComplexTypeMetadataImpl<>(this, schema, schemaElemComplexTypeExpr, schemaElemTypeName,
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
    public JAXBRIContext getContext() {
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
    public Map<String, String> getSchemaPrefixes() {
        return this.schemaPrefixes;
    }

    @Override
    public void setSchemaPrefixes(Map<String, String> schemaPrefixes) {
        this.schemaPrefixes.clear();
        this.schemaPrefixes.putAll(schemaPrefixes);
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
    public Map<String, String> getSchemaXpathPrefixes() {
        return this.schemaXpathPrefixes;
    }

    @Override
    public void setSchemaXpathPrefixes(Map<String, String> schemaXpathPrefixes) {
        this.schemaXpathPrefixes.clear();
        this.schemaXpathPrefixes.putAll(schemaXpathPrefixes);
    }

    @Override
    public MsvXmlSchemaImpl getValidationSchema() {
        return this.validationSchema;
    }
}
