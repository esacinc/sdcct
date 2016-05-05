package gov.hhs.onc.sdcct.build.xml.jaxb.utils

import com.sun.codemodel.JAnnotatable
import com.sun.codemodel.JAnnotationUse
import com.sun.codemodel.JAnnotationValue
import com.sun.codemodel.JBlock
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JEnumConstant
import com.sun.codemodel.JExpr
import com.sun.codemodel.JExpression
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JMethod
import com.sun.codemodel.JMod
import com.sun.codemodel.JPackage
import com.sun.codemodel.JVar
import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils
import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.regex.Pattern
import javax.annotation.Nullable
import org.apache.commons.lang3.StringUtils

final class SdcctCodegenUtils {    
    final static int CONST_FIELD_MODS = (JMod.PUBLIC | JMod.FINAL | JMod.STATIC)
    final static int ENUM_FIELD_MODS = (JMod.PRIVATE | JMod.FINAL)
    
    final static String DATE_MEMBER_NAME = "date"
    final static String ID_MEMBER_NAME = "id"
    final static String NAME_MEMBER_NAME = "name"
    final static String VALUE_MEMBER_NAME = "value"
    final static String VALUE_ITEM_MEMBER_NAME = VALUE_MEMBER_NAME + "Item"
    
    final static String COMMENTS_FIELD_NAME = "comments"
    final static String ENUM_CONSTS_BY_NAME_FIELD_NAME = "enumConstantsByName"
    final static String MEMBER_VALUES_FIELD_NAME = "memberValues"
    
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
    
    private final static Field ANNO_MODEL_MEMBER_VALUES_FIELD = JAnnotationUse.declaredFields.find{ (it.name == MEMBER_VALUES_FIELD_NAME) }
    private final static Field CLASS_MODEL_ENUM_CONSTS_BY_NAME_FIELD = JDefinedClass.declaredFields.find{ (it.name == ENUM_CONSTS_BY_NAME_FIELD_NAME) }
    private final static Field CLASS_MODEL_NAME_FIELD = JDefinedClass.declaredFields.find{ (it.name == NAME_MEMBER_NAME) }
    
    private final static Method ANNO_MODEL_ADD_VALUE_METHOD = JAnnotationUse.declaredMethods.find{ (it.name == VALUE_ADDER_METHOD_NAME) }
    
    static {
        [ ANNO_MODEL_MEMBER_VALUES_FIELD, CLASS_MODEL_ENUM_CONSTS_BY_NAME_FIELD, CLASS_MODEL_NAME_FIELD, ANNO_MODEL_ADD_VALUE_METHOD ]
            .each{ (it.accessible = true) }
    }
    
    private SdcctCodegenUtils() {
    }
    
    static String buildNormalizedId(String id) {
        return StringUtils.remove(StringUtils.remove(id, SdcctStringUtils.HYPHEN_CHAR), SdcctStringUtils.SLASH_CHAR).toLowerCase()
    }
    
    static <T extends JAnnotatable, U extends JAnnotatable> U copyAnnotations(@Nullable T srcModel, U destModel) {
        if (srcModel == null) {
            return destModel
        }
        
        JAnnotationUse destAnnoModel
        
        srcModel.annotations().each{
            destAnnoModel = destModel.annotate(it.annotationClass)
            
            ((Map<String, JAnnotationValue>) ANNO_MODEL_MEMBER_VALUES_FIELD.get(it))?.each{
                addAnnotationValue(destAnnoModel, it.key, it.value)
            }
        }
        
        return destModel
    } 
    
