package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import com.sun.xml.xsom.XSSchemaSet
import gov.hhs.onc.sdcct.SdcctPackages
import gov.hhs.onc.sdcct.beans.SpecificationType
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.CompositeNameConverter
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.CompositeNameConverter.DefaultCodegenNameConverter
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctFhirCodegenUtils
import gov.hhs.onc.sdcct.net.SdcctUris
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils
import javax.annotation.Nullable
import org.apache.commons.lang3.ClassUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.w3c.dom.Element
import org.xml.sax.ErrorHandler

class FhirTypeCodegenPlugin extends AbstractTypeCodegenPlugin {
    private final static String RESOURCE_TYPE_DATA_FILE_PROJECT_PROP_NAME = "project.build.fhirResourceTypeDataFile"
    private final static String TYPE_DATA_FILE_PROJECT_PROP_NAME = "project.build.fhirTypeDataFile"
    
    private final static String PRIMITIVE_TYPE_CLASS_NAME_SUFFIX = "Type"
    
    private final static String RESOURCE_TYPE_CLASS_NAME = "Resource"
    
    private final static String STRUCT_DEF_REGEX_EXT_URL_VALUE = SdcctUris.FHIR_URL_VALUE + "/StructureDefinition/structuredefinition-regex"

    FhirTypeCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, SdcctPackages.FHIR_NAME, SdcctPackages.FHIR_IMPL_NAME, codeModel, schemaContext, SpecificationType.FHIR,
            "sdcct-type-fhir")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        JDefinedClass typeClassModel, typeImplClassModel
        String typeId, typeClassSimpleName
        
        this.types.each{
            typeId = it.value.id
            
            if (((typeClassModel = SdcctCodegenUtils.findClass(codeModel, (this.basePkgName + ClassUtils.PACKAGE_SEPARATOR +
                (typeClassSimpleName = this.typeClassSimpleNames[typeId])))) == null) || ((typeImplClassModel = SdcctCodegenUtils.findClass(codeModel,
                (this.baseImplPkgName + ClassUtils.PACKAGE_SEPARATOR + typeClassSimpleName + SdcctClassUtils.IMPL_CLASS_NAME_SUFFIX))) == null)) {
                return
            }
            
            it.value.classModel = typeClassModel
            it.value.implClassModel = typeImplClassModel
            
            buildTypeClassModel(codeModel, it.value, typeClassModel, typeImplClassModel, ((it.value.kind != CodegenTypeKind.RESOURCE) ||
                (typeClassModel.name() == RESOURCE_TYPE_CLASS_NAME)))
        }
    }

    @Override
    protected void postProcessSchemasInternal(Options opts, XSSchemaSet schemas) throws Exception {
        DefaultCodegenNameConverter nameConv = ((DefaultCodegenNameConverter) ((CompositeNameConverter) opts.nameConverter).delegates
            .find{ (it instanceof DefaultCodegenNameConverter) })
        String normalizedTypeId, typeClassSimpleName
        TypeCodegenModel type
        
        this.schemaContext.complexTypes.each{
            if (this.normalizedTypes.containsKey((normalizedTypeId = SdcctCodegenUtils.buildNormalizedId(it.key))) &&
                ((type = this.normalizedTypes[normalizedTypeId]) != null)) {
                typeClassSimpleName = nameConv.toClassName(it.key)
                
                if (type.kind == CodegenTypeKind.PRIMITIVE) {
                    typeClassSimpleName += PRIMITIVE_TYPE_CLASS_NAME_SUFFIX
                }
                
                this.typeClassSimpleNames[it.key] = typeClassSimpleName
            }
        }
    }

    @Override
    protected void onActivatedInternal(Options opts) throws Exception {
        super.onActivatedInternal(opts)
        
        CompositeNameConverter nameConv = ((CompositeNameConverter) opts.nameConverter)
        nameConv.delegates.add(new FhirTypeCodegenNameConverter(this.typeClassSimpleNames))
        
        CodegenTypeKind typeKind
        String typeUri, typeId
        boolean primitiveType
        TypeCodegenModel type
        
        for (Element typeElem : SdcctFhirCodegenUtils.buildResourceItemElements(SdcctFhirCodegenUtils.STRUCTURE_DEFINITION_ELEM_NAME,
            [ TYPE_DATA_FILE_PROJECT_PROP_NAME, RESOURCE_TYPE_DATA_FILE_PROJECT_PROP_NAME ].collect{ new File(this.project.properties.getProperty(it)) })) {
            if ((typeKind = SdcctEnumUtils.findById(CodegenTypeKind, buildKind(typeElem))) == null) {
                continue
            }
            
            typeUri = SdcctFhirCodegenUtils.buildUri(typeElem)
            
            this.types[typeUri] = type = new TypeCodegenModel(typeElem, (typeId = SdcctFhirCodegenUtils.buildId(typeElem)),
                SdcctFhirCodegenUtils.buildName(typeElem), typeUri, SdcctFhirCodegenUtils.buildFhirVersion(typeElem),
                ((primitiveType = ((typeKind == CodegenTypeKind.DATATYPE) && Character.isLowerCase( typeId.toCharArray()[0]))) ?
                (typeKind = CodegenTypeKind.PRIMITIVE) : typeKind), (primitiveType ? buildPattern(typeElem) : null))
            
            this.normalizedTypes[SdcctCodegenUtils.buildNormalizedId(typeId)] = type
        }
    }
    
    @Nullable
    protected static String buildPattern(Element elem) {
        return SdcctFhirCodegenUtils.buildExtensionValue(SdcctXmlUtils.findChildElement(elem, SdcctUris.FHIR_URL_VALUE,
            SdcctFhirCodegenUtils.SNAPSHOT_ELEM_NAME), STRUCT_DEF_REGEX_EXT_URL_VALUE, SdcctFhirCodegenUtils.VALUE_STRING_ELEM_NAME, false)
    }
    
    protected static String buildKind(Element elem) {
        return SdcctXmlUtils.findChildElement(elem, SdcctUris.FHIR_URL_VALUE, SdcctFhirCodegenUtils.KIND_ELEM_NAME)?.
            getAttribute(SdcctFhirCodegenUtils.VALUE_NODE_NAME)
    }
}
