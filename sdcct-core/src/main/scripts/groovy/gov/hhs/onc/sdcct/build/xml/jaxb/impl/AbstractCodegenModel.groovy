package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import javax.annotation.Nullable
import org.w3c.dom.Element

abstract class AbstractCodegenModel {
    protected Element elem
    
    protected AbstractCodegenModel(@Nullable Element elem) {
        this.elem = elem
    }

    boolean hasElement() {
        return (this.elem != null)
    }
    
    @Nullable
    Element getElement() {
        return this.elem
    }
}
