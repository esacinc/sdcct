import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

Artifact schematronXsltArchiveArtifact = new DefaultArtifact("org.oclc.purl.dsdl.schematron", "iso-schematron-xslt2",
    project.properties.getProperty("project.build.schematronVersion"), Artifact.SCOPE_COMPILE, SdcctFileNameExtensions.ZIP, StringUtils.EMPTY,
    new DefaultArtifactHandler());
File schematronXsltArchiveFile = SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, schematronXsltArchiveArtifact,
    new URL("${project.properties.getProperty("project.build.schematronUrlPrefix")}/${schematronXsltArchiveArtifact.artifactId}.${schematronXsltArchiveArtifact.type}"),
    true), schematronStyleDir = new File("${project.properties.getProperty("project.build.styleDirectory")}/schematron")

ant.mkdir(dir: schematronStyleDir)

ant.unzip(src: schematronXsltArchiveFile, dest: schematronStyleDir) {
    ant.patternset() {
        ant.include(name: "iso_abstract_expand.${SdcctFileNameExtensions.XSL}")
        ant.include(name: "iso_dsdl_include.${SdcctFileNameExtensions.XSL}")
        ant.include(name: "iso_schematron_skeleton_for_saxon.${SdcctFileNameExtensions.XSL}")
        ant.include(name: "iso_svrl_for_xslt2.${SdcctFileNameExtensions.XSL}")
    }
}
