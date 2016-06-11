package gov.hhs.onc.sdcct.build.xml.jaxb.utils

import com.github.sebhoss.warnings.CompilerWarnings
import com.sun.codemodel.JAnnotatable
import com.sun.codemodel.JAnnotationArrayMember
import com.sun.codemodel.JAnnotationUse
import com.sun.codemodel.JAnnotationValue
import com.sun.codemodel.JBlock
import com.sun.codemodel.JClass
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JEnumConstant
import com.sun.codemodel.JExpr
import com.sun.codemodel.JExpression
import com.sun.codemodel.JFieldRef
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JInvocation
import com.sun.codemodel.JMethod
import com.sun.codemodel.JMod
import com.sun.codemodel.JPackage
import com.sun.codemodel.JType
import com.sun.codemodel.JVar
import com.sun.tools.xjc.model.CClassInfo
import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils
import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.lang.reflect.Method
import javax.annotation.Nullable
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils

final class SdcctCodegenUtils {
    final static int CONST_FIELD_MODS = (JMod.PUBLIC | JMod.FINAL | JMod.STATIC)
    final static int ENUM_FIELD_MODS = (JMod.PRIVATE | JMod.FINAL)
    
    final static String DATE_MEMBER_NAME = "date"
    final static String ID_MEMBER_NAME = "id"
    final static String NAME_MEMBER_NAME = "name"
    final static String VALUE_MEMBER_NAME = "value"
    final static String VALUE_ITEM_MEMBER_NAME = VALUE_MEMBER_NAME + "Item"
    
    final static String ANNOS_FIELD_NAME = "annotations"
    final static String ARGS_FIELD_NAME = "args"
    final static String COMMENTS_FIELD_NAME = "comments"
    final static String ENUM_CONSTS_BY_NAME_FIELD_NAME = "enumConstantsByName"
    final static String EXPR_FIELD_NAME = "expr"
    final static String FIELDS_FIELD_NAME = "fields"
    final static String INIT_FIELD_NAME = "init"
    final static String MEMBER_VALUES_FIELD_NAME = "memberValues"
    final static String VALUES_FIELD_NAME = VALUE_MEMBER_NAME + "s"
    final static String VAR_FIELD_NAME = "var"
    
    final static String ADDER_METHOD_NAME_PREFIX = "add"
    final static String GETTER_METHOD_NAME_PREFIX = "get"
    final static String HAS_METHOD_NAME_PREFIX = "has"
    final static String IS_GETTER_METHOD_NAME_PREFIX = "is"
    final static String SETTER_METHOD_NAME_PREFIX = "set"
    
    final static String VALUE_METHOD_NAME_SUFFIX = "Value"
    
    final static String ADD_METHOD_NAME = "add"
    final static String ADD_ALL_METHOD_NAME = ADD_METHOD_NAME + "All"
    final static String COMPILE_METHOD_NAME = "compile"
    final static String CREATE_METHOD_NAME = "create"
    final static String FIND_BY_ID_METHOD_NAME = "findById"
    final static String FROM_VALUE_METHOD_NAME = "from" + VALUE_METHOD_NAME_SUFFIX
    final static String HAS_VALUE_METHOD_NAME = HAS_METHOD_NAME_PREFIX + VALUE_METHOD_NAME_SUFFIX
    final static String VALUE_ADDER_METHOD_NAME = ADDER_METHOD_NAME_PREFIX + VALUE_METHOD_NAME_SUFFIX
    final static String VALUE_GETTER_METHOD_NAME = GETTER_METHOD_NAME_PREFIX + VALUE_METHOD_NAME_SUFFIX
    final static String VALUE_SETTER_METHOD_NAME = SETTER_METHOD_NAME_PREFIX + VALUE_METHOD_NAME_SUFFIX
    
