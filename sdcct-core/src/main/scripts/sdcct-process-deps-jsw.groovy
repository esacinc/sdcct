import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

final String JSW_ARCHIVE_FILE_SESSION_USER_PROP_NAME = "project.build.jswArchiveFile"

if (session.userProperties.containsKey(JSW_ARCHIVE_FILE_SESSION_USER_PROP_NAME)) {
    return
}

String jswOs = "${project.properties.getProperty( "project.build.jswOsName")}-${project.properties.getProperty( "project.build.jswOsArch")}"
session.userProperties.setProperty("project.build.jswOs", jswOs)

Artifact jswArchiveArtifact = new DefaultArtifact("tanukisoft", "wrapper", project.properties.getProperty("project.build.jswVersion"), Artifact.SCOPE_COMPILE,
    "${SdcctFileNameExtensions.TAR}.${SdcctFileNameExtensions.GZ}", jswOs, new DefaultArtifactHandler())

session.userProperties.setProperty(JSW_ARCHIVE_FILE_SESSION_USER_PROP_NAME, SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, jswArchiveArtifact,
    new URL("${project.properties.getProperty("project.build.jswUrlPrefix")}/${jswArchiveArtifact.version}/${jswArchiveArtifact.artifactId}-${jswArchiveArtifact.classifier}-${jswArchiveArtifact.version}.${jswArchiveArtifact.type}"),
    true).path)
