package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import gov.hhs.onc.sdcct.beans.IdentifiedBean
import gov.hhs.onc.sdcct.beans.NamedBean
import javax.annotation.Nullable
import org.w3c.dom.Element

abstract class AbstractCodegenModel implements IdentifiedBean, NamedBean {
    protected Element elem
    protected String id
    protected String name
    protected String uri
    
    protected AbstractCodegenModel(@Nullable Element elem, String id, @Nullable String name, @Nullable String uri) {
        this.elem = elem
        this.id = id
        this.name = name
        this.uri = uri
    }

    boolean hasElement() {
        return (this.elem != null)
    }
    
    @Nullable
    Element getElement() {
        return this.elem
    }

    @Override
    String getId() {
        return this.id
    }
    
    boolean hasName() {
        return (this.name != null)
    }
    
    @Nullable
    @Override
    String getName() {
        return this.name
    }
    
    boolean hasUri() {
        return (this.uri != null)
    }
    
    @Nullable
    String getUri() {
        return this.uri
    }
}
