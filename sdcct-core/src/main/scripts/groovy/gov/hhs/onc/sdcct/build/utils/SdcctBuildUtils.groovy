package gov.hhs.onc.sdcct.build.utils

import gov.hhs.onc.sdcct.utils.SdcctStringUtils
import javax.annotation.Nullable
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.repository.ArtifactRepository
import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.resources.FileResource

final class SdcctBuildUtils {
    final static Closure<Boolean> FALSE_PREDICATE = { false }
    final static Closure<Boolean> TRUE_PREDICATE = { true }
    
    final static String SNAPSHOT_DEP_VERSION_SUFFIX = "-SNAPSHOT"
    
    final static String DEPS_DIR_PROJECT_PROP_NAME = "project.build.dependenciesDirectory"
    
    private SdcctBuildUtils() {
    }
    
    static File resolveRemoteArtifact(Log log, MavenProject project, AntBuilder ant, Artifact artifact, URL artifactUrl, boolean install) {
        String artifactDesc = "(groupId=${artifact.groupId}, artifactId=${artifact.artifactId}, version=${artifact.version}, classifier=${artifact.classifier}, scope=${artifact.scope}, type=${artifact.type})"
        File artifactLocalRepoFile = buildArtifactLocalRepositoryFile(project, artifact)
        
        if (artifactLocalRepoFile.exists()) {
            if (!StringUtils.endsWith(artifact.version, SNAPSHOT_DEP_VERSION_SUFFIX)) {
                log.info("Remote artifact ${artifactDesc} already resolved: ${artifactLocalRepoFile.path}")

                return artifactLocalRepoFile
            } else {
                log.info("Updating remote artifact ${artifactDesc} with snapshot version.")
            }
        }
        
        File artifactDepsFile = buildDependenciesArtifactFile(project, artifact)
        
        ant.get(src: artifactUrl, dest: artifactDepsFile)
        
        log.info("Downloaded remote artifact ${artifactDesc}: ${artifactUrl} => ${artifactDepsFile.path}")
        
        return (install ? installArtifact(ant, artifact, artifactDepsFile) : artifactDepsFile)
    }
    
    static File installArtifact(AntBuilder ant, Artifact artifact, File artifactFile) {
        ant.exec(executable: "mvn", failonerror: true) {
            ant.arg(value: "-q")
            ant.arg(value: "install:install-file")
            ant.arg(value: "-DgroupId=${artifact.groupId}")
            ant.arg(value: "-DartifactId=${artifact.artifactId}")
            ant.arg(value: "-Dversion=${artifact.version}")
            ant.arg(value: "-Dclassifier=${artifact.classifier}")
            ant.arg(value: "-Dpackaging=${artifact.type}")
            ant.arg(value: "-Dfile=${artifactFile}")
        }
        
        return artifactFile
    }
    
    static File buildDependenciesArtifactFile(MavenProject project, Artifact artifact) {
        return new File(buildDependenciesArtifactDirectory(project, artifact.artifactId),
            "${artifact.artifactId}-${(artifact.hasClassifier() ? "${artifact.classifier}-" : StringUtils.EMPTY)}${artifact.version}.${artifact.type}")
    }
    
    static File buildDependenciesArtifactDirectory(MavenProject project, String artifactDepsDirName) {
        File artifactDepsDir = new File(project.properties.getProperty(DEPS_DIR_PROJECT_PROP_NAME), artifactDepsDirName)
        
        if (!artifactDepsDir.exists()) {
            artifactDepsDir.mkdirs()
        }
        
        return artifactDepsDir
    }
    
    static File buildArtifactLocalRepositoryFile(MavenProject project, Artifact artifact) {
        ArtifactRepository localRepo = project.projectBuildingRequest.localRepository
        
        return new File(localRepo.basedir, "${localRepo.pathOf(artifact)}.${artifact.type}")
    }
    
    static String getExecutionProperty(MavenProject project, MavenSession session, String propName) {
        return getExecutionProperty(project, session, propName, null)
    }

    static String getExecutionProperty(MavenProject project, MavenSession session, String propName, String defaultPropValue) {
        return (project.properties.containsKey(propName) ? project.properties[propName] :
            (session.userProperties.containsKey(propName) ? session.userProperties[propName] :
                (session.systemProperties.containsKey(propName) ? session.systemProperties[propName] : defaultPropValue)))
    }

    static boolean containsExecutionProperty(MavenProject project, MavenSession session, String propName) {
        return (project.properties.containsKey(propName) || session.userProperties.containsKey(propName) || session.systemProperties.containsKey(propName))
    }
    
    static List<File> extractFiles(FileSet fileSet) {
        return fileSet.collect{ ((FileResource) it).file }
    }

    static Map<String, String> tokenizeMap(@Nullable String str) {
        String[] tokens = tokenize(str), tokenParts
        Map<String, String> tokenMap = new LinkedHashMap<>(tokens.length)

        tokens.each{ tokenMap.put((tokenParts = StringUtils.split(it, SdcctStringUtils.EQUALS, 2))[0], tokenParts[1]) }

        return tokenMap
    }

    static String[] tokenize(@Nullable String str) {
        return tokenize(str, null)
    }

    static String[] tokenize(@Nullable String str, @Nullable String defaultStr) {
        return SdcctStringUtils.splitTokens(ObjectUtils.defaultIfNull(str, defaultStr))
    }
}
