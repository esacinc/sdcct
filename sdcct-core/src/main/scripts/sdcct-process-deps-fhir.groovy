import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import java.util.regex.Pattern
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

final String FHIR_GROUP_ID = "org.hl7.fhir"

final String FHIR_FILE_NAME_PREFIX = "fhir-"
final String FHIR_SDC_FILE_NAME_PREFIX = "sdc-"
final String FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}questionnaire"
final String FHIR_SDC_CONFORMANCE_FILE_NAME_PREFIX = "conformance-${FHIR_SDC_FILE_NAME_PREFIX}"
final String FHIR_SDC_EXT_QUESTIONNAIRE_FILE_NAME_PREFIX = "extension-${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}"
final String FHIR_SDC_ELEM_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}element"
final String FHIR_SDC_RESP_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}response"
final String FHIR_SDC_VALUE_SET_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}valueset"

final String FHIR_SCHEMA_FILE_NAME = "${FHIR_FILE_NAME_PREFIX}single.${SdcctFileNameExtensions.XSD}"
final String FHIR_XHTML_SCHEMA_FILE_NAME = "${FHIR_FILE_NAME_PREFIX}xhtml.${SdcctFileNameExtensions.XSD}"

final String FHIR_INVARIANTS_SCHEMATRON_FILE_NAME = "${FHIR_FILE_NAME_PREFIX}invariants.${SdcctFileNameExtensions.SCH}"

final Pattern FHIR_SCHEMA_XML_SCHEMA_IMPORT_PATTERN = ~/(<xs:import\s+namespace="http:\/\/www\.w3\.org\/XML\/1998\/namespace")\s+schemaLocation="xml\.xsd"(\/>)/

final Pattern FHIR_XHTML_SCHEMA_PATTERN_FIX_PATTERN = ~/(<xs:pattern\s+value=")([^"]+)(")/
final String FHIR_XHTML_SCHEMA_PATTERN_FIX_SEARCH_STR = "[-+]"
final String FHIR_XHTML_SCHEMA_PATTERN_FIX_REPLACE_STR = "[\\-\\+]"

final String[] FHIR_SDC_DATA_FILE_NAMES = [
    "${FHIR_SDC_CONFORMANCE_FILE_NAME_PREFIX}form-archiver.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_CONFORMANCE_FILE_NAME_PREFIX}form-designer.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_CONFORMANCE_FILE_NAME_PREFIX}form-filler.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_CONFORMANCE_FILE_NAME_PREFIX}form-manager.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_CONFORMANCE_FILE_NAME_PREFIX}form-receiver.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_EXT_QUESTIONNAIRE_FILE_NAME_PREFIX}-endpoint.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_EXT_QUESTIONNAIRE_FILE_NAME_PREFIX}-optionaldisplay.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_EXT_QUESTIONNAIRE_FILE_NAME_PREFIX}-provenancesignaturerequred.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_ELEM_FILE_NAME_PREFIX}.profile.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}.profile.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_RESP_FILE_NAME_PREFIX}.profile.${SdcctFileNameExtensions.XML}",
    "${FHIR_SDC_VALUE_SET_FILE_NAME_PREFIX}.profile.${SdcctFileNameExtensions.XML}",
    "valueset-${FHIR_SDC_FILE_NAME_PREFIX}dex-mimetype.${SdcctFileNameExtensions.XML}"
]

final String[] FHIR_SDC_SCHEMATRON_FILE_NAMES = [
    "${FHIR_SDC_ELEM_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDC_RESP_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDC_VALUE_SET_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}"
]

String fhirVersion = project.properties.getProperty("project.build.fhirVersion"),
    fhirSiteUrlPrefix = project.properties.getProperty("project.build.fhirSiteUrlPrefix"), fhirSiteSdcUrlPrefix = "${fhirSiteUrlPrefix}/sdc"
Artifact fhirXmlValidationArchiveFileArtifact = new DefaultArtifact(FHIR_GROUP_ID, "validation-xml", fhirVersion, Artifact.SCOPE_COMPILE,
    SdcctFileNameExtensions.ZIP, StringUtils.EMPTY, new DefaultArtifactHandler()), fhirSdcArchiveFileArtifact = new DefaultArtifact(FHIR_GROUP_ID, "sdc",
    fhirVersion, Artifact.SCOPE_COMPILE, SdcctFileNameExtensions.ZIP, StringUtils.EMPTY, new DefaultArtifactHandler())
File fhirXmlValidationArchiveFile = SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, fhirXmlValidationArchiveFileArtifact,
    new URL("${fhirSiteUrlPrefix}/validation.${SdcctFileNameExtensions.XML}.${SdcctFileNameExtensions.ZIP}"), true),
    fhirSdcArchiveLocalRepoFile = SdcctBuildUtils.buildArtifactLocalRepositoryFile(project, fhirSdcArchiveFileArtifact),
    fhirSdcArchiveDepsFile = SdcctBuildUtils.buildDependenciesArtifactFile(project, fhirSdcArchiveFileArtifact),
    fhirDataDir = new File(project.properties.getProperty("project.build.fhirDataDirectory")),
    fhirSchemaDir = new File("${project.properties.getProperty("project.build.schemaDirectory")}/fhir"),
    fhirSchematronDir = new File("${project.properties.getProperty("project.build.schematronDirectory")}/fhir"),
    fhirSchemaFile = new File(fhirSchemaDir, FHIR_SCHEMA_FILE_NAME),
    fhirXhtmlSchemaFile = new File(fhirSchemaDir, FHIR_XHTML_SCHEMA_FILE_NAME)

