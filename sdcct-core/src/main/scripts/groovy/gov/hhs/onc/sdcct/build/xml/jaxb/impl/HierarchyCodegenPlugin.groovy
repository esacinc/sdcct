package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.fasterxml.jackson.annotation.JsonTypeName
import com.sun.codemodel.JAnnotationUse
import com.sun.codemodel.JCodeModel
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import javax.xml.bind.annotation.XmlRootElement
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.xml.sax.ErrorHandler

class HierarchyCodegenPlugin extends AbstractCodegenPlugin {
    private final static Set<String> XFER_ANNO_CLASS_NAMES = [ JsonTypeName.name, XmlRootElement.name ] as HashSet<String>
    
    HierarchyCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars, String basePkgName, String baseImplPkgName,
        JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        super(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext, null, "sdcct-hierarchy")
    }

    @Override
    protected void runInternal(Outline outline, Model model, JCodeModel codeModel, Options opts, ErrorHandler errorHandler) throws Exception {
        List<JAnnotationUse> implAnnoModels
        Iterator<JAnnotationUse> annoModelIterator
        JAnnotationUse annoModel
        
        outline.classes.each{
            implAnnoModels = SdcctCodegenUtils.buildAnnotations(it.implClass)
            annoModelIterator = SdcctCodegenUtils.buildAnnotations(it.ref).iterator()
            
            while (annoModelIterator.hasNext()) {
                if (!XFER_ANNO_CLASS_NAMES.contains((annoModel = annoModelIterator.next()).annotationClass.fullName())) {
                    continue
                }
                
                implAnnoModels.add(annoModel)
                
                annoModelIterator.remove()
            }
            
            if (it.implClass.abstract) {
                SdcctCodegenUtils.setClassName(it.implClass, (SdcctClassUtils.ABSTRACT_CLASS_NAME_PREFIX + StringUtils.removeEnd(ClassUtils.getShortClassName(
                    it.implClass.name()), SdcctClassUtils.IMPL_CLASS_NAME_SUFFIX)))
            }
        }
    }
}
