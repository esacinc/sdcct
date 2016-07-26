package gov.hhs.onc.sdcct.beans;

import javax.xml.transform.SourceLocator;
import org.xml.sax.Locator;

public interface LocationBean extends Locator, PublicIdentifiedBean, SourceLocator, SystemIdentifiedBean {
}
