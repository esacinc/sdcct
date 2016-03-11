package gov.hhs.onc.sdcct.build.utils

import javax.annotation.Nullable
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.execution.MavenSession
import org.apache.maven.project.MavenProject
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.resources.FileResource
import org.springframework.context.ConfigurableApplicationContext

final class SdcctBuildUtils {
    private SdcctBuildUtils() {
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