ant.mkdir(dir: fhirDataDir)

ant.unzip(src: fhirXmlValidationArchiveFile, dest: fhirDataDir) {
    ant.patternset() {
        ant.include(name: "extension-definitions.${SdcctFileNameExtensions.XML}")
        ant.include(name: "profiles-*.${SdcctFileNameExtensions.XML}")
        ant.include(name: "search-parameters.${SdcctFileNameExtensions.XML}")
        ant.include(name: "v*-*.${SdcctFileNameExtensions.XML}")
        ant.include(name: "valuesets.${SdcctFileNameExtensions.XML}")
    }
}

ant.mkdir(dir: fhirSchemaDir)

ant.unzip(src: fhirXmlValidationArchiveFile, dest: fhirSchemaDir) {
    ant.patternset() {
        ant.include(name: FHIR_SCHEMA_FILE_NAME)
        ant.include(name: FHIR_XHTML_SCHEMA_FILE_NAME)
    }
}

[ fhirSchemaFile, fhirXhtmlSchemaFile ].each{
    it.text = it.text.replaceAll(FHIR_SCHEMA_XML_SCHEMA_IMPORT_PATTERN, { (it[1] + it[2]) })
}

fhirXhtmlSchemaFile.text = fhirXhtmlSchemaFile.text.replaceAll(FHIR_XHTML_SCHEMA_PATTERN_FIX_PATTERN,
    { (it[1] + StringUtils.replace(((String) it[2]), FHIR_XHTML_SCHEMA_PATTERN_FIX_SEARCH_STR, FHIR_XHTML_SCHEMA_PATTERN_FIX_REPLACE_STR) + it[3]) })

ant.mkdir(dir: fhirSchematronDir)

ant.unzip(src: fhirXmlValidationArchiveFile, dest: fhirSchematronDir) {
    ant.patternset() {
        ant.include(name: FHIR_INVARIANTS_SCHEMATRON_FILE_NAME)
    }
}

if (!fhirSdcArchiveLocalRepoFile.exists()) {
    ant.get(dest: fhirDataDir) {
        FHIR_SDC_DATA_FILE_NAMES.collect{ ant.url(url: "${fhirSiteSdcUrlPrefix}/${it}") }
    }
    
    ant.get(dest: fhirSchematronDir) {
        FHIR_SDC_SCHEMATRON_FILE_NAMES.collect{ ant.url(url: "${fhirSiteSdcUrlPrefix}/${it}") }
    }
    
    ant.zip(destfile: fhirSdcArchiveDepsFile) {
        ant.fileset(dir: fhirDataDir) {
            FHIR_SDC_DATA_FILE_NAMES.collect{ ant.include(name: it) }
        }
        
        ant.fileset(dir: fhirSchematronDir) {
            FHIR_SDC_SCHEMATRON_FILE_NAMES.collect{ ant.include(name: it) }
        }
    }
    
    SdcctBuildUtils.installArtifact(ant, fhirSdcArchiveFileArtifact, fhirSdcArchiveDepsFile)
} else {
    ant.unzip(src: fhirSdcArchiveLocalRepoFile, dest: fhirDataDir) {
        ant.patternset() {
            ant.include(name: "*.${SdcctFileNameExtensions.XML}")
        }
        
        ant.cutdirsmapper(dirs: 1)
    }
    
    ant.unzip(src: fhirSdcArchiveLocalRepoFile, dest: fhirSchematronDir) {
        ant.patternset() {
            ant.include(name: "*.${SdcctFileNameExtensions.SCH}")
        }
        
        ant.cutdirsmapper(dirs: 1)
    }
}

