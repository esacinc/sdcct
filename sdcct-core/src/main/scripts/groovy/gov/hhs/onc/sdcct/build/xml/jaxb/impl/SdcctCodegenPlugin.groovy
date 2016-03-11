package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject

class SdcctCodegenPlugin extends AbstractCodegenPlugin {
    SdcctCodegenPlugin(Log log, MavenProject project, Map<String, String> bindingVars) {
        super(log, project, bindingVars, "sdcct")
    }
}
