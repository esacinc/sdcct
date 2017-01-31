import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import java.util.regex.Pattern
import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

final String FHIR_GROUP_ID = "org.hl7.fhir"

final String FHIR_FILE_NAME_PREFIX = "fhir"
final String FHIR_CONFORMANCE_FILE_NAME_PREFIX = "Conformance-conformance-"
final String FHIR_STRUCT_DEF_FILE_NAME_PREFIX = "StructureDefinition-"
final String FHIR_SDC_FILE_NAME_PREFIX = "sdc-"
final String FHIR_SDCDE_FILE_NAME_PREFIX = "sdcde-"
final String FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}questionnaire"
final String FHIR_SDC_CODE_SYSTEM_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}codesystem"
final String FHIR_SDC_ELEM_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}element"
final String FHIR_SDC_RESP_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}response"
final String FHIR_SDC_VALUE_SET_FILE_NAME_PREFIX = "${FHIR_SDC_FILE_NAME_PREFIX}valueset"
final String FHIR_SDCDE_DATA_ELEM_FILE_NAME_PREFIX = "${FHIR_SDCDE_FILE_NAME_PREFIX}dataelement"
final String FHIR_SDCDE_VALUE_SET_FILE_NAME_PREFIX = "${FHIR_SDCDE_FILE_NAME_PREFIX}valueset"

final String FHIR_XML_SCHEMA_FILE_NAME = "${FHIR_FILE_NAME_PREFIX}-single.${SdcctFileNameExtensions.XSD}"
final String FHIR_XHTML_XML_SCHEMA_FILE_NAME = "${FHIR_FILE_NAME_PREFIX}-xhtml.${SdcctFileNameExtensions.XSD}"

final String FHIR_INVARIANTS_SCHEMATRON_FILE_NAME = "${FHIR_FILE_NAME_PREFIX}-invariants.${SdcctFileNameExtensions.SCH}"

final Pattern FHIR_XML_SCHEMA_SCHEMA_IMPORT_PATTERN = ~/(<xs:import\s+namespace="http:\/\/www\.w3\.org\/XML\/1998\/namespace")\s+schemaLocation="xml\.xsd"(\/>)/

final Pattern FHIR_XHTML_XML_SCHEMA_PATTERN_FIX_PATTERN = ~/(<xs:pattern\s+value=")([^"]+)(")/
final String FHIR_XHTML_XML_SCHEMA_PATTERN_FIX_SEARCH_STR = "[-+]"
final String FHIR_XHTML_XML_SCHEMA_PATTERN_FIX_REPLACE_STR = "[\\-\\+]"

final String[] FHIR_SDC_DATA_FILE_NAMES = [
    "${FHIR_CONFORMANCE_FILE_NAME_PREFIX}${FHIR_SDC_FILE_NAME_PREFIX}form-archiver.${SdcctFileNameExtensions.XML}",
    "${FHIR_CONFORMANCE_FILE_NAME_PREFIX}${FHIR_SDC_FILE_NAME_PREFIX}form-filler.${SdcctFileNameExtensions.XML}",
    "${FHIR_CONFORMANCE_FILE_NAME_PREFIX}${FHIR_SDC_FILE_NAME_PREFIX}form-manager.${SdcctFileNameExtensions.XML}",
    "${FHIR_CONFORMANCE_FILE_NAME_PREFIX}${FHIR_SDC_FILE_NAME_PREFIX}form-receiver.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}-endpoint.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}-optionalDisplay.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}-provenanceSignatureRequired.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_CODE_SYSTEM_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_ELEM_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_RESP_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDC_VALUE_SET_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.XML}"
]

final String[] FHIR_SDCDE_DATA_FILE_NAMES = [
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDCDE_DATA_ELEM_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.XML}",
    "${FHIR_STRUCT_DEF_FILE_NAME_PREFIX}${FHIR_SDCDE_VALUE_SET_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.XML}"
    
]

final String[] FHIR_SDC_SCHEMATRON_FILE_NAMES = [
    "${FHIR_SDC_CODE_SYSTEM_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDC_ELEM_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDC_QUESTIONNAIRE_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDC_RESP_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDC_VALUE_SET_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}"
]

