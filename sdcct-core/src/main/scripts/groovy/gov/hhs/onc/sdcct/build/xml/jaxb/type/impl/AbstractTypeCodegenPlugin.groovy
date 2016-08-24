package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import com.sun.codemodel.JClass
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JMod
import com.sun.tools.xjc.Options
import gov.hhs.onc.sdcct.api.SpecificationType
import gov.hhs.onc.sdcct.api.type.DatatypeKindType
import gov.hhs.onc.sdcct.beans.TypeBean
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AbstractCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctFhirCodegenUtils
import javax.xml.namespace.QName
import org.apache.commons.collections4.BidiMap
import org.apache.commons.collections4.bidimap.TreeBidiMap
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject

abstract class AbstractTypeCodegenPlugin extends AbstractCodegenPlugin {
    protected Map<String, TypeCodegenModel> types
    protected Map<String, TypeCodegenModel> normalizedTypes
    protected BidiMap<String, String> typeClassSimpleNames
    
    protected AbstractTypeCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String basePkgName, String baseImplPkgName,
        JCodeModel codeModel, CodegenSchemaContext schemaContext, SpecificationType specType, String name, QName ... tagQnames) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, specType, name, tagQnames)
    }
    
    protected static JDefinedClass buildTypeClassModel(JCodeModel codeModel, TypeCodegenModel type, JDefinedClass typeClassModel,
        JDefinedClass typeImplClassModel, boolean implInterface) {
        if (implInterface) {
            typeClassModel._implements(TypeBean)
        }
        
        JClass typeKindTypeClassModel = codeModel.directClass(DatatypeKindType.name), strClassModel = codeModel.directClass(String.name)
        
        SdcctCodegenUtils.buildGetterMethod(typeClassModel, JMod.NONE, typeKindTypeClassModel, SdcctFhirCodegenUtils.TYPE_KIND_MEMBER_NAME, false, true, false)
        
        SdcctCodegenUtils.buildGetterMethod(typeImplClassModel, JMod.PUBLIC, typeKindTypeClassModel, SdcctFhirCodegenUtils.TYPE_KIND_MEMBER_NAME, false, true,
            false).body()._return(SdcctCodegenUtils.buildConstField(typeImplClassModel, typeKindTypeClassModel,
            SdcctFhirCodegenUtils.TYPE_KIND_MEMBER_NAME, typeKindTypeClassModel.staticRef(type.kind.name())))
        
        SdcctCodegenUtils.buildGetterMethod(typeClassModel, JMod.NONE, strClassModel, SdcctFhirCodegenUtils.TYPE_PATH_MEMBER_NAME, false, true, false)
        SdcctCodegenUtils.buildGetterMethod(typeImplClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.TYPE_PATH_MEMBER_NAME, false, true, false)
            .body()._return(SdcctCodegenUtils.buildConstField(typeImplClassModel, strClassModel, SdcctFhirCodegenUtils.TYPE_PATH_MEMBER_NAME, type.path))
    }
    
    @Override
    protected void onActivatedInternal(Options opts) throws Exception {
        this.types = new TreeMap<>()
        this.normalizedTypes = new TreeMap<>()
        this.typeClassSimpleNames = new TreeBidiMap<>()
    }
}
