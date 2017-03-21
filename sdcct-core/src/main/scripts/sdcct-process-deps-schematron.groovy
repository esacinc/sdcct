import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

final String SCHEMATRON_ARCHIVE_STYLE_DIR_PATH = "**/schematron/code"

final String[] SCHEMATRON_STYLE_FILE_NAMES = [
    "iso_abstract_expand.${SdcctFileNameExtensions.XSL}",
    "iso_dsdl_include.${SdcctFileNameExtensions.XSL}",
    "iso_schematron_skeleton_for_saxon.${SdcctFileNameExtensions.XSL}",
    "iso_svrl_for_xslt2.${SdcctFileNameExtensions.XSL}"
]

Artifact schematronArchiveArtifact = new DefaultArtifact("org.iso.schematron", "iso-schematron",
    project.properties.getProperty("project.build.schematronVersion"), Artifact.SCOPE_COMPILE, SdcctFileNameExtensions.ZIP, StringUtils.EMPTY,
    new DefaultArtifactHandler());
File schematronArchiveLocalRepoFile = SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, schematronArchiveArtifact,
    new URL("${project.properties.getProperty("project.build.schematronRepoArchiveUrlPrefix")}/${project.properties.getProperty("project.build.schematronRepoVersion")}.${schematronArchiveArtifact.type}"),
    true), schematronStyleDir = new File("${project.properties.getProperty("project.build.styleDirectory")}/schematron")

ant.mkdir(dir: schematronStyleDir)

ant.unzip(src: schematronArchiveLocalRepoFile, dest: schematronStyleDir) {
    ant.patternset() {
        SCHEMATRON_STYLE_FILE_NAMES.collect{ ant.include(name: "${SCHEMATRON_ARCHIVE_STYLE_DIR_PATH}/${it}") }
    }
    
    ant.flattenmapper()
}
