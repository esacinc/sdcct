package gov.hhs.onc.sdcct.xml.jaxb.impl;

import com.ctc.wstx.msv.W3CSchema;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextId;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextInfo;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbElementInfo;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import org.apache.ws.commons.schema.XmlSchemaCollection;

public class JaxbContextInfoImpl implements JaxbContextInfo {
    private JaxbContextId id;
    private Set<Class<?>> classes = new LinkedHashSet<>();
    private JAXBContext context;
    private Map<String, Object> contextProps = new HashMap<>();
    private Map<Class<?>, JaxbElementInfo<?>> elemInfos = new HashMap<>();
    private Object[] objFactories;
    private XmlSchemaCollection schemaColl;
    private Map<String, XdmDocument> schemaDocs;
    private W3CSchema validationSchema;

    public JaxbContextInfoImpl(JaxbContextId id) {
        this.id = id;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return this.classes;
    }

    @Override
    public void setClasses(Set<Class<?>> classes) {
        this.classes.clear();
        this.classes.addAll(classes);
    }

    @Override
    public JAXBContext getContext() {
        return this.context;
    }

    @Override
    public void setContext(JAXBContext context) {
        this.context = context;
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
    public Map<Class<?>, JaxbElementInfo<?>> getElementInfos() {
        return this.elemInfos;
    }

    @Override
    public void setElementInfos(Map<Class<?>, JaxbElementInfo<?>> elemInfos) {
        this.elemInfos.clear();
        this.elemInfos.putAll(elemInfos);
    }

    @Override
    public JaxbContextId getId() {
        return this.id;
    }

    @Override
    public Object[] getObjectFactories() {
        return this.objFactories;
    }

    @Override
    public void setObjectFactories(Object ... objFactories) {
        this.objFactories = objFactories;
    }

    @Override
    public XmlSchemaCollection getSchemaCollection() {
        return this.schemaColl;
    }

    @Override
    public void setSchemaCollection(XmlSchemaCollection schemaColl) {
        this.schemaColl = schemaColl;
    }

    @Override
    public Map<String, XdmDocument> getSchemaDocuments() {
        return this.schemaDocs;
    }

    @Override
    public void setSchemaDocuments(XdmDocument ... schemaDocs) {
        this.schemaDocs =
            Stream.of(schemaDocs).collect(SdcctStreamUtils.toMap(schemaDoc -> schemaDoc.getUri().toString(), Function.identity(), LinkedHashMap::new));
    }

    @Override
    public boolean hasValidationSchema() {
        return (this.validationSchema != null);
    }

    @Nullable
    @Override
    public W3CSchema getValidationSchema() {
        return this.validationSchema;
    }

    @Override
    public void setValidationSchema(@Nullable W3CSchema validationSchema) {
        this.validationSchema = validationSchema;
    }
}
