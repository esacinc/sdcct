import gov.hhs.onc.sdcct.tools.utils.SdcctToolUtils
import org.apache.commons.lang3.BooleanUtils

def skipTestsPropName = "skipTests"

if (!SdcctToolUtils.containsExecutionProperty(project, session, skipTestsPropName)) {
    project.properties[skipTestsPropName] = Boolean.toString(false)
}

def skipTestsPropValue = SdcctToolUtils.getExecutionProperty(project, session, skipTestsPropName)

["Unit", "It"].each {
    def skipTestsTypePropName = (skipTestsPropName + it)

    if (!SdcctToolUtils.containsExecutionProperty(project, session, skipTestsTypePropName)) {
        project.properties[skipTestsTypePropName] = skipTestsPropValue
    }

    if (BooleanUtils.toBoolean(SdcctToolUtils.getExecutionProperty(project, session, skipTestsTypePropName))) {
        log.info("Tests of type (propName=${skipTestsTypePropName}) will be skipped.")
    }
}