    static JAnnotationUse addAnnotationValue(JAnnotationUse annoModel, String name, JAnnotationValue value) {
        ANNO_MODEL_ADD_VALUE_METHOD.invoke(annoModel, name, value)
        
        return annoModel
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
    
    static JDefinedClass clearFields(JDefinedClass classModel) {
        return removeFields(classModel, SdcctBuildUtils.TRUE_PREDICATE)
    }
    
    static JDefinedClass removeFields(JDefinedClass classModel, Closure<Boolean> predicate) {
        classModel.fields().values().findAll(predicate).each{ classModel.removeField(it) }
        
        return classModel
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
    
    static JMethod buildFieldSetterMethod(JDefinedClass classModel, int mods, Class<?> type, String name, boolean overridden, boolean nullable) {
        JMethod setterMethodModel = buildSetterMethod(classModel, mods, type, name, overridden, nullable)
        
        setterMethodModel.body().assign(JExpr._this().ref(name), setterMethodModel.params()[0])
        
        return setterMethodModel
    }
    
    static JMethod buildSetterMethod(JDefinedClass classModel, int mods, Class<?> type, String name, boolean overridden, boolean nullable) {
        JMethod setterMethodModel = classModel.method(mods, type, (SETTER_METHOD_NAME_PREFIX + StringUtils.capitalize(name)))
        JVar setterMethodParamModel = setterMethodModel.param(type, name)
        
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
        JMethod hasMethodModel = classModel.method(mods, classModel.owner().BOOLEAN, (HAS_METHOD_NAME_PREFIX + StringUtils.capitalize(name)))
        
        if (overridden) {
            hasMethodModel.annotate(Override)
        }
        
        return hasMethodModel
    }
    
    static JMethod buildFieldGetterMethod(JDefinedClass classModel, int mods, Class<?> type, String name, boolean overridden, boolean nullable) {
        JMethod getterMethodModel = buildGetterMethod(classModel, mods, type, name, overridden, nullable)
        
        getterMethodModel.body()._return(JExpr._this().ref(name))
        
        return getterMethodModel
    }
    
    static JMethod buildGetterMethod(JDefinedClass classModel, int mods, Class<?> type, String name, boolean overridden, boolean nullable) {
        JMethod getterMethodModel = classModel.method(mods, type, ((SdcctClassUtils.BOOLEAN_CLASSES.contains(type) ? IS_GETTER_METHOD_NAME_PREFIX :
            GETTER_METHOD_NAME_PREFIX) + StringUtils.capitalize(name)))
        
        if (overridden) {
            getterMethodModel.annotate(Override)
        }
        
        if (nullable) {
            getterMethodModel.annotate(Nullable)
        }
        
        return getterMethodModel
    }
    
    static JFieldVar buildEnumField(JDefinedClass classModel, Class<?> type, String name) {
        return classModel.field(ENUM_FIELD_MODS, type, name)
    }
    
    static JFieldVar buildConstPatternField(JDefinedClass classModel, String name, @Nullable String value) {
        return buildConstField(classModel, Pattern, name, ((value != null) ? classModel.owner().ref(Pattern).staticInvoke(COMPILE_METHOD_NAME).arg(value) :
            null))
    }
    
    static JFieldVar buildConstUriField(JDefinedClass classModel, String name, @Nullable String value) {
        return buildConstField(classModel, URI, name, ((value != null) ? classModel.owner().ref(URI).staticInvoke(CREATE_METHOD_NAME).arg(value) : null))
    }
    
    static JFieldVar buildConstField(JDefinedClass classModel, Class<?> type, String name, @Nullable String value) {
        return buildConstField(classModel, type, name, buildLiteralExpression(value))
    }
    
    static JFieldVar buildConstField(JDefinedClass classModel, Class<?> type, String name, @Nullable JExpression value) {
        JFieldVar constFieldModel = classModel.field(CONST_FIELD_MODS, type,
            StringUtils.splitByCharacterTypeCamelCase(name).join(SdcctStringUtils.UNDERSCORE).toUpperCase())
        
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
        name = (HAS_METHOD_NAME_PREFIX + StringUtils.capitalize(name))
        
        return hasMethod(classModel, { ((it.name() == name) && it.params().isEmpty() && SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName())) })
    }
    
    static boolean hasSetterMethod(JDefinedClass classModel, String name) {
        name = (SETTER_METHOD_NAME_PREFIX + StringUtils.capitalize(name))
        
        return hasMethod(classModel, { ((it.name() == name) && (it.params().size() == 1)) })
    }
    
    static boolean hasGetterMethod(JDefinedClass classModel, String name) {
        name = StringUtils.capitalize(name)
        
        return hasMethod(classModel, { (it.name() == (SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName()) ? IS_GETTER_METHOD_NAME_PREFIX :
            GETTER_METHOD_NAME_PREFIX) + name) })
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
    
    static List<JMethod> findGetterMethods(JDefinedClass classModel) {
        return classModel.methods().findAll({ (StringUtils.startsWith(it.name(), (SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName()) ?
            IS_GETTER_METHOD_NAME_PREFIX : GETTER_METHOD_NAME_PREFIX)) && it.params().isEmpty()) })
    }
    
    static List<JMethod> findMethods(JDefinedClass classModel, Closure<Boolean> predicate) {
        return classModel.methods().findAll(predicate)
    }
    
    @Nullable
    static JMethod findHasMethod(JDefinedClass classModel, String name) {
        name = (HAS_METHOD_NAME_PREFIX + StringUtils.capitalize(name))
        
        return findMethod(classModel, { ((it.name() == name) && it.params().isEmpty() && SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName())) })
    }
    
    @Nullable
    static JMethod findSetterMethod(JDefinedClass classModel, String name) {
        name = (SETTER_METHOD_NAME_PREFIX + StringUtils.capitalize(name))
        
        return findMethod(classModel, { ((it.name() == name) && (it.params().size() == 1)) })
    }
    
    @Nullable
    static JMethod findGetterMethod(JDefinedClass classModel, String name) {
        name = StringUtils.capitalize(name)
        
        return findMethod(classModel, { (it.name() == (SdcctClassUtils.BOOLEAN_CLASS_NAMES.contains(it.type().fullName()) ? IS_GETTER_METHOD_NAME_PREFIX :
            GETTER_METHOD_NAME_PREFIX) + name) })
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
}
