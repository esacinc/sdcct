package gov.hhs.onc.sdcct.validate;

import net.sf.saxon.om.NodeInfo;

public interface SdcctValidatorService {
    public void validate(NodeInfo nodeInfo) throws ValidationException;
}
