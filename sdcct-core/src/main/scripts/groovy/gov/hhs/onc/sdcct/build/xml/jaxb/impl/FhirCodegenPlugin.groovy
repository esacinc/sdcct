package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.tools.xjc.Options
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject

class FhirCodegenPlugin extends AbstractCodegenPlugin {
    FhirCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars) {
        super(log, project, bindingVars, "sdcct-fhir")
    }
    
    @Override
    protected void onActivatedInternal(Options opts) throws Exception {
        super.onActivatedInternal(opts)
        
        ((CodegenNameConverterProxy) opts.getNameConverter()).setDelegate(new FhirCodegenNameConverter())
    }
}
