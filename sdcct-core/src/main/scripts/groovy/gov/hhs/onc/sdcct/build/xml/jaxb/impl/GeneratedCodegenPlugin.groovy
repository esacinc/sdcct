package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.fasterxml.jackson.databind.util.ISO8601Utils
import com.sun.codemodel.JAnnotatable
import com.sun.codemodel.JCodeModel
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import javax.annotation.Generated
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler

class GeneratedCodegenPlugin extends AbstractCodegenPlugin {
    private final static String GENERATOR_NAME = "sdcct-jaxb"
    
    private final static String GENERATION_COMMENTS = ("JAXB RI v" + Options.getBuildID())
    
    GeneratedCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String basePkgName, String baseImplPkgName,
        JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-gen")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        String generationTimeStr = ISO8601Utils.format(new Date())
        
        outline.classes.each{
            annotateGenerated(it.ref, generationTimeStr)
            annotateGenerated(it.implClass, generationTimeStr)
        }
        
        outline.enums.each{
            annotateGenerated(it.clazz, generationTimeStr)
        }
    }
    
    private static void annotateGenerated(JAnnotatable annotatable, String generationTimeStr) {
        annotatable.annotate(Generated).param(SdcctCodegenUtils.VALUE_MEMBER_NAME, GENERATOR_NAME).param(SdcctCodegenUtils.DATE_MEMBER_NAME,
            generationTimeStr).param(SdcctCodegenUtils.COMMENTS_FIELD_NAME, GENERATION_COMMENTS)
    }
}
