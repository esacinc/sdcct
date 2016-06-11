import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.codehaus.groovy.ant.Groovyc

Map<String, String> bindingVars = ((Map<String, String>) this.binding.variables)
Boolean joint = BooleanUtils.toBooleanObject(bindingVars["joint"])
String javaVersion = project.properties.getProperty("java.version")
File javaSrcDir = new File(project.build.sourceDirectory)
File groovyScriptSrcDir = new File(project.build.scriptSourceDirectory, "groovy")
File outDir = new File(project.build.outputDirectory)

if (!joint && !groovyScriptSrcDir.exists()) {
    return
}

ant.taskdef(name: "groovyc", classname: Groovyc.name)

ant.mkdir(dir: outDir)

ant.groovyc(destdir: outDir, encoding: project.properties.getProperty("project.build.sourceEncoding"), includejavaruntime: true, stacktrace: true,
    targetBytecode: javaVersion, verbose: true) {
    ant.src() {
        if (joint) {
            ant.pathelement(path: javaSrcDir)
        }
        
        ant.pathelement(path: groovyScriptSrcDir)
    }
    
    ObjectUtils.defaultIfNull(org.springframework.util.StringUtils.tokenizeToStringArray(ObjectUtils.defaultIfNull(bindingVars["srcIncludes"], null),
        StringUtils.LF), ArrayUtils.EMPTY_STRING_ARRAY).each{
        ant.include(name: it)
    }
    
    ObjectUtils.defaultIfNull(org.springframework.util.StringUtils.tokenizeToStringArray(ObjectUtils.defaultIfNull(bindingVars["srcExcludes"], null),
        StringUtils.LF), ArrayUtils.EMPTY_STRING_ARRAY).each{
        ant.exclude(name: it)
    }
    
    if (joint) {
        ant.javac(debug: true, deprecation: true, source: javaVersion, target: javaVersion) {
            ant.compilerarg(value: "-Fparameters")
            ant.compilerarg(value: "-Fproc:none")
            ant.compilerarg(value: "-FWerror")
            ant.compilerarg(value: "-FXlint:-path")
        }
    }
}
