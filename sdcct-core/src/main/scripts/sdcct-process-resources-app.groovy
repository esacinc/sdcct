import org.apache.commons.lang3.StringUtils

String rootDirReplacePattern = "\\\${BASEDIR}/../../../", defaultScriptFileReplacePattern = rootDirReplacePattern + "etc/default/\\\${APP_NAME}"

ant.fileset(dir: "${project.properties["project.build.appassemblerDaemonsJswDirectory"]}/bin", erroronmissingdir: false, includes: project.artifactId).each{
    def runDirReplacePattern = rootDirReplacePattern + project.properties["project.build.debRunDataDirectoryPath"]
    
    it.file.write(it.file.text
        .replaceFirst(~/\n\[\s+\-f\s+".BASEDIR"\/bin\/setenv\-\$\{APP_NAME\}\.sh\s+\]/,
            "\n[ -f \"${defaultScriptFileReplacePattern}\" ] && . \"${defaultScriptFileReplacePattern}\"\n\$0")
        .replaceFirst(~/(\n)#(RUN_AS_USER=)(\n)/, { it[1] + it[2] + project.artifactId + it[3] + "RUN_AS_GROUP=" + project.artifactId + it[3] })
        .replaceFirst(~/\nPIDDIR="\.\.\/run"\n/,
            "\$0\n[ -e \"${runDirReplacePattern}\" ] || { mkdir \"${runDirReplacePattern}\" && chown \"${project.artifactId}:${project.artifactId}\" \"${runDirReplacePattern}\"; }\n")
    )
}

ant.fileset(dir: "${project.properties["project.build.appassemblerDaemonsJswDirectory"]}/conf", erroronmissingdir: false,
    includes: "wrapper-${project.artifactId}.conf").each{
    it.file.write(StringUtils.trim(it.file.text).replaceFirst(~/(\n)(wrapper\.logfile=)([^\n]+\n)/, { "${it[1]}#${it[2]}${it[3]}${it[2]}\n" }) +
    "\n\n#include ../conf/wrapper-${project.artifactId}-override.conf\n\n")
}

String jswOs = session.userProperties.getProperty("project.build.jswOs")
File debDataDir = new File(project.properties.getProperty("project.build.debDataDirectory")),
    jswArchiveFile = new File(session.userProperties.getProperty("project.build.jswArchiveFile"))

ant.untar(src: jswArchiveFile, dest: new File(debDataDir, project.properties.getProperty("project.build.debShareBinDataDirectoryPath")),
    compression: "gzip") {
    ant.patternset() {
        ant.include(name: "*/bin/wrapper")
    }
    
    ant.chainedmapper() {
        ant.flattenmapper()
        ant.globmapper(from: "*", to: "*-${jswOs}")
    }
}

ant.untar(src: jswArchiveFile, dest: new File(debDataDir, project.properties.getProperty("project.build.debShareLibDataDirectoryPath")),
    compression: "gzip") {
    ant.patternset() {
        ant.include(name: "*/lib/libwrapper.so")
        ant.include(name: "*/lib/wrapper.jar")
    }
    
    ant.chainedmapper() {
        ant.flattenmapper()
        ant.regexpmapper(from: "([^\\.]+)(\\.[^\\.]+)", to: "\\1-${jswOs}\\2")
    }
}
