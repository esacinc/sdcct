package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import gov.hhs.onc.sdcct.beans.IdentifiedBean
import gov.hhs.onc.sdcct.beans.NamedBean
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AbstractCodegenModel
import javax.annotation.Nullable
import org.w3c.dom.Element

abstract class AbstractTermCodegenModel extends AbstractCodegenModel implements IdentifiedBean, NamedBean {
    protected String id
    protected String name
    protected String uri
    
    protected AbstractTermCodegenModel(@Nullable Element elem, String id, @Nullable String name, @Nullable String uri) {
        super(elem)
        
        this.id = id
        this.name = name
        this.uri = uri
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
