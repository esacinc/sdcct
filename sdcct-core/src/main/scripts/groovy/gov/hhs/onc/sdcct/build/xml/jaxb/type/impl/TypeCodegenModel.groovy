package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import com.sun.codemodel.JDefinedClass
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AbstractCodegenModel
import javax.annotation.Nullable
import org.w3c.dom.Element

class TypeCodegenModel extends AbstractCodegenModel {
    private CodegenTypeKind kind
    private String path
    private JDefinedClass classModel
    private JDefinedClass implClassModel

    TypeCodegenModel(@Nullable Element elem, CodegenTypeKind kind, String path) {
        super(elem)
        
        this.kind = kind
        this.path = path
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
    
    String getPath() {
        return this.path
    }
}
