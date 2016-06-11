package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import com.sun.msv.grammar.xmlschema.XMLSchemaSchema;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbElementMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class JaxbSchemaMetadataImpl extends AbstractJaxbContextMetadataComponent<XMLSchemaSchema> implements JaxbSchemaMetadata {
    private Package pkg;
    private Package implPkg;
    private Map<String, JaxbElementMetadata<?>> elemNames = new TreeMap<>();
    private Map<String, String> elemTypeNames = new TreeMap<>();
    private Map<Class<?>, JaxbTypeMetadata<?, ?>> typeBeanClasses = new TreeMap<>(Comparator.comparing(Class::getName));
    private Map<String, JaxbTypeMetadata<?, ?>> typeNames = new TreeMap<>();

    public JaxbSchemaMetadataImpl(JaxbContextMetadata context, XMLSchemaSchema expr, Package pkg, Package implPkg, String name) {
        super(context, expr, name);

        this.pkg = pkg;
        this.implPkg = implPkg;
    }

    @Override
    public void addElement(JaxbElementMetadata<?> elem) {
        String elemName = elem.getName();

        this.elemNames.put(elemName, elem);
        this.elemTypeNames.put(elem.getType().getName(), elemName);
    }

    @Override
    public Map<String, JaxbElementMetadata<?>> getElementNames() {
        return this.elemNames;
    }

    @Override
    public Map<String, String> getElementTypeNames() {
        return this.elemTypeNames;
    }

    @Override
    public Package getImplPackage() {
        return this.implPkg;
    }

    @Override
    public Package getPackage() {
        return this.pkg;
    }

    @Override
    public void addType(JaxbTypeMetadata<?, ?> type) {
        if (type.hasBeanImplClass()) {
            this.typeBeanClasses.put(type.getBeanImplClass(), type);
        }

        this.typeNames.put(type.getName(), type);
    }

    @Override
    public Map<Class<?>, JaxbTypeMetadata<?, ?>> getTypeBeanClasses() {
        return this.typeBeanClasses;
    }

    @Override
    public Map<String, JaxbTypeMetadata<?, ?>> getTypeNames() {
        return this.typeNames;
    }
}
