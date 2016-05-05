package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.codemodel.JAnnotationUse
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JFieldRef
import com.sun.codemodel.JPackage
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import javax.xml.bind.annotation.XmlNs
import javax.xml.bind.annotation.XmlNsForm
import javax.xml.bind.annotation.XmlSchema
import org.apache.commons.lang3.ClassUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler

class PackageCodegenPlugin extends AbstractCodegenPlugin {
    private final static String XML_NS_PREFIXES_BINDING_VAR_NAME = "xmlNsPrefixes"
    private final static String XML_NS_URIS_BINDING_VAR_NAME = "xmlNsUris"
    
    final static ELEM_FORM_DEFAULT_ANNO_PARAM_NAME = "elementFormDefault"
    final static String NS_ANNO_PARAM_NAME = "namespace"
    final static String NS_URI_ANNO_PARAM_NAME = NS_ANNO_PARAM_NAME + "URI"
    final static String PREFIX_ANNO_PARAM_NAME = "prefix"
    final static String XMLNS_ANNO_PARAM_NAME = "xmlns"
    
    private Map<String, String> pkgNames
    private Map<String, String> implPkgNames
    
    PackageCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, Map<String, String> pkgNames, Map<String, String> implPkgNames,
        String basePkgName, String baseImplPkgName, JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-pkg")
        
        this.pkgNames = pkgNames
        this.implPkgNames = implPkgNames
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        Map<String, JFieldRef> xmlNsPrefixStaticRefModels = this.buildXmlNsStaticRefModels(codeModel, XML_NS_PREFIXES_BINDING_VAR_NAME)
        Map<String, JFieldRef> xmlNsUriStaticRefModels = this.buildXmlNsStaticRefModels(codeModel, XML_NS_URIS_BINDING_VAR_NAME)
        JFieldRef xmlNsFormQualifiedStaticRefModel = codeModel.directClass(XmlNsForm.name).staticRef(XmlNsForm.QUALIFIED.name())
        JPackage implPkg
        JAnnotationUse xmlSchemaAnnoModel
        
        this.implPkgNames.each{
            if (!SdcctCodegenUtils.hasPackage(codeModel, it.value)) {
                return
            }
            
            if ((xmlSchemaAnnoModel = (implPkg = SdcctCodegenUtils.findPackage(codeModel, it.value)).annotations()
                .find{ (it.annotationClass.fullName() == XmlSchema.name) }) == null) {
                (xmlSchemaAnnoModel = implPkg.annotate(XmlSchema)).param(ELEM_FORM_DEFAULT_ANNO_PARAM_NAME, xmlNsFormQualifiedStaticRefModel)
            }
            
            xmlSchemaAnnoModel.param(NS_ANNO_PARAM_NAME, xmlNsUriStaticRefModels[it.key]).paramArray(XMLNS_ANNO_PARAM_NAME)
                .annotate(XmlNs).param(PREFIX_ANNO_PARAM_NAME, xmlNsPrefixStaticRefModels[it.key]).param(NS_URI_ANNO_PARAM_NAME,
                xmlNsUriStaticRefModels[it.key])
        }
    }
    
    private Map<String, JFieldRef> buildXmlNsStaticRefModels(JCodeModel codeModel, String bindingVarName) {
        return SdcctBuildUtils.tokenizeMap(((String) this.bindingVars[bindingVarName])).collectEntries(new LinkedHashMap<>(),
            { [ it.key, codeModel.directClass(ClassUtils.getPackageName(it.value)).staticRef(ClassUtils.getShortClassName(it.value)) ] })
    }
}
