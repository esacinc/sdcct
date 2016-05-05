package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.codemodel.JCodeModel
import com.sun.codemodel.fmt.JPropertyFile
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import javax.xml.bind.JAXBContext
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.eclipse.persistence.jaxb.JAXBContextFactory
import org.xml.sax.ErrorHandler

class RuntimeCodegenPlugin extends AbstractCodegenPlugin {
    RuntimeCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String basePkgName, String baseImplPkgName, JCodeModel codeModel,
        CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-runtime")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        outline.allPackageContexts.each{
            it._package().propertyFiles().each{
                if (it instanceof JPropertyFile) {
                    ((JPropertyFile) it).add(JAXBContext.JAXB_CONTEXT_FACTORY, JAXBContextFactory.name)
                }
            }
        }
    }
}
