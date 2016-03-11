import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import org.apache.commons.lang3.BooleanUtils

def skipTestsPropName = "skipTests"

if (!SdcctBuildUtils.containsExecutionProperty(project, session, skipTestsPropName)) {
    project.properties[skipTestsPropName] = Boolean.toString(false)
}

def skipTestsPropValue = SdcctBuildUtils.getExecutionProperty(project, session, skipTestsPropName)

["Unit", "It"].each {
    def skipTestsTypePropName = (skipTestsPropName + it)

    if (!SdcctBuildUtils.containsExecutionProperty(project, session, skipTestsTypePropName)) {
        project.properties[skipTestsTypePropName] = skipTestsPropValue
    }

    if (BooleanUtils.toBoolean(SdcctBuildUtils.getExecutionProperty(project, session, skipTestsTypePropName))) {
        log.info("Tests of type (propName=${skipTestsTypePropName}) will be skipped.")
    }
}
