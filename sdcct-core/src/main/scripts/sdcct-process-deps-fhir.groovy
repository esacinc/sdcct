import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

final Artifact FHIR_XML_VALIDATION_ARCHIVE_ARTIFACT = new DefaultArtifact("org.hl7.fhir", "validation-xml",
    project.properties.getProperty("project.build.fhirVersion"), Artifact.SCOPE_COMPILE, "zip", StringUtils.EMPTY, new DefaultArtifactHandler());

File fhirXmlValidationArchiveFile = SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, FHIR_XML_VALIDATION_ARCHIVE_ARTIFACT,
    new URL("${project.properties.getProperty("project.build.fhirSiteUrlPrefix")}/validation.xml.zip")),
    fhirDataDir = new File(project.properties.getProperty("project.build.fhirDataDirectory")),
    fhirSchemaDir = new File("${project.properties.getProperty("project.build.schemaDirectory")}/fhir"),
    fhirSchematronDir = new File("${project.properties.getProperty("project.build.schematronDirectory")}/fhir")

ant.mkdir(dir: fhirDataDir)

ant.unzip(src: fhirXmlValidationArchiveFile, dest: fhirDataDir) {
    ant.patternset() {
        ant.include(name: "extension-definitions.xml")
        ant.include(name: "profiles-*.xml")
        ant.include(name: "search-parameters.xml")
        ant.include(name: "v*-*.xml")
        ant.include(name: "valuesets.xml")
    }
}

ant.mkdir(dir: fhirSchemaDir)

ant.unzip(src: fhirXmlValidationArchiveFile, dest: fhirSchemaDir) {
    ant.patternset() {
        ant.include(name: "fhir-single.xsd")
        ant.include(name: "fhir-xhtml.xsd")
        ant.include(name: "xml.xsd")
    }
}

ant.mkdir(dir: fhirSchematronDir)

ant.unzip(src: fhirXmlValidationArchiveFile, dest: fhirSchematronDir) {
    ant.patternset() {
        ant.include(name: "*.sch")
    }
}
