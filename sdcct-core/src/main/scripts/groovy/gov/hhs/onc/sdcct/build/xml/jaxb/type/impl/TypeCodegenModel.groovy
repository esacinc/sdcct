package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import com.sun.codemodel.JDefinedClass
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AbstractCodegenModel
import javax.annotation.Nullable
import org.w3c.dom.Element

class TypeCodegenModel extends AbstractCodegenModel {
    private String version
    private CodegenTypeKind kind
    private String pattern
    private JDefinedClass classModel
    private JDefinedClass implClassModel

    TypeCodegenModel(@Nullable Element elem, String id, @Nullable String name, @Nullable String uri, @Nullable String version, CodegenTypeKind kind,
        @Nullable String pattern) {
        super(elem, id, name, uri)
        
        this.version = version
        this.kind = kind
        this.pattern = pattern
    }

    JDefinedClass getClassModel() {
        return this.classModel
    }

    void setClassModel(JDefinedClass classModel) {
        this.classModel = classModel
    }

    JDefinedClass getImplClassModel() {
        return this.implClassModel
    }

    void setImplClassModel(JDefinedClass implClassModel) {
        this.implClassModel = implClassModel
    }

    CodegenTypeKind getKind() {
        return this.kind
    }

    boolean hasPattern() {
        return (this.pattern != null)
    }
    
    @Nullable
    String getPattern() {
        return this.pattern
    }
    
    boolean hasVersion() {
        return (this.version != null)
    }
    
    @Nullable
    String getVersion() {
        return this.version
    }
}
