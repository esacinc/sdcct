package gov.hhs.onc.sdcct.transform.content.path.impl;

import com.sun.msv.grammar.xmlschema.XMLSchemaTypeExp;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlQnameUtils;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;

public abstract class AbstractContentPathSegment<T extends XMLSchemaTypeExp, U extends JaxbTypeMetadata<T, ?>> implements ContentPathSegment<T, U> {
    protected String nsPrefix;
    protected String nsUri;
    protected String localName;
    protected QName qname;
    protected Class<?> beanClass;
    protected U jaxbTypeMetadata;

    protected AbstractContentPathSegment(@Nullable String nsPrefix, @Nullable String nsUri, String localName) {
        this(SdcctXmlQnameUtils.build(nsPrefix, nsUri, localName));
    }

    protected AbstractContentPathSegment(QName qname) {
        this.nsPrefix = (this.qname = qname).getPrefix();
        this.nsUri = this.qname.getNamespaceURI();
        this.localName = this.qname.getLocalName();
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public boolean hasJaxbTypeMetadata() {
        return (this.jaxbTypeMetadata != null);
    }

    @Nullable
    @Override
    public U getJaxbTypeMetadata() {
        return this.jaxbTypeMetadata;
    }

    @Override
    public void setJaxbTypeMetadata(@Nullable U jaxbTypeMetadata) {
        this.jaxbTypeMetadata = jaxbTypeMetadata;
    }

    @Override
    public String getLocalName() {
        return this.localName;
    }

    @Override
    public String getNamespacePrefix() {
        return this.nsPrefix;
    }

    @Override
    public String getNamespaceUri() {
        return this.nsUri;
    }

    @Override
    public QName getQname() {
        return this.qname;
    }
}