    final static String ELEM_FORM_DEFAULT_ANNO_PARAM_NAME = "elementFormDefault"
    final static String NS_ANNO_PARAM_NAME = "namespace"
    final static String NS_URI_ANNO_PARAM_NAME = NS_ANNO_PARAM_NAME + "URI"
    final static String PREFIX_ANNO_PARAM_NAME = "prefix"
    final static String XMLNS_ANNO_PARAM_NAME = "xmlns"
    
    private final static Field ANNO_MODEL_MEMBER_VALUES_FIELD = JAnnotationUse.declaredFields.find{ (it.name == MEMBER_VALUES_FIELD_NAME) }
    private final static Field ANNO_ARR_MEMBER_MODEL_VALUES_FIELD = JAnnotationArrayMember.declaredFields.find{ (it.name == VALUES_FIELD_NAME) }
    private final static Field CLASS_MODEL_ANNOS_FIELD = JDefinedClass.declaredFields.find{ (it.name == ANNOS_FIELD_NAME) }
    private final static Field CLASS_MODEL_ENUM_CONSTS_BY_NAME_FIELD = JDefinedClass.declaredFields.find{ (it.name == ENUM_CONSTS_BY_NAME_FIELD_NAME) }
    private final static Field CLASS_MODEL_FIELDS_FIELD = JDefinedClass.declaredFields.find{ (it.name == FIELDS_FIELD_NAME) }
    private final static Field CLASS_MODEL_NAME_FIELD = JDefinedClass.declaredFields.find{ (it.name == NAME_MEMBER_NAME) }
    private final static Field FIELD_REF_MODEL_VAR_FIELD = JFieldRef.declaredFields.find{ (it.name == VAR_FIELD_NAME) }
    private final static Field INVOCATION_MODEL_ARGS_FIELD = JInvocation.declaredFields.find{ (it.name == ARGS_FIELD_NAME) }
    private final static Field METHOD_MODEL_ANNOS_FIELD = JMethod.declaredFields.find{ (it.name == ANNOS_FIELD_NAME) }
    private final static Field RETURN_MODEL_EXPR_FIELD = Class.forName("com.sun.codemodel.JReturn").declaredFields.find{ (it.name == EXPR_FIELD_NAME) }
    private final static Field VAR_MODEL_ANNOS_FIELD = JVar.declaredFields.find{ (it.name == ANNOS_FIELD_NAME) }
    private final static Field VAR_MODEL_INIT_FIELD = JVar.declaredFields.find{ (it.name == INIT_FIELD_NAME) }
    
    private final static Method ANNO_MODEL_ADD_VALUE_METHOD = JAnnotationUse.declaredMethods.find{ (it.name == VALUE_ADDER_METHOD_NAME) }
    
    static {
        [
            ANNO_MODEL_MEMBER_VALUES_FIELD, ANNO_ARR_MEMBER_MODEL_VALUES_FIELD, CLASS_MODEL_ANNOS_FIELD, CLASS_MODEL_ENUM_CONSTS_BY_NAME_FIELD,
            CLASS_MODEL_FIELDS_FIELD, CLASS_MODEL_NAME_FIELD, FIELD_REF_MODEL_VAR_FIELD, INVOCATION_MODEL_ARGS_FIELD, METHOD_MODEL_ANNOS_FIELD,
            RETURN_MODEL_EXPR_FIELD, VAR_MODEL_ANNOS_FIELD, VAR_MODEL_INIT_FIELD, ANNO_MODEL_ADD_VALUE_METHOD
        ].each{ (it.accessible = true) }
    }
    
    private SdcctCodegenUtils() {
    }
    
    static String buildNormalizedId(String id) {
        return StringUtils.remove(StringUtils.remove(id, SdcctStringUtils.HYPHEN_CHAR), SdcctStringUtils.SLASH_CHAR).toLowerCase()
    }
    
