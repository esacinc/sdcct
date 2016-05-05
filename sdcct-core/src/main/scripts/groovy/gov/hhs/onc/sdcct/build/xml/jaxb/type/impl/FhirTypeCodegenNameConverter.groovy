package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import com.sun.xml.xsom.XSComplexType
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.AbstractCodegenNameConverter
import org.apache.commons.collections4.BidiMap
import org.springframework.core.annotation.Order

@Order(1)
class FhirTypeCodegenNameConverter extends AbstractCodegenNameConverter {
    private BidiMap<String, String> typeClassSimpleNames

    FhirTypeCodegenNameConverter(BidiMap<String, String> typeClassSimpleNames) {
        this.typeClassSimpleNames = typeClassSimpleNames
    }

    @Override
    boolean canConvertComplexTypeClassName(XSComplexType schemaType, String token) {
        return this.typeClassSimpleNames.containsKey(schemaType.name)
    }

    @Override
    String toComplexTypeClassName(XSComplexType schemaType, String token) {
        return this.typeClassSimpleNames[schemaType.name]
    }
}
