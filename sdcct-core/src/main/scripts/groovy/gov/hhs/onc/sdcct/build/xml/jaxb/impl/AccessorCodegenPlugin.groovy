package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.github.sebhoss.warnings.CompilerWarnings
import com.sun.codemodel.JBlock
import com.sun.codemodel.JClass
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JExpr
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JMethod
import com.sun.codemodel.JMod
import com.sun.codemodel.JType
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.CClassInfo
import com.sun.tools.xjc.model.CPropertyInfo
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.ClassOutline
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.config.utils.SdcctPropertiesUtils.LinkedProperties
import java.nio.charset.StandardCharsets
import javax.xml.bind.JAXBElement
import org.apache.commons.collections4.keyvalue.MultiKey
import org.apache.commons.collections4.set.ListOrderedSet
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.xml.sax.ErrorHandler

class AccessorCodegenPlugin extends AbstractCodegenPlugin {
    private class PrivatePropertyNameComparator implements Comparator<String> {
        private ListOrderedSet<Object> orderedPluralPropNames

        PrivatePropertyNameComparator() {
            this.orderedPluralPropNames = AccessorCodegenPlugin.this.pluralProps.getOrderedKeys()
        }

        @Override
        int compare(String privatePropName1, String privatePropName2) {
            boolean pluralizedProp1 = this.orderedPluralPropNames.contains(privatePropName1),
                pluralizedProp2 = this.orderedPluralPropNames.contains(privatePropName2)
            Integer pluralPropIndex1 = (pluralizedProp1 ? this.orderedPluralPropNames.indexOf(privatePropName1) : null),
                pluralPropIndex2 = (pluralizedProp2 ? this.orderedPluralPropNames.indexOf(privatePropName2) : null)
            
            if (pluralizedProp1) {
                return (pluralizedProp2 ? Integer.compare(pluralPropIndex1, pluralPropIndex2) : -1)
            } else if (pluralizedProp2) {
                return 1
            } else {
                return privatePropName1.compareTo(privatePropName2)
            }
        }
    }
    
    protected final static String PLURAL_PROPS_FILE_PROJECT_PROP_NAME = "project.build.jaxbPluralsPropertyFile"
    
    private LinkedProperties pluralProps
    private PrivatePropertyNameComparator propNameComparator
    private Map<MultiKey<String>, JMethod> propSetterMethodModels
    private Map<MultiKey<String>, Boolean> jaxbElemProps
    private Map<MultiKey<String>, JMethod> propAdderMethodModels
    
    AccessorCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String basePkgName, String baseImplPkgName,
        JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-accessor")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        this.propSetterMethodModels = new LinkedHashMap<>()
        this.jaxbElemProps = new LinkedHashMap<>()
        this.propAdderMethodModels = new LinkedHashMap<>()
        
        outline.classes.each{
            this.buildAccessors(it.target, it.ref, it.ref.fullName(), it.implClass, SdcctCodegenUtils.buildUserImplClassName(it.target))
        }
        