    static JDefinedClass clearConstructors(JDefinedClass classModel) {
        return removeConstructors(classModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JDefinedClass removeConstructors(JDefinedClass classModel, Closure<Boolean> predicate) {
        Iterator<JMethod> constructorModelIterator = classModel.constructors().iterator()
        
        while (constructorModelIterator.hasNext()) {
            if (predicate(constructorModelIterator.next())) {
                constructorModelIterator.remove()
            }
        }
        
        return classModel
    }
    
    static JDefinedClass clearMethods(JDefinedClass classModel) {
        return removeMethods(classModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JDefinedClass removeMethods(JDefinedClass classModel, Closure<Boolean> predicate) {
        Iterator<JMethod> methodModelIterator = classModel.methods().iterator()
        
        while (methodModelIterator.hasNext()) {
            if (predicate(methodModelIterator.next())) {
                methodModelIterator.remove()
            }
        }
        
        return classModel
    }
    
    static JDefinedClass clearEnumConstants(JDefinedClass classModel) {
        return removeEnumConstants(classModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JDefinedClass removeEnumConstants(JDefinedClass classModel, Closure<Boolean> predicate) {
        Iterator<JEnumConstant> enumConstModelIterator = buildEnumConstants(classModel).values().iterator()
        
        while (enumConstModelIterator.hasNext()) {
            if (predicate(enumConstModelIterator.next())) {
                enumConstModelIterator.remove()
            }
        }
        
        return classModel
    }
    
    static Map<String, JEnumConstant> buildEnumConstants(JDefinedClass classModel) {
        return ((Map<String, JEnumConstant>) CLASS_MODEL_ENUM_CONSTS_BY_NAME_FIELD.get(classModel))
    }
    
    @Nullable
    static JVar buildFieldReferenceVariable(JFieldRef fieldRefModel) {
        return ((JVar) FIELD_REF_MODEL_VAR_FIELD.get(fieldRefModel))
    }
    
    @Nullable
    static JExpression buildVariableInitializationExpression(JVar varModel) {
        return ((JExpression) VAR_MODEL_INIT_FIELD.get(varModel))
    }
    
    static List<JExpression> buildInvocationArgs(JInvocation invocationModel) {
        return ((List<JExpression>) INVOCATION_MODEL_ARGS_FIELD.get(invocationModel))
    }
    
    static JExpression buildReturnExpression(Object returnModel) {
        return ((JExpression) RETURN_MODEL_EXPR_FIELD.get(returnModel))
    }
    
    static JDefinedClass clearFields(JDefinedClass classModel) {
        return removeFields(classModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JDefinedClass removeFields(JDefinedClass classModel, Closure<Boolean> predicate) {
        classModel.fields().values().findAll(predicate).each{ classModel.removeField(it) }
        
        return classModel
    }
    
    static Map<String, JFieldVar> buildFields(JDefinedClass classModel) {
        return ((Map<String, JFieldVar>) CLASS_MODEL_FIELDS_FIELD.get(classModel))
    }
    
    static JMethod buildEnumValueMethod(JDefinedClass classModel, String fieldName) {
        JMethod enumValueMethodModel = classModel.method(JMod.PUBLIC, String, VALUE_MEMBER_NAME)
        enumValueMethodModel.annotate(Override)
        enumValueMethodModel.body()._return(JExpr._this().ref(fieldName))
        
        return enumValueMethodModel
    }
    
    static JMethod buildEnumFromValueMethod(JDefinedClass classModel) {
        JCodeModel codeModel = classModel.owner()
        JMethod enumFromValueMethodModel = classModel.method((JMod.PUBLIC | JMod.STATIC), classModel, FROM_VALUE_METHOD_NAME)
        JVar enumFromValueParamModel = enumFromValueMethodModel.param(String, VALUE_MEMBER_NAME), enumFromValueItemModel
        
        JBlock enumFromValueMethodModelBody = enumFromValueMethodModel.body()
        (enumFromValueItemModel = enumFromValueMethodModelBody.decl(classModel, VALUE_ITEM_MEMBER_NAME))
            .init(codeModel.ref(SdcctEnumUtils).staticInvoke(FIND_BY_ID_METHOD_NAME).arg(JExpr.dotclass(classModel)).arg(enumFromValueParamModel))
        enumFromValueMethodModelBody._if(enumFromValueItemModel.eq(JExpr._null()))._then()
            ._throw(JExpr._new(codeModel.ref(IllegalArgumentException)).arg(enumFromValueParamModel))
        enumFromValueMethodModelBody._return(enumFromValueItemModel)
        
        return enumFromValueMethodModel
    }
    
    static JMethod buildFieldAdderMethod(JDefinedClass classModel, int mods, JType type, JType valueItemType, String name, boolean useIs, boolean overridden) {
        JMethod adderMethodModel = buildAdderMethod(classModel, mods, type, valueItemType, name, overridden)
        
        JBlock adderMethodModelBody = adderMethodModel.body()
        adderMethodModelBody.staticInvoke(classModel.owner().directClass(CollectionUtils.name), ADD_ALL_METHOD_NAME)
            .arg(JExpr._this().invoke(findGetterMethod(classModel, name, useIs))).arg(JExpr.direct(name))
        adderMethodModelBody._return(JExpr._this())
        
        return adderMethodModel
    }
    
    static JMethod buildAdderMethod(JDefinedClass classModel, int mods, JType type, JType valueItemType, String name, boolean overridden) {
        JMethod adderMethodModel = classModel.method(mods, type, (ADDER_METHOD_NAME_PREFIX + StringUtils.capitalize(StringUtils.removeStart(name,
            SdcctStringUtils.UNDERSCORE))))
        adderMethodModel.varParam(valueItemType, name)
        
        if (overridden) {
            adderMethodModel.annotate(Override)
        }
        
        return adderMethodModel
    }
    
    static JMethod buildFieldSetterMethod(JDefinedClass classModel, int mods, JType type, JType fieldType, JType valueType, String name, boolean overridden,
        boolean nullable) {
        JMethod setterMethodModel = buildSetterMethod(classModel, mods, type, valueType, name, overridden, nullable)
        
        JExpression fieldAssignModel = setterMethodModel.params()[0]
        
        if (fieldType != valueType) {
            fieldAssignModel = JExpr.cast(fieldType, fieldAssignModel)
        }
        
        JBlock setterMethodModelBody = setterMethodModel.body()
        setterMethodModelBody.assign(JExpr._this().ref(name), fieldAssignModel)
        setterMethodModelBody._return(JExpr._this())
        
        return setterMethodModel
    }
    
    static JMethod buildSetterMethod(JDefinedClass classModel, int mods, JType type, JType valueType, String name, boolean overridden,
        boolean nullable) {
        JMethod setterMethodModel = classModel.method(mods, type, (SETTER_METHOD_NAME_PREFIX + StringUtils.capitalize(StringUtils.removeStart(name,
            SdcctStringUtils.UNDERSCORE))))
        JVar setterMethodParamModel = setterMethodModel.param(valueType, name)
        
        if (overridden) {
            setterMethodModel.annotate(Override)
        }
        
        if (nullable) {
            setterMethodParamModel.annotate(Nullable)
        }
        
        return setterMethodModel
    }
    
    static JMethod buildFieldHasMethod(JDefinedClass classModel, int mods, String name, boolean overridden) {
        JMethod hasMethodModel = buildHasMethod(classModel, mods, name, overridden)
        
        hasMethodModel.body()._return(JExpr._this().ref(name).ne(JExpr._null()))
        
        return hasMethodModel
    }
    
    static JMethod buildHasMethod(JDefinedClass classModel, int mods, String name, boolean overridden) {
        JMethod hasMethodModel = classModel.method(mods, classModel.owner().BOOLEAN, (HAS_METHOD_NAME_PREFIX + StringUtils.capitalize(StringUtils.removeStart(
            name, SdcctStringUtils.UNDERSCORE))))
        
        if (overridden) {
            hasMethodModel.annotate(Override)
        }
        
        return hasMethodModel
    }
    
    static JMethod buildFieldGetterMethod(JDefinedClass classModel, int mods, JType type, String name, boolean useIs, boolean overridden, boolean nullable) {
        JMethod getterMethodModel = buildGetterMethod(classModel, mods, type, name, useIs, overridden, nullable)
        
        getterMethodModel.body()._return(JExpr._this().ref(name))
        
        return getterMethodModel
    }
    
    static JMethod buildGetterMethod(JDefinedClass classModel, int mods, JType type, String name, boolean useIs, boolean overridden, boolean nullable) {
        JMethod getterMethodModel = classModel.method(mods, type, (((useIs && SdcctClassUtils.BOOLEAN_CLASSES.contains(type)) ? IS_GETTER_METHOD_NAME_PREFIX :
            GETTER_METHOD_NAME_PREFIX) + StringUtils.capitalize(StringUtils.removeStart(name, SdcctStringUtils.UNDERSCORE))))
        
        if (overridden) {
            getterMethodModel.annotate(Override)
        }
        
        if (nullable) {
            getterMethodModel.annotate(Nullable)
        }
        
        return getterMethodModel
    }
    
    static JFieldVar buildEnumField(JDefinedClass classModel, JType type, String name) {
        return classModel.field(ENUM_FIELD_MODS, type, name)
    }
    
    static JFieldVar buildConstUriField(JDefinedClass classModel, String name, @Nullable String value) {
        JCodeModel codeModel = classModel.owner()
        
        return buildConstField(classModel, codeModel.directClass(URI.name), name, ((value != null) ?
            codeModel.ref(URI).staticInvoke(CREATE_METHOD_NAME).arg(value) : null))
    }
    
    static JFieldVar buildConstField(JDefinedClass classModel, JType type, String name, @Nullable String value) {
        return buildConstField(classModel, type, name, buildLiteralExpression(value))
    }
    
    static JFieldVar buildConstField(JDefinedClass classModel, JType type, String name, @Nullable JExpression value) {
        JFieldVar constFieldModel = classModel.field(CONST_FIELD_MODS, type, StringUtils.splitByCharacterTypeCamelCase(StringUtils.removeStart(name,
            SdcctStringUtils.UNDERSCORE)).join(SdcctStringUtils.UNDERSCORE).toUpperCase())
        
        constFieldModel.init(((value != null) ? value : JExpr._null()))
        
        return constFieldModel
    }
    
    static JExpression buildLiteralExpression(@Nullable String value) {
        return ((value != null) ? JExpr.lit(value) : JExpr._null())
    }
    
    static boolean hasField(JDefinedClass classModel, String name) {
        return hasField(classModel, { (it.name() == name) })
    }
    
    static boolean hasField(JDefinedClass classModel, Closure<Boolean> predicate) {
        return classModel.fields().values().any(predicate)
    }
    
    static List<JFieldVar> findConstFields(JDefinedClass classModel) {
        return findFields(classModel, { ((it.mods() & CONST_FIELD_MODS) != 0) })
    }
    
    static List<JFieldVar> findFields(JDefinedClass classModel, Closure<Boolean> predicate) {
        return classModel.fields().values().findAll(predicate)
    }
    
    @Nullable
    static JFieldVar findConstField(JDefinedClass classModel, String name) {
        return findField(classModel, { ((it.name() == name) && ((it.mods() & CONST_FIELD_MODS) != 0)) })
    }
    
    @Nullable
    static JFieldVar findField(JDefinedClass classModel, String name) {
        return findField(classModel, { (it.name() == name) })
    }
    
    @Nullable
    static JFieldVar findField(JDefinedClass classModel, Closure<Boolean> predicate) {
        return classModel.fields().values().find(predicate)
    }
    
    static boolean hasHasMethod(JDefinedClass classModel, String name) {
        name = (HAS_METHOD_NAME_PREFIX + StringUtils.capitalize(StringUtils.removeStart(name, SdcctStringUtils.UNDERSCORE)))
        
        return hasMethod(classModel, { ((it.name() == name) && it.params().isEmpty() && SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName())) })
    }
    
    static boolean hasSetterMethod(JDefinedClass classModel, String name) {
        name = (SETTER_METHOD_NAME_PREFIX + StringUtils.capitalize(StringUtils.removeStart(name, SdcctStringUtils.UNDERSCORE)))
        
        return hasMethod(classModel, { ((it.name() == name) && (it.params().size() == 1)) })
    }
    
    static boolean hasGetterMethod(JDefinedClass classModel, String name, boolean useIs) {
        name = StringUtils.capitalize(StringUtils.removeStart(name, SdcctStringUtils.UNDERSCORE))
        
        return hasMethod(classModel, { (it.name() == ((useIs && SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName())) ?
            IS_GETTER_METHOD_NAME_PREFIX : GETTER_METHOD_NAME_PREFIX) + name) })
    }
    
    static boolean hasMethod(JDefinedClass classModel, String name) {
        return hasMethod(classModel, { (it.name == name) })
    }
    
    static boolean hasMethod(JDefinedClass classModel, Closure<Boolean> predicate) {
        return classModel.methods().any(predicate)
    }
    
    static List<JMethod> findHasMethods(JDefinedClass classModel) {
        return classModel.methods().findAll({ (StringUtils.startsWith(it.name(), HAS_METHOD_NAME_PREFIX) && it.params().isEmpty() &&
            SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName())) })
    }
    
    static List<JMethod> findSetterMethods(JDefinedClass classModel) {
        return classModel.methods().findAll({ (StringUtils.startsWith(it.name(), SETTER_METHOD_NAME_PREFIX) && (it.params().size() == 1)) })
    }
    
    static List<JMethod> findGetterMethods(JDefinedClass classModel, boolean useIs) {
        return classModel.methods().findAll({ (StringUtils.startsWith(it.name(), ((useIs && SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName()))
            ? IS_GETTER_METHOD_NAME_PREFIX : GETTER_METHOD_NAME_PREFIX)) && it.params().isEmpty()) })
    }
    
    static List<JMethod> findMethods(JDefinedClass classModel, Closure<Boolean> predicate) {
        return classModel.methods().findAll(predicate)
    }
    
    @Nullable
    static JMethod findHasMethod(JDefinedClass classModel, String name) {
        name = (HAS_METHOD_NAME_PREFIX + StringUtils.capitalize(StringUtils.removeStart(name, SdcctStringUtils.UNDERSCORE)))
        
        return findMethod(classModel, { ((it.name() == name) && it.params().isEmpty() && SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName())) })
    }
    
    @Nullable
    static JMethod findSetterMethod(JDefinedClass classModel, String name) {
        name = (SETTER_METHOD_NAME_PREFIX + StringUtils.capitalize(StringUtils.removeStart(name, SdcctStringUtils.UNDERSCORE)))
        
        return findMethod(classModel, { ((it.name() == name) && (it.params().size() == 1)) })
    }
    
    @Nullable
    static JMethod findGetterMethod(JDefinedClass classModel, String name, boolean useIs) {
        name = StringUtils.capitalize(StringUtils.removeStart(name, SdcctStringUtils.UNDERSCORE))
        
        return findMethod(classModel, { (it.name() == ((useIs && SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName())) ?
            IS_GETTER_METHOD_NAME_PREFIX : GETTER_METHOD_NAME_PREFIX) + name) })
    }
    
    @Nullable
    static JMethod findMethod(JDefinedClass classModel, String name) {
        return findMethod(classModel, { (it.name == name) })
    }
    
    @Nullable
    static JMethod findMethod(JDefinedClass classModel, Closure<Boolean> predicate) {
        return classModel.methods().find(predicate)
    }
    
    static JDefinedClass setClassName(JDefinedClass classModel, String name) {
        CLASS_MODEL_NAME_FIELD.set(classModel, name)
        
        return classModel
    }
    
    static boolean hasClass(JCodeModel codeModel, String name) {
        return hasClass(codeModel, { (it.fullName() == name) })
    }
    
    static boolean hasClass(JCodeModel codeModel, Closure<Boolean> predicate) {
        return codeModel.packages().any{ hasClass(it, predicate) }
    }
    
    static boolean hasClass(JPackage pkgModel, String name) {
        return hasClass(pkgModel, { (it.fullName() == name) })
    }
    
    static boolean hasClass(JPackage pkgModel, Closure<Boolean> predicate) {
        return pkgModel.classes().any(predicate)
    }
    
    static List<JDefinedClass> findClasses(JCodeModel codeModel, Closure<Boolean> predicate) {
        return codeModel.packages().collectMany{ findClasses(it, predicate) }
    }
    
    static List<JDefinedClass> findClasses(JPackage pkgModel, Closure<Boolean> predicate) {
        return pkgModel.classes().findAll(predicate)
    }
    
    @Nullable
    static JDefinedClass findClass(JCodeModel codeModel, String name) {
        return findClass(codeModel, { (it.fullName() == name) })
    }
    
    @Nullable
    static JDefinedClass findClass(JCodeModel codeModel, Closure<Boolean> predicate) {
        JDefinedClass classModel = null
        
        for (JPackage pkgModel : codeModel.packages()) {
            if ((classModel = findClass(pkgModel, predicate)) != null) {
                break
            }
        }
        
        return classModel
    }
    
    @Nullable
    static JDefinedClass findClass(JPackage pkgModel, String name) {
        return findClass(pkgModel, { (it.fullName() == name) })
    }
    
    @Nullable
    static JDefinedClass findClass(JPackage pkgModel, Closure<Boolean> predicate) {
        return ((JDefinedClass) pkgModel.classes().find(predicate))
    }
    
    static String buildUserImplClassName(CClassInfo classInfoModel) {
        String implClassName = classInfoModel.userSpecifiedImplClass
        
        return ((implClassName != null) ? implClassName : classInfoModel.fullName())
    }
    
    static boolean hasPackage(JCodeModel codeModel, String name) {
        return hasPackage(codeModel, { (it.name == name) })
    }
    
    static boolean hasPackage(JCodeModel codeModel, Closure<Boolean> predicate) {
        return codeModel.packages().any(predicate)
    }
    
    static List<JPackage> findPackages(JCodeModel codeModel, Closure<Boolean> predicate) {
        return codeModel.packages().findAll(predicate)
    }
    
    @Nullable
    static JPackage findPackage(JCodeModel codeModel, String name) {
        return findPackage(codeModel, { (it.name == name) })
    }
    
    @Nullable
    static JPackage findPackage(JCodeModel codeModel, Closure<Boolean> predicate) {
        return ((JPackage) codeModel.packages().find(predicate))
    }
    
    static <T extends JAnnotatable> T buildSuppressWarningsAnnotation(JCodeModel codeModel, T annotatableModel, String ... warnings) {
        JClass compilerWarningsClassModel = codeModel.directClass(CompilerWarnings.name)
        JAnnotationArrayMember suppressWarningsValueAnnoMemberModel = annotatableModel.annotate(SuppressWarnings).paramArray(VALUE_MEMBER_NAME)
        
        warnings.each{
            suppressWarningsValueAnnoMemberModel.param(compilerWarningsClassModel.staticRef(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                StringUtils.replace(it, SdcctStringUtils.HYPHEN, SdcctStringUtils.UNDERSCORE)), SdcctStringUtils.UNDERSCORE).toUpperCase()))
        }
        
        return annotatableModel
    }
    
