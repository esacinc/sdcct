package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.github.sebhoss.warnings.CompilerWarnings
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JFieldRef
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JInvocation
import com.sun.codemodel.JMethod
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.generator.bean.DualObjectFactoryGenerator
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import javax.xml.bind.annotation.XmlElementDecl
import javax.xml.namespace.QName
import org.apache.commons.collections4.BidiMap
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler

class ObjectFactoryCodegenPlugin extends AbstractCodegenPlugin {
    private final static String QNAME_CONST_FIELD_NAME_SUFFIX = "_QNAME"
    
    private BidiMap<String, String> pkgNames
    private BidiMap<String, String> implPkgNames
    
    ObjectFactoryCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, BidiMap<String, String> pkgNames,
        BidiMap<String, String> implPkgNames, String basePkgName, String baseImplPkgName, JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-obj-factory")
        
        this.pkgNames = pkgNames
        this.implPkgNames = implPkgNames
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        Map<String, JFieldRef> xmlNsUriStaticRefModels = this.schemaContext.namespaceUriStaticRefModels
        DualObjectFactoryGenerator dualObjFactoryGen
        Map<String, JFieldVar> objFactoryFields
        String initialObjFactoryQnameConstFieldName, objFactoryQnameConstFieldName, pkgName
        JFieldRef xmlNsUriStaticRefModel
        
        this.pkgNames.values().each{
            outline.getPackageContext(SdcctCodegenUtils.findPackage(codeModel, it)).each {
                [
                    (dualObjFactoryGen = ((DualObjectFactoryGenerator)it.objectFactoryGenerator())).publicOFG.objectFactory,
                    dualObjFactoryGen.privateOFG.objectFactory
                ].each { JDefinedClass objFactoryClassModel ->
                    xmlNsUriStaticRefModel = xmlNsUriStaticRefModels[(this.pkgNames.containsValue((pkgName = objFactoryClassModel._package().name())) ?
                        this.pkgNames.getKey(pkgName) : this.implPkgNames.getKey(pkgName))]

                    SdcctCodegenUtils.findFields(objFactoryClassModel, { (it.type().fullName() == QName.name) }).each {
                        initialObjFactoryQnameConstFieldName = it.name()

                        it.mods().setPublic()

                        objFactoryQnameConstFieldName = (StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(StringUtils.splitPreserveAllTokens(
                            initialObjFactoryQnameConstFieldName, SdcctStringUtils.UNDERSCORE, 3)[1]), SdcctStringUtils.UNDERSCORE).toUpperCase()
                            + QNAME_CONST_FIELD_NAME_SUFFIX)

                        (objFactoryFields = SdcctCodegenUtils.buildFields(objFactoryClassModel)).remove(initialObjFactoryQnameConstFieldName)

                        it.name(objFactoryQnameConstFieldName)

                        objFactoryFields[objFactoryQnameConstFieldName] = it

                        SdcctCodegenUtils.buildInvocationArgs(((JInvocation)SdcctCodegenUtils.buildVariableInitializationExpression(it))).set(0,
                            xmlNsUriStaticRefModel)
                    }

                    SdcctCodegenUtils.findMethods(objFactoryClassModel, { SdcctCodegenUtils.hasAnnotation(((JMethod)it), XmlElementDecl) }).each {
                        SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, it, CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED)

                        SdcctCodegenUtils.findAnnotation(it, XmlElementDecl).param(SdcctCodegenUtils.NS_ANNO_PARAM_NAME, xmlNsUriStaticRefModel)
                    }
                }
            }
        }
    }
}
