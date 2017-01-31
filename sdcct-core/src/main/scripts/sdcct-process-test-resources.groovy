import gov.hhs.onc.sdcct.utils.SdcctStringUtils

SdcctStringUtils.tokenize(((String) this.binding.getVariable("dirs"))).each{
    ant.mkdir(dir: new File(it))
}