final String[] FHIR_SDCDE_SCHEMATRON_FILE_NAMES = [
    "${FHIR_SDCDE_DATA_ELEM_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}",
    "${FHIR_SDCDE_VALUE_SET_FILE_NAME_PREFIX}.${SdcctFileNameExtensions.SCH}"
]

String fhirVersion = project.properties.getProperty("project.build.fhirVersion"),
    fhirSiteUrlPrefix = project.properties.getProperty("project.build.fhirSiteUrlPrefix"),
    fhirSdcSiteUrlPrefix = project.properties.getProperty("project.build.fhirSdcImplGuideSiteUrlPrefix"),
    fhirSdcVersion = project.properties.getProperty("project.build.fhirSdcVersion"),
    fhirSdcDeSiteUrlPrefix = project.properties.getProperty("project.build.fhirSdcDeImplGuideSiteUrlPrefix"),
    fhirSdcDeVersion = project.properties.getProperty("project.build.fhirSdcDeVersion")
Artifact fhirXmlDefsArchiveFileArtifact = new DefaultArtifact(FHIR_GROUP_ID, "definitions-xml", fhirVersion, Artifact.SCOPE_COMPILE,
    SdcctFileNameExtensions.ZIP, StringUtils.EMPTY, new DefaultArtifactHandler()), fhirXsdArchiveFileArtifact = new DefaultArtifact(FHIR_GROUP_ID, "xsd",
    fhirVersion, Artifact.SCOPE_COMPILE, SdcctFileNameExtensions.ZIP, StringUtils.EMPTY, new DefaultArtifactHandler()),
    fhirSdcArchiveFileArtifact = new DefaultArtifact(FHIR_GROUP_ID, "sdc", fhirSdcVersion,
    Artifact.SCOPE_COMPILE, SdcctFileNameExtensions.ZIP, StringUtils.EMPTY, new DefaultArtifactHandler()),
    fhirSdcDeArchiveFileArtifact = new DefaultArtifact(FHIR_GROUP_ID, "sdcde", fhirSdcDeVersion,
    Artifact.SCOPE_COMPILE, SdcctFileNameExtensions.ZIP, StringUtils.EMPTY, new DefaultArtifactHandler())
File fhirXmlDefsArchiveFile = SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, fhirXmlDefsArchiveFileArtifact,
    new URL("${fhirSiteUrlPrefix}/definitions.${SdcctFileNameExtensions.XML}.${SdcctFileNameExtensions.ZIP}"), true),
    fhirXsdArchiveFile = SdcctBuildUtils.resolveRemoteArtifact(log, project, ant, fhirXsdArchiveFileArtifact,
    new URL("${fhirSiteUrlPrefix}/${FHIR_FILE_NAME_PREFIX}-all-${SdcctFileNameExtensions.XSD}.${SdcctFileNameExtensions.ZIP}"), true),
    fhirSdcArchiveLocalRepoFile = SdcctBuildUtils.buildArtifactLocalRepositoryFile(project, fhirSdcArchiveFileArtifact),
    fhirSdcArchiveDepsFile = SdcctBuildUtils.buildDependenciesArtifactFile(project, fhirSdcArchiveFileArtifact),
    fhirSdcDeArchiveLocalRepoFile = SdcctBuildUtils.buildArtifactLocalRepositoryFile(project, fhirSdcDeArchiveFileArtifact),
    fhirSdcDeArchiveDepsFile = SdcctBuildUtils.buildDependenciesArtifactFile(project, fhirSdcDeArchiveFileArtifact),
    fhirDataDir = new File(project.properties.getProperty("project.build.fhirDataDirectory")),
    fhirSchemaDir = new File("${project.properties.getProperty("project.build.schemaDirectory")}/fhir"),
    fhirSchematronDir = new File("${project.properties.getProperty("project.build.schematronDirectory")}/fhir"),
    fhirXmlSchemaFile = new File(fhirSchemaDir, FHIR_XML_SCHEMA_FILE_NAME),
    fhirXhtmlXmlSchemaFile = new File(fhirSchemaDir, FHIR_XHTML_XML_SCHEMA_FILE_NAME)

ant.mkdir(dir: fhirDataDir)

