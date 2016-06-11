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
import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import javax.xml.bind.annotation.XmlNs
import javax.xml.bind.annotation.XmlNsForm
import javax.xml.bind.annotation.XmlSchema
import org.apache.commons.collections4.BidiMap
import org.apache.commons.lang3.ClassUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler

class PackageCodegenPlugin extends AbstractCodegenPlugin {
    private final static String PKG_JAVADOC_FORMAT_PREFIX = "JAXB "
    
    private final static String PKG_JAVADOC_FORMAT_SUFFIX = "package (xmlNsPrefix={@value %s}, xmlNsUri={@value %s})."
    
    private final static String PKG_JAVADOC_FORMAT = PKG_JAVADOC_FORMAT_PREFIX + PKG_JAVADOC_FORMAT_SUFFIX
    private final static String IMPL_PKG_JAVADOC_FORMAT = PKG_JAVADOC_FORMAT_PREFIX + "implementation " + PKG_JAVADOC_FORMAT_SUFFIX
    
    private final static String XML_NS_PREFIXES_BINDING_VAR_NAME = "xmlNsPrefixes"
    private final static String XML_NS_URIS_BINDING_VAR_NAME = "xmlNsUris"
    
    private BidiMap<String, String> pkgNames
    private BidiMap<String, String> implPkgNames
    private Map<String, String> xmlNsPrefixBindingVarMap
    private Map<String, String> xmlNsUriBindingVarMap
    private Map<String, JFieldRef> xmlNsPrefixStaticRefModels
    private Map<String, JFieldRef> xmlNsUriStaticRefModels
    
    PackageCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, BidiMap<String, String> pkgNames, BidiMap<String, String> implPkgNames,
        String basePkgName, String baseImplPkgName, JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-pkg")
        
        this.pkgNames = pkgNames
        this.implPkgNames = implPkgNames
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        JFieldRef xmlNsFormQualifiedStaticRefModel = codeModel.directClass(XmlNsForm.name).staticRef(XmlNsForm.QUALIFIED.name())
        JPackage implPkg
        JAnnotationUse xmlSchemaAnnoModel
        
        this.implPkgNames.each{
            if ((xmlSchemaAnnoModel = (implPkg = SdcctCodegenUtils.findPackage(codeModel, it.value)).annotations()
                .find{ (it.annotationClass.fullName() == XmlSchema.name) }) == null) {
                (xmlSchemaAnnoModel = implPkg.annotate(XmlSchema)).param(SdcctCodegenUtils.ELEM_FORM_DEFAULT_ANNO_PARAM_NAME, xmlNsFormQualifiedStaticRefModel)
            }
            
            xmlSchemaAnnoModel.param(SdcctCodegenUtils.NS_ANNO_PARAM_NAME, this.xmlNsUriStaticRefModels[it.key])
                .paramArray(SdcctCodegenUtils.XMLNS_ANNO_PARAM_NAME).annotate(XmlNs).param(SdcctCodegenUtils.PREFIX_ANNO_PARAM_NAME,
                this.xmlNsPrefixStaticRefModels[it.key]).param(SdcctCodegenUtils.NS_URI_ANNO_PARAM_NAME, xmlNsUriStaticRefModels[it.key])
            
            SdcctCodegenUtils.copyAnnotations(implPkg, SdcctCodegenUtils.findPackage(codeModel, this.pkgNames[it.key]))
        }
        
        this.schemaContext.namespacePrefixStaticRefModels = this.xmlNsPrefixStaticRefModels
        this.schemaContext.namespaceUriStaticRefModels = this.xmlNsUriStaticRefModels
    }

    @Override
    protected void postProcessModelInternal(Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        super.postProcessModelInternal(model, codeModel, opts, errorHandler)
        
        this.xmlNsPrefixStaticRefModels = buildXmlNsStaticRefModels(codeModel,
            (this.xmlNsPrefixBindingVarMap = this.buildXmlNsBindingVarMap(XML_NS_PREFIXES_BINDING_VAR_NAME)))
        this.xmlNsUriStaticRefModels = buildXmlNsStaticRefModels(codeModel,
            (this.xmlNsUriBindingVarMap = this.buildXmlNsBindingVarMap(XML_NS_URIS_BINDING_VAR_NAME)))
        
        String pkgName, xmlNsPrefixBindingVarValue, xmlNsUriBindingVarValue
        JPackage pkg, implPkg
        
        this.implPkgNames.each{
            pkgName = this.pkgNames[it.key]
            
            if (SdcctCodegenUtils.hasPackage(codeModel, it.value)) {
                pkg = SdcctCodegenUtils.findPackage(codeModel, pkgName)
                implPkg = SdcctCodegenUtils.findPackage(codeModel, it.value)
            } else {
                pkg = codeModel._package(pkgName)
                implPkg = codeModel._package(it.value)
            }
            
            pkg.javadoc().append(String.format(PKG_JAVADOC_FORMAT,
                (xmlNsPrefixBindingVarValue = buildJavadocXmlNsXdocletValue(this.xmlNsPrefixBindingVarMap[it.key])),
                (xmlNsUriBindingVarValue = buildJavadocXmlNsXdocletValue(this.xmlNsUriBindingVarMap[it.key]))))
            implPkg.javadoc().append(String.format(IMPL_PKG_JAVADOC_FORMAT, xmlNsPrefixBindingVarValue, xmlNsUriBindingVarValue))
        }
    }

    private static String buildJavadocXmlNsXdocletValue(String xmlNsBindingVarValue) {
        return (ClassUtils.getPackageName(xmlNsBindingVarValue) + SdcctStringUtils.HASH + ClassUtils.getShortClassName(xmlNsBindingVarValue))
    }
    
    private static Map<String, JFieldRef> buildXmlNsStaticRefModels(JCodeModel codeModel, Map<String, String> bindingVarMap) {
        return bindingVarMap.collectEntries(new LinkedHashMap<>(), { [ it.key, codeModel.directClass(ClassUtils.getPackageName(it.value))
            .staticRef(ClassUtils.getShortClassName(it.value)) ] })
    }
    
    private Map<String, String> buildXmlNsBindingVarMap(String bindingVarName) {
        return SdcctBuildUtils.tokenizeMap(((String) this.bindingVars[bindingVarName]))
    }
}
