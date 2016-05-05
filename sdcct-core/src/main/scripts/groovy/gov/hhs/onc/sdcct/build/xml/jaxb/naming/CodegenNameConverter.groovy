package gov.hhs.onc.sdcct.build.xml.jaxb.naming

import com.sun.xml.bind.api.impl.NameConverter
import com.sun.xml.xsom.XSComplexType
import com.sun.xml.xsom.XSRestrictionSimpleType

interface CodegenNameConverter extends NameConverter {
    boolean canConvertPackageName(String nsUri)
    
    boolean canConvertComplexTypeClassName(XSComplexType schemaType, String token)
    
    String toComplexTypeClassName(XSComplexType schemaType, String token)
    
    boolean canConvertEnumTypeClassName(XSRestrictionSimpleType schemaType, String token)
    
    String toEnumTypeClassName(XSRestrictionSimpleType schemaType, String token)
    
    boolean canConvertClassName(String token)
    
    boolean canConvertInterfaceName(String token)
    
    boolean canConvertConstantName(String token)
    
    boolean canConvertPropertyName(String token)
    
    boolean canConvertVariableName(String token)
}