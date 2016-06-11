package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import com.sun.codemodel.JBlock
import com.sun.codemodel.JClass
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JDocComment
import com.sun.codemodel.JEnumConstant
import com.sun.codemodel.JExpr
import com.sun.codemodel.JMethod
import com.sun.codemodel.JMod
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.beans.SpecificationType
import gov.hhs.onc.sdcct.beans.StaticValueSetBean
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AbstractCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctFhirCodegenUtils
import gov.hhs.onc.sdcct.net.SdcctUris
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes
import javax.annotation.Nullable
import javax.xml.bind.annotation.XmlEnumValue
import javax.xml.namespace.QName
import org.apache.commons.collections4.BidiMap
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.bidimap.TreeBidiMap
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.jvnet.jaxb2_commons.util.CustomizationUtils
import org.xml.sax.ErrorHandler

abstract class AbstractTermCodegenPlugin extends AbstractCodegenPlugin {
    final static String STATIC_VALUE_SET_ENUM_ELEM_NAME = "static-value-set-enum"
    
    final static QName STATIC_VALUE_SET_ENUM_ELEM_QNAME = new QName(SdcctUris.SDCCT_JAXB_URN_VALUE, STATIC_VALUE_SET_ENUM_ELEM_NAME,
        SdcctXmlPrefixes.SDCCT_JAXB)
    
    protected Map<String, CodeSystemCodegenModel> codeSystems
    protected Map<String, ValueSetCodegenModel> valueSets
    protected Map<String, String> staticValueSetEnums
    protected Map<String, JDefinedClass> enumClassModels = new TreeMap<>()
    protected Map<String, Map<String, JEnumConstant>> enumConstModels = new TreeMap<>()
    protected BidiMap<String, String> enumClassSimpleNames
    protected BidiMap<String, String> enumComponentClassSimpleNames
    