        outline.classes.each{
            this.buildHierarchicalAccessors(outline, it.ref, it.ref.fullName(), it.implClass, SdcctCodegenUtils.buildUserImplClassName(it.target))
        }
    }
    
    @Override
    protected void postProcessModelInternal(Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        super.postProcessModelInternal(model, codeModel, opts, errorHandler)
        
        PropertiesLoaderUtils.fillProperties((this.pluralProps = new LinkedProperties()), new EncodedResource(new FileSystemResource(new File(
            this.project.properties.getProperty(PLURAL_PROPS_FILE_PROJECT_PROP_NAME))), StandardCharsets.UTF_8))
        
        this.propNameComparator = new PrivatePropertyNameComparator()
    }
    
    private void buildHierarchicalAccessors(Outline outline, JDefinedClass classModel, String className, JDefinedClass implClassModel, String implClassName) {
        JClass superImplClassModel = implClassModel
        String superImplClassName, superClassName, pluralPrivatePropName, pluralPublicPropName
        ClassOutline superClassOutline
        boolean collProp, jaxbElemProp
        MultiKey<String> propAccessorMethodKey
        JMethod superImplPropAccessorMethodModel, propAccessorMethodModel
        JType propTypeModel, propItemTypeModel
        JBlock propAccessorMethodModelBody
        
        while ((superImplClassName = (superImplClassModel = superImplClassModel._extends()).fullName()) != Object.name) {
            if ((superClassName = (superClassOutline = outline.classes.find{ (it.implClass.fullName() == superImplClassName) })?.ref?.fullName()) == null) {
                continue
            }
            
            superClassOutline.target.properties.collectEntries(new TreeMap<String, CPropertyInfo>(this.propNameComparator),
                { Collections.singletonMap(it.getName(false), it) }).each{
                pluralPrivatePropName = this.buildPluralPropertyName((collProp = it.value.collection), it.key, false)
                pluralPublicPropName = this.buildPluralPropertyName(collProp, it.value.getName(true), true)
                jaxbElemProp = this.jaxbElemProps[(propAccessorMethodKey = new MultiKey<>(superClassName, it.key))]
                
                if (this.propSetterMethodModels.containsKey(propAccessorMethodKey)) {
                    superImplPropAccessorMethodModel = this.propSetterMethodModels[propAccessorMethodKey]
                    
                    if (this.propSetterMethodModels.containsKey((propAccessorMethodKey = new MultiKey<>(className, it.key)))) {
                        if ((propAccessorMethodModel = this.propSetterMethodModels[propAccessorMethodKey]).type().fullName() != className) {
                            propAccessorMethodModel.type(classModel)
                            
                            this.propSetterMethodModels[new MultiKey<>(implClassName, it.key)].type(classModel)
                        }
                    } else {
                        propAccessorMethodModel = SdcctCodegenUtils.buildSetterMethod(classModel, JMod.NONE, classModel,
                            (propTypeModel = superImplPropAccessorMethodModel.params()[0].type()), pluralPrivatePropName, true, false)
                        
                        (propAccessorMethodModelBody = (propAccessorMethodModel = SdcctCodegenUtils.buildSetterMethod(implClassModel, JMod.PUBLIC, classModel,
                            propTypeModel, pluralPrivatePropName, true, false)).body())._return(JExpr.cast(classModel, JExpr._super()
                            .invoke(superImplPropAccessorMethodModel).arg(JExpr.direct(pluralPrivatePropName))))
                    }
                }
                
                if (!collProp || !this.propAdderMethodModels.containsKey((propAccessorMethodKey = new MultiKey<>(superClassName, it.key)))) {
                    return
                }
                
                superImplPropAccessorMethodModel = this.propAdderMethodModels[propAccessorMethodKey]
                
                if (this.propAdderMethodModels.containsKey((propAccessorMethodKey = new MultiKey<>(className, it.key)))) {
                    if ((propAccessorMethodModel = this.propAdderMethodModels[propAccessorMethodKey]).type().fullName() != className) {
                        propAccessorMethodModel.type(classModel)
                        
                        if (jaxbElemProp) {
                            SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, propAccessorMethodModel, CompilerWarnings.UNCHECKED)
                        }
                        
                        (propAccessorMethodModel = this.propAdderMethodModels[new MultiKey<>(implClassName, it.key)]).type(classModel)
                        
                        if (jaxbElemProp) {
                            SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, propAccessorMethodModel, CompilerWarnings.UNCHECKED)
                        }
                    }
                } else {
                    propAccessorMethodModel = SdcctCodegenUtils.buildAdderMethod(classModel, JMod.NONE, classModel,
                        (propItemTypeModel = superImplPropAccessorMethodModel.listVarParam().type().elementType()), pluralPrivatePropName, true)
                    
                    if (jaxbElemProp) {
                        SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, propAccessorMethodModel, CompilerWarnings.UNCHECKED)
                    }
                    
                    (propAccessorMethodModelBody = (propAccessorMethodModel = SdcctCodegenUtils.buildAdderMethod(implClassModel, JMod.PUBLIC, classModel,
                        propItemTypeModel, pluralPrivatePropName, true)).body())._return(JExpr.cast(classModel, JExpr._super()
                        .invoke(superImplPropAccessorMethodModel).arg(JExpr.direct(pluralPrivatePropName))))
                    
                    if (jaxbElemProp) {
                        SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, propAccessorMethodModel, CompilerWarnings.UNCHECKED)
                    }
                }
            }
        }
    }
    
    private void buildAccessors(CClassInfo classInfoModel, JDefinedClass classModel, String className, JDefinedClass implClassModel,
        String implClassName) {
        boolean collProp, jaxbElemProp, pluralProp
        JFieldVar propFieldModel
        JMethod propAccessorMethodModel, implPropAccessorMethodModel
        JType propTypeModel, propItemTypeModel
        String publicPropName, pluralPrivatePropName, pluralPublicPropName, propAccessorName
        
        classInfoModel.properties.collectEntries(new TreeMap<String, CPropertyInfo>(((Comparator<String>) this.propNameComparator)),
            { Collections.singletonMap(it.getName(false), it) }).each{
            if (((propFieldModel = SdcctCodegenUtils.findField(implClassModel, it.key)) == null) ||
                ((propAccessorMethodModel = SdcctCodegenUtils.findGetterMethod(classModel, (publicPropName = it.value.getName(true)), true)) == null) ||
                ((implPropAccessorMethodModel = SdcctCodegenUtils.findGetterMethod(implClassModel, publicPropName, true)) == null)) {
                return
            }
            
            pluralPrivatePropName = this.buildPluralPropertyName((collProp = it.value.collection), it.key, false)
            pluralPublicPropName = this.buildPluralPropertyName(collProp, publicPropName, true)
            propTypeModel = propAccessorMethodModel.type()
            
            this.jaxbElemProps[new MultiKey<>(className, it.key)] = (jaxbElemProp = StringUtils.startsWith((collProp ?
                (propItemTypeModel = ((JClass) propTypeModel).getTypeParameters()[0]) : propTypeModel).fullName(), JAXBElement.name))
            this.jaxbElemProps[new MultiKey<>(implClassName, it.key)] = jaxbElemProp
            
            if (StringUtils.startsWith(propAccessorMethodModel.name(), SdcctCodegenUtils.IS_GETTER_METHOD_NAME_PREFIX)) {
                propAccessorMethodModel.name((propAccessorName = (SdcctCodegenUtils.GETTER_METHOD_NAME_PREFIX + publicPropName)))
                implPropAccessorMethodModel.name(propAccessorName)
            } else if ((pluralProp = (pluralPrivatePropName != it.key))) {
                propAccessorMethodModel.name((propAccessorName = (SdcctCodegenUtils.GETTER_METHOD_NAME_PREFIX + pluralPublicPropName)))
                implPropAccessorMethodModel.name(propAccessorName)
            }
            
            if ((propAccessorMethodModel = SdcctCodegenUtils.findSetterMethod(classModel, it.key)) == null) {
                propAccessorMethodModel = SdcctCodegenUtils.buildSetterMethod(classModel, JMod.NONE, classModel, propTypeModel, pluralPrivatePropName,
                    false, false)
            } else {
                propAccessorMethodModel.type(classModel)
            }
            
            if ((implPropAccessorMethodModel = SdcctCodegenUtils.findSetterMethod(implClassModel, it.key)) == null) {
                implPropAccessorMethodModel = SdcctCodegenUtils.buildFieldSetterMethod(implClassModel, JMod.PUBLIC, classModel, propFieldModel.type(),
                    propTypeModel, it.key, true, false)
            } else {
                implPropAccessorMethodModel.type(classModel)
                implPropAccessorMethodModel.body()._return(JExpr._this())
            }
                
            if (pluralProp) {
                propAccessorMethodModel.name((propAccessorName = (SdcctCodegenUtils.SETTER_METHOD_NAME_PREFIX + pluralPublicPropName)))
                implPropAccessorMethodModel.name(propAccessorName)
            }
            
            if (jaxbElemProp) {
                SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, implPropAccessorMethodModel, CompilerWarnings.UNCHECKED)
            }
            
            this.propSetterMethodModels[new MultiKey<>(className, it.key)] = propAccessorMethodModel
            this.propSetterMethodModels[new MultiKey<>(implClassName, it.key)] = implPropAccessorMethodModel
            
            if (!propTypeModel.primitive) {
                SdcctCodegenUtils.buildHasMethod(classModel, JMod.NONE, it.key, false).name((propAccessorName = (SdcctCodegenUtils.HAS_METHOD_NAME_PREFIX
                    + pluralPublicPropName)))

                (implPropAccessorMethodModel = SdcctCodegenUtils.buildFieldHasMethod(implClassModel, JMod.PUBLIC, it.key, true)).name(propAccessorName)
            }
            
            if (!collProp) {
                return
            }
            
            this.propAdderMethodModels[new MultiKey<>(className, it.key)] = (propAccessorMethodModel = SdcctCodegenUtils.buildAdderMethod(classModel, JMod.NONE,
                classModel, propItemTypeModel, pluralPrivatePropName, false))
            
            this.propAdderMethodModels[new MultiKey<>(implClassName, it.key)] = (implPropAccessorMethodModel = SdcctCodegenUtils.buildFieldAdderMethod(
                implClassModel, JMod.PUBLIC, classModel, propItemTypeModel, pluralPrivatePropName, false, true))
            
            if (jaxbElemProp) {
                SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, propAccessorMethodModel, CompilerWarnings.UNCHECKED)
                SdcctCodegenUtils.buildSuppressWarningsAnnotation(codeModel, implPropAccessorMethodModel, CompilerWarnings.UNCHECKED)
            }
        }
    }
    
    private String buildPluralPropertyName(boolean collProp, String propName, boolean publicProp) {
        String pluralPropName = ((collProp && (this.pluralProps.containsKey((publicProp ? (propName = StringUtils.uncapitalize(propName)) : propName)))) ?
            this.pluralProps.getProperty(propName) : propName)
        
        return (publicProp ? StringUtils.capitalize(pluralPropName) : pluralPropName)
    }
}
