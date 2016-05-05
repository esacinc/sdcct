package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AbstractCodegenModel
import javax.annotation.Nullable
import org.w3c.dom.Element

abstract class AbstractTermCodegenModel extends AbstractCodegenModel {
    protected AbstractTermCodegenModel(@Nullable Element elem, String id, @Nullable String name, @Nullable String uri) {
        super(elem, id, name, uri)
    }
}
