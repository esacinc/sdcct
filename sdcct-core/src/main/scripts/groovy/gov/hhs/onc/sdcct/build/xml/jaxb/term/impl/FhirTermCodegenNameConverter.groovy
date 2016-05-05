package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import com.sun.xml.xsom.XSComplexType
import com.sun.xml.xsom.XSRestrictionSimpleType
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.AbstractCodegenNameConverter
import org.apache.commons.collections4.BidiMap
import org.apache.commons.lang3.StringUtils
import org.springframework.core.annotation.Order

@Order(0)
class FhirTermCodegenNameConverter extends AbstractCodegenNameConverter {
    private BidiMap<String, String> enumClassSimpleNames
    private BidiMap<String, String> enumComponentClassSimpleNames

    FhirTermCodegenNameConverter(BidiMap<String, String> enumClassSimpleNames, BidiMap<String, String> enumComponentClassSimpleNames) {
        this.enumClassSimpleNames = enumClassSimpleNames
        this.enumComponentClassSimpleNames = enumComponentClassSimpleNames
    }

    @Override
    boolean canConvertComplexTypeClassName(XSComplexType schemaType, String token) {
        return this.enumComponentClassSimpleNames.containsKey(schemaType.name)
    }

    @Override
    String toComplexTypeClassName(XSComplexType schemaType, String token) {
        return this.enumComponentClassSimpleNames[schemaType.name]
    }

    @Override
    boolean canConvertEnumTypeClassName(XSRestrictionSimpleType schemaType, String token) {
        return StringUtils.endsWith(schemaType.name, FhirTermCodegenPlugin.LIST_TOKEN_SUFFIX)
    }

    @Override
    String toEnumTypeClassName(XSRestrictionSimpleType schemaType, String token) {
        String enumClassSimpleName
        
        this.enumClassSimpleNames[token] = (enumClassSimpleName = super.toEnumTypeClassName(schemaType, (token = StringUtils.removeEnd(token,
            FhirTermCodegenPlugin.LIST_TOKEN_SUFFIX))))
        this.enumComponentClassSimpleNames[token] = (this.toClassName(token) + FhirTermCodegenPlugin.COMPONENT_CLASS_NAME_SUFFIX)
        
        return enumClassSimpleName
    }
}
