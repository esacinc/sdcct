package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.codemodel.JCodeModel
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler

class HierarchyCodegenPlugin extends AbstractCodegenPlugin {
    HierarchyCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String basePkgName, String baseImplPkgName,
        JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-hierarchy")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        outline.classes.findAll{ it.implClass.abstract }.each{
            SdcctCodegenUtils.setClassName(it.implClass, (SdcctClassUtils.ABSTRACT_CLASS_NAME_PREFIX + StringUtils.removeEnd(ClassUtils.getShortClassName(
                it.implClass.name()), SdcctClassUtils.IMPL_CLASS_NAME_SUFFIX)))
        }
    }
}
