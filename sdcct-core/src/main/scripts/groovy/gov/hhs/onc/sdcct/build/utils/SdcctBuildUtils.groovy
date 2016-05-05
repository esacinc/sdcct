package gov.hhs.onc.sdcct.build.utils

import javax.annotation.Nullable
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.repository.ArtifactRepository
import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.logging.Log
import org.apache.maven.project.MavenProject
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.resources.FileResource
import org.springframework.context.ConfigurableApplicationContext

final class SdcctBuildUtils {
    final static Closure<Boolean> FALSE_PREDICATE = { false }
    final static Closure<Boolean> TRUE_PREDICATE = { true }
    
    final static String DEPS_DIR_PROJECT_PROP_NAME = "project.build.dependenciesDirectory"
    
    private SdcctBuildUtils() {
    }
    
    static File resolveRemoteArtifact(Log log, MavenProject project, AntBuilder ant, Artifact artifact, URL artifactUrl) {
        ArtifactRepository localRepo = project.projectBuildingRequest.localRepository
        File artifactLocalRepoFile = new File(localRepo.basedir, "${localRepo.pathOf(artifact)}.${artifact.type}")
        
        if (artifactLocalRepoFile.exists()) {
            log.info(
                "Remote artifact (groupId=${artifact.groupId}, artifactId=${artifact.artifactId}, version=${artifact.version}, scope=${artifact.scope}, type=${artifact.type}) already resolved: ${artifactLocalRepoFile.path}")
            
            return artifactLocalRepoFile
        }
        
        File artifactDir = new File(project.properties.getProperty(DEPS_DIR_PROJECT_PROP_NAME), artifact.artifactId),
            artifactFile = new File(artifactDir, artifactLocalRepoFile.name)
        
        if (!artifactDir.exists()) {
            artifactDir.mkdirs()
        }
        
        ant.get(src: artifactUrl, dest: artifactFile)
        
        log.info(
            "Downloaded remote artifact (groupId=${artifact.groupId}, artifactId=${artifact.artifactId}, version=${artifact.version}, scope=${artifact.scope}, type=${artifact.type}): ${artifactUrl} => ${artifactFile.path}")
        
        ant.exec(executable: "mvn", failonerror: true) {
            ant.arg(value: "-q")
            ant.arg(value: "install:install-file")
            ant.arg(value: "-DgroupId=${artifact.groupId}")
            ant.arg(value: "-DartifactId=${artifact.artifactId}")
            ant.arg(value: "-Dversion=${artifact.version}")
            ant.arg(value: "-Dpackaging=${artifact.type}")
            ant.arg(value: "-Dfile=${artifactFile}")
        }
        
        return artifactFile
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
        def tokens = tokenize(str)
        def tokenMap = new LinkedHashMap<String, String>(tokens.length)
        def tokenParts

        tokens.each{ tokenMap.put((tokenParts = StringUtils.split(it, "=", 2))[0], tokenParts[1]) }

        return tokenMap
    }

    static String[] tokenize(@Nullable String str) {
        return tokenize(str, null)
    }

    static String[] tokenize(@Nullable String str, @Nullable String defaultStr) {
        return ObjectUtils.defaultIfNull(org.springframework.util.StringUtils.tokenizeToStringArray(ObjectUtils.defaultIfNull(str, defaultStr),
            ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS), ArrayUtils.EMPTY_STRING_ARRAY)
    }
}