    static <T extends JAnnotatable, U extends JAnnotatable> U copyAnnotations(@Nullable T srcModel, U destModel) {
        return copyAnnotations(srcModel, destModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static <T extends JAnnotatable, U extends JAnnotatable> U copyAnnotations(@Nullable T srcModel, U destModel, Closure<Boolean> predicate) {
        if (srcModel == null) {
            return destModel
        }
        
        srcModel.annotations().findAll(predicate).each{
            getAnnotationValues(destModel.annotate(it.annotationClass)).putAll(getAnnotationValues(it))
        }
        
        return destModel
    }
    
    static Map<String, JAnnotationValue> getAnnotationValues(JAnnotationUse annoModel) {
        Map<String, JAnnotationValue> annoValueModels = ((Map<String, JAnnotationValue>) ANNO_MODEL_MEMBER_VALUES_FIELD.get(annoModel))
        
        if (annoValueModels == null) {
            ANNO_MODEL_MEMBER_VALUES_FIELD.set(annoModel, (annoValueModels = new LinkedHashMap<>()))
        }
        
        return annoValueModels
    }
    
    static JMethod clearAnnotations(JMethod methodModel) {
        return removeAnnotations(methodModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JVar clearAnnotations(JVar varModel) {
        return removeAnnotations(varModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JDefinedClass clearAnnotations(JDefinedClass classModel) {
        return removeAnnotations(classModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JMethod removeAnnotations(JMethod methodModel, Closure<Boolean> predicate) {
        Iterator<JAnnotationUse> annoModelIterator = buildAnnotations(methodModel).iterator()
        
        while (annoModelIterator.hasNext()) {
            if (predicate(annoModelIterator.next())) {
                annoModelIterator.remove()
            }
        }
        
        return methodModel
    }
    
    static JVar removeAnnotations(JVar varModel, Closure<Boolean> predicate) {
        Iterator<JAnnotationUse> annoModelIterator = buildAnnotations(varModel).iterator()
        
        while (annoModelIterator.hasNext()) {
            if (predicate(annoModelIterator.next())) {
                annoModelIterator.remove()
            }
        }
        
        return varModel
    }
    
    static JDefinedClass removeAnnotations(JDefinedClass classModel, Closure<Boolean> predicate) {
        Iterator<JAnnotationUse> annoModelIterator = buildAnnotations(classModel).iterator()
        
        while (annoModelIterator.hasNext()) {
            if (predicate(annoModelIterator.next())) {
                annoModelIterator.remove()
            }
        }
        
        return classModel
    }
    
    static List<JAnnotationUse> buildAnnotations(JMethod methodModel) {
        List<JAnnotationUse> annoModels = ((List<JAnnotationUse>) METHOD_MODEL_ANNOS_FIELD.get(methodModel))
        
        if (annoModels == null) {
            METHOD_MODEL_ANNOS_FIELD.set(methodModel, (annoModels = new ArrayList<>()))
        }
        
        return annoModels
    }
    
    static List<JAnnotationUse> buildAnnotations(JVar varModel) {
        List<JAnnotationUse> annoModels = ((List<JAnnotationUse>) VAR_MODEL_ANNOS_FIELD.get(varModel))
        
        if (annoModels == null) {
            VAR_MODEL_ANNOS_FIELD.set(varModel, (annoModels = new ArrayList<>()))
        }
        
        return annoModels
    }
    
    static List<JAnnotationUse> buildAnnotations(JDefinedClass classModel) {
        List<JAnnotationUse> annoModels = ((List<JAnnotationUse>) CLASS_MODEL_ANNOS_FIELD.get(classModel))
        
        if (annoModels == null) {
            CLASS_MODEL_ANNOS_FIELD.set(classModel, (annoModels = new ArrayList<>()))
        }
        
        return annoModels
    }
    
    static boolean hasAnnotation(JAnnotatable annotatableModel, Class<? extends Annotation> annoClass) {
        return hasAnnotation(annotatableModel, { (it.annotationClass.fullName() == annoClass.name) })
    }
    
    static boolean hasAnnotation(JAnnotatable annotatableModel, Closure<Boolean> predicate) {
        return annotatableModel.annotations().any(predicate)
    }
    
    @Nullable
    static JAnnotationUse findAnnotation(JAnnotatable annotatableModel, Class<? extends Annotation> annoClass) {
        return findAnnotation(annotatableModel, { (it.annotationClass.fullName() == annoClass.name) })
    }
    
    @Nullable
    static JAnnotationUse findAnnotation(JAnnotatable annotatableModel, Closure<Boolean> predicate) {
        return annotatableModel.annotations().find(predicate)
    }
}
