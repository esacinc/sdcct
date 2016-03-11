import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ObjectUtils
import org.codehaus.groovy.ant.Groovyc
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.util.StringUtils

def bindingVars = this.binding.variables
def joint = BooleanUtils.toBooleanObject(((String) bindingVars["joint"]))
def String javaVersion = project.properties.getProperty("java.version")
def File javaSrcDir = new File(project.build.sourceDirectory)
def File groovyScriptSrcDir = new File(project.build.scriptSourceDirectory, "groovy")
def File outDir = new File(project.build.outputDirectory)

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
    
    ObjectUtils.defaultIfNull(StringUtils.tokenizeToStringArray(ObjectUtils.defaultIfNull(((String) bindingVars["srcIncludes"]), null),
        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS), ArrayUtils.EMPTY_STRING_ARRAY).each{
        ant.include(name: it)
    }
    
    ObjectUtils.defaultIfNull(StringUtils.tokenizeToStringArray(ObjectUtils.defaultIfNull(((String) bindingVars["srcExcludes"]), null),
        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS), ArrayUtils.EMPTY_STRING_ARRAY).each{
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
