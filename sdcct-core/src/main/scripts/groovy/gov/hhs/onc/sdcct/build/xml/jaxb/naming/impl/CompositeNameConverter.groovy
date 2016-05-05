package gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl

import com.sun.xml.bind.api.impl.NameConverter
import com.sun.xml.xsom.XSComplexType
import com.sun.xml.xsom.XSRestrictionSimpleType
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.CodegenNameConverter
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.core.annotation.Order

class CompositeNameConverter implements NameConverter {
    @Order(Ordered.LOWEST_PRECEDENCE)
    static class DefaultCodegenNameConverter extends AbstractCodegenNameConverter {
        @Override
        boolean canConvertPackageName(String nsUri) {
            return true
        }

        @Override
        boolean canConvertComplexTypeClassName(XSComplexType schemaType, String token) {
            return true
        }

        @Override
        boolean canConvertEnumTypeClassName(XSRestrictionSimpleType schemaType, String token) {
            return true
        }

        @Override
        boolean canConvertClassName(String token) {
            return true
        }

        @Override
        boolean canConvertInterfaceName(String token) {
            return true
        }

        @Override
        boolean canConvertConstantName(String token) {
            return true
        }

        @Override
        boolean canConvertPropertyName(String token) {
            return true
        }

        @Override
        boolean canConvertVariableName(String token) {
            return true
        }
    }
    
    private CodegenSchemaContext schemaContext
    private Set<CodegenNameConverter> delegates = new TreeSet<>(AnnotationAwareOrderComparator.INSTANCE)
    
    CompositeNameConverter(CodegenSchemaContext schemaContext) {
        this.schemaContext = schemaContext
        
        this.delegates.add(new DefaultCodegenNameConverter())
    }

    @Override
    String toPackageName(String nsUri) {
        return this.delegates.find{ it.canConvertPackageName(nsUri) }.toPackageName(nsUri)
    }

    @Override
    String toClassName(String token) {
        if (this.schemaContext.hasSchemas()) {
            if (this.schemaContext.enumTypes.containsKey(token)) {
                XSRestrictionSimpleType enumSchemaType = this.schemaContext.enumTypes[token]
                
                return this.delegates.find{ it.canConvertEnumTypeClassName(enumSchemaType, token) }.toEnumTypeClassName(enumSchemaType, token)
            } else if (this.schemaContext.complexTypes.containsKey(token)) {
                XSComplexType complexSchemaType = this.schemaContext.complexTypes[token]
                
                return this.delegates.find{ it.canConvertComplexTypeClassName(complexSchemaType, token) }.toComplexTypeClassName(complexSchemaType, token)
            }
        }
        
        return this.delegates.find{ it.canConvertClassName(token) }.toClassName(token)
    }
    
    @Override
    String toInterfaceName(String token) {
        return this.delegates.find{ it.canConvertInterfaceName(token) }.toInterfaceName(token)
    }

    @Override
    String toConstantName(String token) {
        return this.delegates.find{ it.canConvertConstantName(token) }.toConstantName(token)
    }

    @Override
    String toPropertyName(String token) {
        return this.delegates.find{ it.canConvertPropertyName(token) }.toPropertyName(token)
    }

    @Override
    String toVariableName(String token) {
        return this.delegates.find{ it.canConvertVariableName(token) }.toVariableName(token)
    }
    
    Set<CodegenNameConverter> getDelegates() {
        return this.delegates
    }
}
