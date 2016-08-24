import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

String jswOs = "${project.properties.getProperty( "project.build.jswOsName")}-${project.properties.getProperty( "project.build.jswOsArch")}"
session.userProperties.setProperty("project.build.jswOs", jswOs)

Artifact jswArchiveArtifact = new DefaultArtifact("tanukisoft", "wrapper", project.properties.getProperty("project.build.jswVersion"), Artifact.SCOPE_COMPILE,
    "${SdcctFileNameExtensions.TAR}.${SdcctFileNameExtensions.GZ}", jswOs, new DefaultArtifactHandler())

session.userProperties.setProperty("project.build.jswArchiveFile", SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, jswArchiveArtifact,
    new URL("${project.properties.getProperty("project.build.jswUrlPrefix")}/${jswArchiveArtifact.version}/${jswArchiveArtifact.artifactId}-${jswArchiveArtifact.classifier}-${jswArchiveArtifact.version}.${jswArchiveArtifact.type}"),
    true).path)