    protected AbstractTermCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String basePkgName, String baseImplPkgName,
        JCodeModel codeModel, CodegenSchemaContext schemaContext, SpecificationType specType, String name) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, specType, name, STATIC_VALUE_SET_ENUM_ELEM_QNAME)
    }

    protected static JEnumConstant buildStaticValueSetEnumConstantModel(JCodeModel codeModel, JDefinedClass enumClassModel, String enumConstName,
        ConceptCodegenModel concept, @Nullable JDocComment initialJavadocModel, @Nullable String javadoc, boolean annotate) {
        ValueSetCodegenModel valueSet = concept.valueSet
        CodeSystemCodegenModel codeSystem = concept.codeSystem
        JEnumConstant enumConstModel = enumClassModel.enumConstant(enumConstName)
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(valueSet.id))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(valueSet.name))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(valueSet.oid))
        enumConstModel.arg(codeModel.ref(URI).staticInvoke(SdcctCodegenUtils.CREATE_METHOD_NAME).arg(SdcctCodegenUtils.buildLiteralExpression(valueSet.uri)))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(valueSet.version))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(codeSystem.id))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(codeSystem.name))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(codeSystem.oid))
        enumConstModel.arg(codeModel.ref(URI).staticInvoke(SdcctCodegenUtils.CREATE_METHOD_NAME).arg(SdcctCodegenUtils.buildLiteralExpression(codeSystem.uri)))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(codeSystem.version))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(concept.id))
        enumConstModel.arg(SdcctCodegenUtils.buildLiteralExpression(concept.name))
        
        if (annotate) {
            enumConstModel.annotate(XmlEnumValue).param(SdcctCodegenUtils.VALUE_MEMBER_NAME, concept.id)
        }
        
        JDocComment javadocModel = enumConstModel.javadoc()
        
        if (!CollectionUtils.isEmpty(initialJavadocModel)) {
            javadocModel.addAll(initialJavadocModel)
        }
        
        if (!StringUtils.isBlank(javadoc)) {
            javadocModel.append(javadoc)
        }
        
        return enumConstModel
    }
    
    protected static JDefinedClass buildStaticValueSetEnumClassModel(JCodeModel codeModel, ValueSetCodegenModel valueSet,
        JDefinedClass valueSetEnumClassModel) {
        valueSetEnumClassModel._implements(StaticValueSetBean)
        
        JClass strClassModel = codeModel.directClass(String.name), uriClassModel = codeModel.directClass(URI.name)
        
        SdcctCodegenUtils.buildConstField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_ID_MEMBER_NAME, valueSet.id)
        SdcctCodegenUtils.buildConstField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_NAME_MEMBER_NAME, valueSet.name)
        SdcctCodegenUtils.buildConstField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_OID_MEMBER_NAME, valueSet.oid)
        SdcctCodegenUtils.buildConstUriField(valueSetEnumClassModel, SdcctFhirCodegenUtils.VALUE_SET_URI_MEMBER_NAME, valueSet.uri)
        SdcctCodegenUtils.buildConstField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_VERSION_MEMBER_NAME, valueSet.version)
        
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_ID_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_NAME_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_OID_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, uriClassModel, SdcctFhirCodegenUtils.VALUE_SET_URI_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_VERSION_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_ID_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_NAME_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_OID_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, uriClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_URI_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_VERSION_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctCodegenUtils.ID_MEMBER_NAME)
        SdcctCodegenUtils.buildEnumField(valueSetEnumClassModel, strClassModel, SdcctCodegenUtils.NAME_MEMBER_NAME)
        
        JMethod valueSetEnumConstructorModel = valueSetEnumClassModel.constructor(JMod.PRIVATE)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.VALUE_SET_ID_MEMBER_NAME)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.VALUE_SET_NAME_MEMBER_NAME)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.VALUE_SET_OID_MEMBER_NAME)
        valueSetEnumConstructorModel.param(uriClassModel, SdcctFhirCodegenUtils.VALUE_SET_URI_MEMBER_NAME)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.VALUE_SET_VERSION_MEMBER_NAME).annotate(Nullable)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_ID_MEMBER_NAME)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_NAME_MEMBER_NAME)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_OID_MEMBER_NAME)
        valueSetEnumConstructorModel.param(uriClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_URI_MEMBER_NAME)
        valueSetEnumConstructorModel.param(strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_VERSION_MEMBER_NAME).annotate(Nullable)
        valueSetEnumConstructorModel.param(strClassModel, SdcctCodegenUtils.ID_MEMBER_NAME)
        valueSetEnumConstructorModel.param(strClassModel, SdcctCodegenUtils.NAME_MEMBER_NAME).annotate(Nullable)
        
        JBlock valueSetEnumConstructorModelBody = valueSetEnumConstructorModel.body()
        
        valueSetEnumConstructorModel.params().each{ valueSetEnumConstructorModelBody.assign(JExpr._this().ref(it.name()), it) }
        
        SdcctCodegenUtils.buildEnumFromValueMethod(valueSetEnumClassModel)
        SdcctCodegenUtils.buildEnumValueMethod(valueSetEnumClassModel, SdcctCodegenUtils.ID_MEMBER_NAME)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_ID_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_NAME_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_OID_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, uriClassModel, SdcctFhirCodegenUtils.VALUE_SET_URI_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldHasMethod(valueSetEnumClassModel, JMod.PUBLIC, SdcctFhirCodegenUtils.VALUE_SET_VERSION_MEMBER_NAME, true)
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.VALUE_SET_VERSION_MEMBER_NAME,
            false, true, true)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_ID_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_NAME_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_OID_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, uriClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_URI_MEMBER_NAME, false,
            true, false)
        
        SdcctCodegenUtils.buildFieldHasMethod(valueSetEnumClassModel, JMod.PUBLIC, SdcctFhirCodegenUtils.CODE_SYSTEM_VERSION_MEMBER_NAME, true)
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctFhirCodegenUtils.CODE_SYSTEM_VERSION_MEMBER_NAME,
            false, true, true)
        
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctCodegenUtils.ID_MEMBER_NAME, false, true, false)
        
        SdcctCodegenUtils.buildFieldHasMethod(valueSetEnumClassModel, JMod.PUBLIC, SdcctCodegenUtils.NAME_MEMBER_NAME, false)
        SdcctCodegenUtils.buildFieldGetterMethod(valueSetEnumClassModel, JMod.PUBLIC, strClassModel, SdcctCodegenUtils.NAME_MEMBER_NAME, false, true, true)
        
        return valueSetEnumClassModel
    }
    
    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        String enumClassSimpleName
        
        outline.enums.each{
            this.enumClassModels[(enumClassSimpleName = it.target.shortName)] = it.clazz
            
            this.enumConstModels[enumClassSimpleName] = it.constants.collectEntries(new TreeMap<String, JEnumConstant>(),
                { Collections.singletonMap(it.target.lexicalValue, it.constRef) })
        }
    }
    
    @Override
    protected void postProcessModelInternal(Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        super.postProcessModelInternal(model, codeModel, opts, errorHandler)
        
        this.staticValueSetEnums = CustomizationUtils.findCustomizations(model, STATIC_VALUE_SET_ENUM_ELEM_QNAME)
            .findAll{ Objects.equals(it.element.getAttribute(SPEC_TYPE_ATTR_NAME), this.specType.id) }
            .collectEntries(new TreeMap<>(), { Collections.singletonMap(it.element.getAttribute(SdcctFhirCodegenUtils.URI_ATTR_NAME),
            it.element.getAttribute(SdcctFhirCodegenUtils.CLASS_ATTR_NAME)) })
    }

    @Override
    protected void onActivatedInternal(Options opts) throws Exception {
        this.codeSystems = new TreeMap<>()
        this.valueSets = new TreeMap<>()
        this.enumClassSimpleNames = new TreeBidiMap<>()
        this.enumComponentClassSimpleNames = new TreeBidiMap<>()
    }
}