ant.unzip(src: fhirXmlDefsArchiveFile, dest: fhirDataDir) {
    ant.patternset() {
        ant.include(name: "extension-definitions.${SdcctFileNameExtensions.XML}")
        ant.include(name: "profiles-*.${SdcctFileNameExtensions.XML}")
        ant.include(name: "search-parameters.${SdcctFileNameExtensions.XML}")
        ant.include(name: "v*-*.${SdcctFileNameExtensions.XML}")
        ant.include(name: "valuesets.${SdcctFileNameExtensions.XML}")
    }
}

ant.mkdir(dir: fhirSchemaDir)

ant.unzip(src: fhirXsdArchiveFile, dest: fhirSchemaDir) {
    ant.patternset() {
        ant.include(name: FHIR_XML_SCHEMA_FILE_NAME)
        ant.include(name: FHIR_XHTML_XML_SCHEMA_FILE_NAME)
    }
}

[ fhirXmlSchemaFile, fhirXhtmlXmlSchemaFile ].each{
    it.text = it.text.replaceAll(FHIR_XML_SCHEMA_SCHEMA_IMPORT_PATTERN, { (it[1] + it[2]) })
}

fhirXhtmlXmlSchemaFile.text = fhirXhtmlXmlSchemaFile.text.replaceAll(FHIR_XHTML_XML_SCHEMA_PATTERN_FIX_PATTERN,
    { (it[1] + StringUtils.replace(((String) it[2]), FHIR_XHTML_XML_SCHEMA_PATTERN_FIX_SEARCH_STR, FHIR_XHTML_XML_SCHEMA_PATTERN_FIX_REPLACE_STR) + it[3]) })

ant.mkdir(dir: fhirSchematronDir)

ant.unzip(src: fhirXsdArchiveFile, dest: fhirSchematronDir) {
    ant.patternset() {
        ant.include(name: FHIR_INVARIANTS_SCHEMATRON_FILE_NAME)
    }
}

if (!fhirSdcArchiveLocalRepoFile.exists()) {
    ant.get(dest: fhirDataDir) {
        FHIR_SDC_DATA_FILE_NAMES.collect{ ant.url(url: "${fhirSdcSiteUrlPrefix}/${it}") }
    }
    
    ant.get(dest: fhirSchematronDir) {
        FHIR_SDC_SCHEMATRON_FILE_NAMES.collect{ ant.url(url: "${fhirSdcSiteUrlPrefix}/${it}") }
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

if (!fhirSdcDeArchiveLocalRepoFile.exists()) {
    ant.get(dest: fhirDataDir) {
        FHIR_SDCDE_DATA_FILE_NAMES.collect{ ant.url(url: "${fhirSdcDeSiteUrlPrefix}/${it}") }
    }
    
    ant.get(dest: fhirSchematronDir) {
        FHIR_SDCDE_SCHEMATRON_FILE_NAMES.collect{ ant.url(url: "${fhirSdcDeSiteUrlPrefix}/${it}") }
    }
    
    ant.zip(destfile: fhirSdcDeArchiveDepsFile) {
        ant.fileset(dir: fhirDataDir) {
            FHIR_SDCDE_DATA_FILE_NAMES.collect{ ant.include(name: it) }
        }
        
        ant.fileset(dir: fhirSchematronDir) {
            FHIR_SDCDE_SCHEMATRON_FILE_NAMES.collect{ ant.include(name: it) }
        }
    }
    
    SdcctBuildUtils.installArtifact(ant, fhirSdcDeArchiveFileArtifact, fhirSdcDeArchiveDepsFile)
} else {
    ant.unzip(src: fhirSdcDeArchiveLocalRepoFile, dest: fhirDataDir) {
        ant.patternset() {
            ant.include(name: "*.${SdcctFileNameExtensions.XML}")
        }
        
        ant.cutdirsmapper(dirs: 1)
    }
    
    ant.unzip(src: fhirSdcDeArchiveLocalRepoFile, dest: fhirSchematronDir) {
        ant.patternset() {
            ant.include(name: "*.${SdcctFileNameExtensions.SCH}")
        }
        
        ant.cutdirsmapper(dirs: 1)
    }
}
