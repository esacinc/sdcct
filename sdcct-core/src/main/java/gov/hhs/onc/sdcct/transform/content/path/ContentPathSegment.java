package gov.hhs.onc.sdcct.transform.content.path;

import com.sun.msv.grammar.xmlschema.XMLSchemaTypeExp;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;

public interface ContentPathSegment<T extends XMLSchemaTypeExp, U extends JaxbTypeMetadata<T, ?>> {
    public Class<?> getBeanClass();

    public void setBeanClass(Class<?> beanClass);

    public boolean hasJaxbTypeMetadata();

    @Nullable
    public U getJaxbTypeMetadata();

    public void setJaxbTypeMetadata(@Nullable U jaxbTypeMetadata);

    public String getLocalName();

    public String getNamespacePrefix();

    public String getNamespaceUri();

    public QName getQname();
}
