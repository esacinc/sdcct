package gov.hhs.onc.sdcct.build.xml.jaxb.term.impl

import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JDocComment
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.CClassInfoParent.Package
import com.sun.tools.xjc.model.CCustomizations
import com.sun.tools.xjc.model.CEnumConstant
import com.sun.tools.xjc.model.CEnumLeafInfo
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.model.TypeUse
import com.sun.tools.xjc.outline.Outline
import com.sun.xml.xsom.XSRestrictionSimpleType
import com.sun.xml.xsom.XSSchemaSet
import gov.hhs.onc.sdcct.SdcctPackages
import gov.hhs.onc.sdcct.api.SpecificationType
import gov.hhs.onc.sdcct.beans.StaticValueSetComponentBean
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.CompositeNameConverter
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.CompositeNameConverter.DefaultCodegenNameConverter
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctFhirCodegenUtils
import gov.hhs.onc.sdcct.net.SdcctUris
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils
import java.util.regex.Pattern
import javax.annotation.Nullable
import javax.xml.namespace.QName
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.ErrorHandler
import org.xml.sax.Locator

class FhirTermCodegenPlugin extends AbstractTermCodegenPlugin {
    final static String LIST_TOKEN_SUFFIX = "-list"
    
    final static String COMPONENT_CLASS_NAME_SUFFIX = "Component"
    
    private final static String TERM_DATA_FILE_PROJECT_PROP_NAME = "project.build.fhirTermDataFile"
    private final static String V2_TERM_DATA_FILE_PROJECT_PROP_NAME = "project.build.fhirV2TermDataFile"
    private final static String V3_TERM_DATA_FILE_PROJECT_PROP_NAME = "project.build.fhirV3TermDataFile"
    
    private final static Pattern VALUE_SET_URI_PREFIX_PATTERN = ~"^\\Q${SdcctUris.FHIR_URL_VALUE}\\E/(?:ValueSet/)"
    
    private final static String VALUE_SET_COMMENTS_EXT_URL_VALUE = SdcctUris.FHIR_URL_VALUE + "/StructureDefinition/valueset-comments"
    private final static String VALUE_SET_OID_EXT_URL_VALUE = SdcctUris.FHIR_URL_VALUE + "/StructureDefinition/valueset-oid"
    
    private Map<String, ValueSetCodegenModel> normalizedValueSets

    FhirTermCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, SdcctPackages.FHIR_NAME, SdcctPackages.FHIR_IMPL_NAME, codeModel, schemaContext, SpecificationType.FHIR,
            "sdcct-term-fhir")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        super.runInternal(outline, model, codeModel, opts, errorHandler)
        
        CompositeNameConverter nameConv = ((CompositeNameConverter) opts.nameConverter)
        JDefinedClass enumClassModel, enumComponentClassModel, enumComponentImplClassModel
        String normalizedValueSetId, enumComponentClassSimpleName, enumConstName
        ValueSetCodegenModel valueSet
        Map<String, JDocComment> enumConstJavadocModels
        
        this.enumClassSimpleNames.each{
            if (!this.normalizedValueSets.containsKey((normalizedValueSetId = SdcctCodegenUtils.buildNormalizedId(it.value))) ||
                ((enumClassModel = this.enumClassModels[it.value]) == null) || ((enumComponentClassModel = codeModel._getClass((this.basePkgName +
                ClassUtils.PACKAGE_SEPARATOR + (enumComponentClassSimpleName = (it.value + COMPONENT_CLASS_NAME_SUFFIX))))) == null) ||
                ((enumComponentImplClassModel = codeModel._getClass((this.baseImplPkgName + ClassUtils.PACKAGE_SEPARATOR + enumComponentClassSimpleName +
                SdcctClassUtils.IMPL_CLASS_NAME_SUFFIX))) == null) || !(valueSet = this.normalizedValueSets[normalizedValueSetId]).hasConcepts()) {
                return
            }
            
            enumConstJavadocModels = SdcctCodegenUtils.buildEnumConstants(enumClassModel).values()
                .collectEntries{ Collections.singletonMap(it.name, it.javadoc()) }
            
            SdcctCodegenUtils.clearFields(enumClassModel)
            SdcctCodegenUtils.clearEnumConstants(enumClassModel)
            SdcctCodegenUtils.clearMethods(enumClassModel)
            SdcctCodegenUtils.clearConstructors(enumClassModel)
            
            buildStaticValueSetEnumClassModel(codeModel, valueSet, enumClassModel)
            
            valueSet.concepts.each{
                buildStaticValueSetEnumConstantModel(codeModel, enumClassModel, (enumConstName = nameConv.toConstantName(it.key.getKey(1))), it.value,
                    enumConstJavadocModels[enumConstName], buildConceptJavadoc(it.value.element), true)
            }
            
            SdcctCodegenUtils.findField(enumComponentImplClassModel, SdcctCodegenUtils.VALUE_MEMBER_NAME).type(enumClassModel)
            
            enumComponentClassModel._implements(StaticValueSetComponentBean)
            
            [ SdcctCodegenUtils.findGetterMethod(enumComponentClassModel, SdcctCodegenUtils.VALUE_MEMBER_NAME, false),
                SdcctCodegenUtils.findGetterMethod(enumComponentImplClassModel, SdcctCodegenUtils.VALUE_MEMBER_NAME, false) ].each{ it.type(enumClassModel) }
            
            [ SdcctCodegenUtils.findSetterMethod(enumComponentClassModel, SdcctCodegenUtils.VALUE_MEMBER_NAME),
                SdcctCodegenUtils.findSetterMethod(enumComponentImplClassModel, SdcctCodegenUtils.VALUE_MEMBER_NAME) ]
                .each{ it.params()[0].type(enumClassModel) }
        }
        
        this.staticValueSetEnums.each{
            (enumClassModel = buildStaticValueSetEnumClassModel(codeModel, (valueSet = this.valueSets[it.key]), SdcctCodegenUtils.findPackage(codeModel,
                ClassUtils.getPackageName(it.value))._enum(ClassUtils.getShortClassName(it.value))))
            
            valueSet.buildConcepts(this.valueSets, this.codeSystems)
            
            valueSet.concepts.each{
                buildStaticValueSetEnumConstantModel(codeModel, enumClassModel, nameConv.toConstantName(it.key.getKey(1)), it.value, null,
                    buildConceptJavadoc(it.value.element), false)
            }
        }
    }

    @Override
    protected void postProcessModelInternal(Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        super.postProcessModelInternal(model, codeModel, opts, errorHandler)
        
        CompositeNameConverter nameConv = ((CompositeNameConverter) opts.nameConverter)
        Map<QName, TypeUse> typeUses = model.typeUses()
        Package enumParentModel = model.getPackage(this.basePkg)
        String enumClassSimpleName, normalizedValueSetId, enumConceptId
        Locator enumLoc
        QName enumQname
        
        this.schemaContext.simpleTypes.each{
            if (!(it.value instanceof XSRestrictionSimpleType) || !StringUtils.endsWith(it.key, LIST_TOKEN_SUFFIX) ||
                !this.enumClassSimpleNames.containsKey(it.key) || !this.normalizedValueSets.containsKey(
                (normalizedValueSetId = SdcctCodegenUtils.buildNormalizedId(StringUtils.removeEnd(it.key, LIST_TOKEN_SUFFIX))))) {
                return
            }
            
            enumLoc = it.value.locator
            
            new CEnumLeafInfo(model, (enumQname = new QName(SdcctUris.FHIR_URL_VALUE, it.key)), enumParentModel,
                (enumClassSimpleName = this.enumClassSimpleNames[it.key]), typeUses[enumQname].info, this.normalizedValueSets[normalizedValueSetId].concepts
                .collect{ new CEnumConstant(nameConv.toConstantName((enumConceptId = it.key.getKey(1))), buildConceptJavadoc(it.value.element), enumConceptId,
                null, CCustomizations.EMPTY, enumLoc) }, it.value, CCustomizations.EMPTY, enumLoc)
        }
    }

    @Override
    protected void postProcessSchemasInternal(Options opts, XSSchemaSet schemas) throws Exception {
        DefaultCodegenNameConverter nameConv = ((DefaultCodegenNameConverter) ((CompositeNameConverter) opts.nameConverter).delegates
            .find{ (it instanceof DefaultCodegenNameConverter) })
        String enumId, enumClassSimpleName
        
        this.schemaContext.simpleTypes.each{
            if (!(it.value instanceof XSRestrictionSimpleType) || !StringUtils.endsWith(it.key, LIST_TOKEN_SUFFIX)) {
                return
            }
            
            this.enumClassSimpleNames[it.key] = (enumClassSimpleName = nameConv.toClassName((enumId = StringUtils.removeEnd(it.key, LIST_TOKEN_SUFFIX))))
            this.enumComponentClassSimpleNames[enumId] = (enumClassSimpleName + COMPONENT_CLASS_NAME_SUFFIX)
        }
    }

    @Override
    protected void onActivatedInternal(Options opts) throws Exception {
        super.onActivatedInternal(opts)
        
        ((CompositeNameConverter) opts.getNameConverter()).delegates.add(new FhirTermCodegenNameConverter(this.enumClassSimpleNames,
            this.enumComponentClassSimpleNames))
        
        this.normalizedValueSets = new LinkedHashMap<>()
        
        String termElemName, termId, termName, termUri, termOid, termVersion
        boolean codeSystemTerm
        ValueSetCodegenModel valueSet
        CodeSystemCodegenModel codeSystem
        Element valueSetComposeElem
        
        for (Element termElem : SdcctFhirCodegenUtils.buildResourceItemElements(SdcctStringUtils.ASTERISK,
            [ V2_TERM_DATA_FILE_PROJECT_PROP_NAME, V3_TERM_DATA_FILE_PROJECT_PROP_NAME, TERM_DATA_FILE_PROJECT_PROP_NAME ]
            .collect{ new File(this.project.properties.getProperty(it)) })) {
            if (!(codeSystemTerm = ((termElemName = termElem.localName) == SdcctFhirCodegenUtils.CODE_SYSTEM_ELEM_NAME)) &&
                (termElemName != SdcctFhirCodegenUtils.VALUE_SET_ELEM_NAME)) {
                return
            }
            
            termId = SdcctFhirCodegenUtils.buildId(termElem)
            termName = SdcctFhirCodegenUtils.buildName(termElem)
            termUri = SdcctFhirCodegenUtils.buildUri(termElem)
            termOid = buildOid(termElem)
            termVersion = SdcctFhirCodegenUtils.buildVersion(termElem)
            
            if (codeSystemTerm) {
                this.codeSystems[termUri] = codeSystem = new CodeSystemCodegenModel(termElem, termId, termName, termUri, termOid, termVersion)
                
                codeSystem.addConcepts(mapConceptElements(termElem).collect{ new ConceptCodegenModel(codeSystem, it.value, it.key,
                    buildConceptDisplayName(it.value)) } as ConceptCodegenModel[])
            } else {
                this.valueSets[termUri] = valueSet = new ValueSetCodegenModel(termElem, termId, termName, termUri, termOid, termVersion)
                
                if ((valueSetComposeElem = SdcctXmlUtils.findChildElement(termElem, SdcctUris.FHIR_URL_VALUE, SdcctFhirCodegenUtils.COMPOSE_ELEM_NAME))
                    != null) {
                    valueSet.addCodeSystemExcludes(SdcctXmlUtils.findChildElements(valueSetComposeElem, SdcctUris.FHIR_URL_VALUE,
                        SdcctFhirCodegenUtils.EXCLUDE_ELEM_NAME).collect{ new CodeSystemFilterCodegenModel(valueSet, it, buildCodeSystemUri(it), false,
                        mapConceptElements(it).keySet() as String[]) } as CodeSystemFilterCodegenModel[])
                    valueSet.addCodeSystemIncludes(SdcctXmlUtils.findChildElements(valueSetComposeElem, SdcctUris.FHIR_URL_VALUE,
                        SdcctFhirCodegenUtils.INCLUDE_ELEM_NAME).collect{ new CodeSystemFilterCodegenModel(valueSet, it, buildCodeSystemUri(it), true,
                        mapConceptElements(it).keySet() as String[]) } as CodeSystemFilterCodegenModel[])
                }
                
                this.normalizedValueSets[SdcctCodegenUtils.buildNormalizedId(VALUE_SET_URI_PREFIX_PATTERN.matcher(termUri).replaceFirst(StringUtils.EMPTY))] =
                    valueSet
                this.normalizedValueSets[SdcctCodegenUtils.buildNormalizedId(termName)] = valueSet
            }
        }
        
        this.valueSets.values().each{ it.buildConcepts(this.valueSets, this.codeSystems) }
    }
    
    private static String buildConceptJavadoc(Element conceptElem) {
        String conceptComments = buildComments(conceptElem)
        
        return ((conceptComments != null) ? conceptComments : SdcctXmlUtils.findChildElement(conceptElem, SdcctUris.FHIR_URL_VALUE,
            SdcctFhirCodegenUtils.DEFINITION_ELEM_NAME)?.getAttribute(SdcctFhirCodegenUtils.VALUE_NODE_NAME))
    }
    
    private static String buildConceptDisplayName(Element conceptElem) {
        return SdcctXmlUtils.findChildElement(conceptElem, SdcctUris.FHIR_URL_VALUE, SdcctFhirCodegenUtils.DISPLAY_ELEM_NAME)?.getAttribute(
            SdcctFhirCodegenUtils.VALUE_NODE_NAME)
    }
    
    private static Map<String, Element> mapConceptElements(Element elem) {
        return SdcctXmlUtils.findDescendantElements(elem, SdcctUris.FHIR_URL_VALUE, SdcctFhirCodegenUtils.CONCEPT_ELEM_NAME)
            .collectEntries(new LinkedHashMap<String, Element>(), { Collections.singletonMap(SdcctXmlUtils.findChildElement(it, SdcctUris.FHIR_URL_VALUE,
            SdcctFhirCodegenUtils.CODE_ELEM_NAME).getAttribute(SdcctFhirCodegenUtils.VALUE_NODE_NAME), it) })
    }
    
    @Nullable
    private static String buildCodeSystemUri(@Nullable Element codeSystemElem) {
        return SdcctXmlUtils.findChildElement(codeSystemElem, SdcctUris.FHIR_URL_VALUE, SdcctFhirCodegenUtils.SYSTEM_ELEM_NAME)?.getAttribute(
            SdcctFhirCodegenUtils.VALUE_NODE_NAME)
    }
    
    @Nullable
    private static String buildComments(@Nullable Node node) {
        return SdcctFhirCodegenUtils.buildExtensionValue(node, VALUE_SET_COMMENTS_EXT_URL_VALUE, SdcctFhirCodegenUtils.VALUE_STRING_ELEM_NAME, true)
    }
    
    @Nullable
    private static String buildOid(@Nullable Node node) {
        return StringUtils.removeStart(SdcctFhirCodegenUtils.buildExtensionValue(node, VALUE_SET_OID_EXT_URL_VALUE,
            SdcctFhirCodegenUtils.VALUE_URI_ELEM_NAME, true), SdcctUris.OID_URN_PREFIX)
    }
}
