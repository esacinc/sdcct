package gov.hhs.onc.sdcct.transform.location;

import gov.hhs.onc.sdcct.transform.SdcctTransformException;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;

public interface SdcctLocator {
    public SdcctLocation buildLocation(SdcctLocation loc) throws SdcctTransformException;

    public void setAttribute(@Nullable QName attrQname);

    public void popElement();

    public void pushElement(QName elemQname);
}
