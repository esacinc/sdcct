import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, new DefaultArtifact("com.smartbear.soapui", "soapui-project",
    project.properties.getProperty("project.build.soapuiVersion"), Artifact.SCOPE_TEST, "pom", StringUtils.EMPTY, new DefaultArtifactHandler()),
    new URL("${project.properties.getProperty("project.build.soapuiRepoUrlPrefix")}/pom.xml"))
