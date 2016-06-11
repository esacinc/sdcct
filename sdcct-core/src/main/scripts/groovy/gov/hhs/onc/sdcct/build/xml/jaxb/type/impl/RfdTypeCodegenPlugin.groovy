package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import com.sun.codemodel.JCodeModel
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.SdcctPackages
import gov.hhs.onc.sdcct.beans.SpecificationType
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.rfd.RfdResourceType
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import org.apache.commons.lang3.ClassUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler

class RfdTypeCodegenPlugin extends AbstractTypeCodegenPlugin {
    RfdTypeCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, SdcctPackages.RFD_NAME, SdcctPackages.RFD_IMPL_NAME, codeModel, schemaContext, SpecificationType.RFD, "sdcct-type-rfd")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        this.types.each{
            buildTypeClassModel(codeModel, it.value, SdcctCodegenUtils.findClass(codeModel, (SdcctPackages.SDC_NAME + ClassUtils.PACKAGE_SEPARATOR +
                it.value.path)), SdcctCodegenUtils.findClass(codeModel, (SdcctPackages.SDC_IMPL_NAME + ClassUtils.PACKAGE_SEPARATOR + it.value.path +
                SdcctClassUtils.IMPL_CLASS_NAME_SUFFIX)), true)
        }
    }
    
    @Override
    protected void onActivatedInternal(Options opts) throws Exception {
        super.onActivatedInternal(opts)
        
        RfdResourceType.values().each{
            this.types[it.id] = new TypeCodegenModel(null, CodegenTypeKind.RESOURCE, it.id)
        }
    }
}
