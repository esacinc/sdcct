package gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl

import com.sun.xml.xsom.XSComplexType
import com.sun.xml.xsom.XSRestrictionSimpleType
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.CodegenNameConverter
import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.StrBuilder

abstract class AbstractCodegenNameConverter implements CodegenNameConverter {
    @Override
    boolean canConvertPackageName(String nsUri) {
        return false
    }
    
    @Override
    String toPackageName(String nsUri) {
        return smart.toPackageName(nsUri)
    }
    
    @Override
    boolean canConvertComplexTypeClassName(XSComplexType schemaType, String token) {
        return false
    }
    
    @Override
    String toComplexTypeClassName(XSComplexType schemaType, String token) {
        return smart.toClassName(processName(token))
    }
    
    @Override
    boolean canConvertEnumTypeClassName(XSRestrictionSimpleType schemaType, String token) {
        return false
    }
    
    @Override
    String toEnumTypeClassName(XSRestrictionSimpleType schemaType, String token) {
        return smart.toClassName(processName(token))
    }
    
    @Override
    boolean canConvertClassName(String token) {
        return false
    }
    
    @Override
    String toClassName(String token) {
        return processName(smart.toClassName(token))
    }
    
    @Override
    boolean canConvertInterfaceName(String token) {
        return false
    }
    
    @Override
    String toInterfaceName(String token) {
        return smart.toInterfaceName(processName(token))
    }
    
    @Override
    boolean canConvertConstantName(String token) {
        return false
    }
    
    @Override
    String toConstantName(String token) {
        String constName = smart.toConstantName(token)
        
        return (Character.isDigit(constName.toCharArray()[0]) ? (SdcctStringUtils.UNDERSCORE + constName) : constName)
    }
    
    @Override
    boolean canConvertPropertyName(String token) {
        return false
    }
    
    @Override
    String toPropertyName(String token) {
        return smart.toPropertyName(processName(token))
    }

    @Override
    boolean canConvertVariableName(String token) {
        return false
    }
    
    @Override
    String toVariableName(String token) {
        return smart.toVariableName(token)
    }

    protected static String processName(String name) {
        StrBuilder nameBuilder = new StrBuilder();
        String[] nameParts = StringUtils.splitByCharacterTypeCamelCase(StringUtils.capitalize(name))
        String namePart
        boolean collapseNameParts = false

        for (int a = 0; a < nameParts.length; a++) {
            if ((namePart = nameParts[a]).length() == 1) {
                nameBuilder.append(Character.toUpperCase(namePart.charAt(0)))
                
                collapseNameParts = true
                
                continue
            }
            
            namePart = namePart.toLowerCase()
            
            if (!collapseNameParts) {
                namePart = StringUtils.capitalize(namePart);
            } else {
                collapseNameParts = false
            }

            nameBuilder.append(namePart);
        }

        return nameBuilder.build();
    }
}
