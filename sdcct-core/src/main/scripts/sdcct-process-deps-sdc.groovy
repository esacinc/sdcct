import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

Artifact sdcSchemaPkgArchiveArtifact = new DefaultArtifact("gov.hhs.onc.sdc", "sdc-schema-package",
    project.properties.getProperty("project.build.sdcSchemaPackageVersion"), Artifact.SCOPE_COMPILE, SdcctFileNameExtensions.ZIP, StringUtils.EMPTY,
    new DefaultArtifactHandler());
File sdcSchemaPkgArchiveFile = SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, sdcSchemaPkgArchiveArtifact,
    new URL("${project.properties.getProperty("project.build.sdcSchemaPackageRepositoryArchiveUrlPrefix")}/${sdcSchemaPkgArchiveArtifact.version}.${sdcSchemaPkgArchiveArtifact.type}"),
    true), schemaDir = new File(project.properties.getProperty("project.build.schemaDirectory")),
    wsdlDir = new File(project.properties.getProperty("project.build.wsdlDirectory")),
    sdcTestFormDir = new File(project.properties.getProperty("project.build.sdcTestFormDirectory"))

ant.mkdir(dir: schemaDir)

ant.unzip(src: sdcSchemaPkgArchiveFile, dest: schemaDir) {
    ant.patternset() {
        ant.include(name: "*/schema/**/*.${SdcctFileNameExtensions.XSD}")
    }
    
    ant.cutdirsmapper(dirs: 2)
}

ant.mkdir(dir: wsdlDir)

ant.unzip(src: sdcSchemaPkgArchiveFile, dest: wsdlDir) {
    ant.patternset() {
        ant.include(name: "*/wsdl/**/*.${SdcctFileNameExtensions.WSDL}")
    }
    
    ant.cutdirsmapper(dirs: 2)
}

ant.mkdir(dir: sdcTestFormDir)

ant.unzip(src: sdcSchemaPkgArchiveFile, dest: sdcTestFormDir) {
    ant.patternset() {
        ant.include(name: "*/example/form/*.${SdcctFileNameExtensions.XML}")
    }
    
    ant.cutdirsmapper(dirs: 3)
}
