package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JExpr
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JMod
import com.sun.tools.xjc.Options
import gov.hhs.onc.sdcct.beans.SpecificationType
import gov.hhs.onc.sdcct.beans.TypeBean
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AbstractCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctFhirCodegenUtils
import java.util.regex.Pattern
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
        
        JFieldVar typeIdConstFieldModel = SdcctCodegenUtils.buildConstField(typeImplClassModel, String, SdcctFhirCodegenUtils.TYPE_ID_MEMBER_NAME, type.id),
            typeNameConstFieldModel = SdcctCodegenUtils.buildConstField(typeImplClassModel, String, SdcctFhirCodegenUtils.TYPE_NAME_MEMBER_NAME, type.name),
            typePatternConstFieldModel = SdcctCodegenUtils.buildConstPatternField(typeImplClassModel, SdcctFhirCodegenUtils.TYPE_PATTERN_MEMBER_NAME,
                type.pattern),
            typeUriConstFieldModel = SdcctCodegenUtils.buildConstUriField(typeImplClassModel, SdcctFhirCodegenUtils.TYPE_URI_MEMBER_NAME, type.uri),
            typeVersionConstFieldModel = SdcctCodegenUtils.buildConstField(typeImplClassModel, String, SdcctFhirCodegenUtils.TYPE_VERSION_MEMBER_NAME,
                type.version)
        
        SdcctCodegenUtils.buildGetterMethod(typeClassModel, JMod.NONE, String, SdcctFhirCodegenUtils.TYPE_ID_MEMBER_NAME, true, false)
        SdcctCodegenUtils.buildGetterMethod(typeImplClassModel, JMod.PUBLIC, String, SdcctFhirCodegenUtils.TYPE_ID_MEMBER_NAME, true, false).body()
            ._return(typeIdConstFieldModel)
        
        SdcctCodegenUtils.buildGetterMethod(typeClassModel, JMod.NONE, String, SdcctFhirCodegenUtils.TYPE_NAME_MEMBER_NAME, true, false)
        SdcctCodegenUtils.buildGetterMethod(typeImplClassModel, JMod.PUBLIC, String, SdcctFhirCodegenUtils.TYPE_NAME_MEMBER_NAME, true, false).body()
            ._return(typeNameConstFieldModel)
        
        SdcctCodegenUtils.buildHasMethod(typeClassModel, JMod.NONE, SdcctFhirCodegenUtils.TYPE_PATTERN_MEMBER_NAME, true)
        SdcctCodegenUtils.buildHasMethod(typeImplClassModel, JMod.PUBLIC, SdcctFhirCodegenUtils.TYPE_PATTERN_MEMBER_NAME, true).body()
            ._return(typePatternConstFieldModel.ne(JExpr._null()))
        SdcctCodegenUtils.buildGetterMethod(typeClassModel, JMod.NONE, Pattern, SdcctFhirCodegenUtils.TYPE_PATTERN_MEMBER_NAME, true, true)
        SdcctCodegenUtils.buildGetterMethod(typeImplClassModel, JMod.PUBLIC, Pattern, SdcctFhirCodegenUtils.TYPE_PATTERN_MEMBER_NAME, true, true).body()
            ._return(typePatternConstFieldModel)
        
        SdcctCodegenUtils.buildHasMethod(typeClassModel, JMod.NONE, SdcctFhirCodegenUtils.TYPE_URI_MEMBER_NAME, true)
        SdcctCodegenUtils.buildHasMethod(typeImplClassModel, JMod.PUBLIC, SdcctFhirCodegenUtils.TYPE_URI_MEMBER_NAME, true).body()
            ._return(typeUriConstFieldModel.ne(JExpr._null()))
        SdcctCodegenUtils.buildGetterMethod(typeClassModel, JMod.NONE, URI, SdcctFhirCodegenUtils.TYPE_URI_MEMBER_NAME, true, true)
        SdcctCodegenUtils.buildGetterMethod(typeImplClassModel, JMod.PUBLIC, URI, SdcctFhirCodegenUtils.TYPE_URI_MEMBER_NAME, true, true).body()
            ._return(typeUriConstFieldModel)
        
        SdcctCodegenUtils.buildHasMethod(typeClassModel, JMod.NONE, SdcctFhirCodegenUtils.TYPE_VERSION_MEMBER_NAME, true)
        SdcctCodegenUtils.buildHasMethod(typeImplClassModel, JMod.PUBLIC, SdcctFhirCodegenUtils.TYPE_VERSION_MEMBER_NAME, true).body()
            ._return(typeVersionConstFieldModel.ne(JExpr._null()))
        SdcctCodegenUtils.buildGetterMethod(typeClassModel, JMod.NONE, String, SdcctFhirCodegenUtils.TYPE_VERSION_MEMBER_NAME, true, true)
        SdcctCodegenUtils.buildGetterMethod(typeImplClassModel, JMod.PUBLIC, String, SdcctFhirCodegenUtils.TYPE_VERSION_MEMBER_NAME, true, true).body()
            ._return(typeVersionConstFieldModel)
    }
    
    @Override
    protected void onActivatedInternal(Options opts) throws Exception {
        this.types = new TreeMap<>()
        this.normalizedTypes = new TreeMap<>()
        this.typeClassSimpleNames = new TreeBidiMap<>()
    }
}
