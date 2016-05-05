package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import javax.annotation.Nullable
import org.w3c.dom.Element

abstract class AbstractContainerTermCodegenModel extends AbstractTermCodegenModel {
    protected String oid
    protected String version
    
    protected AbstractContainerTermCodegenModel(@Nullable Element elem, String id, @Nullable String name, @Nullable String uri, @Nullable String oid,
        @Nullable String version) {
        super(elem, id, name, uri)
        
        this.oid = oid
        this.version = version
    }

    boolean hasOid() {
        return (this.oid != null)
    }
    
    @Nullable
    String getOid() {
        return this.oid
    }
    
    boolean hasVersion() {
        return (this.version != null)
    }
    
    @Nullable
    String getVersion() {
        return this.version
    }
}
