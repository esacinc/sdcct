package gov.hhs.onc.sdcct.transform.saxon.impl;

import java.util.HashMap;
import java.util.Map;
import net.sf.saxon.Controller;
import net.sf.saxon.expr.instruct.Executable;

public class SdcctSaxonController extends Controller {
    private Map<Object, Object> contextData = new HashMap<>();

    public SdcctSaxonController(SdcctSaxonConfiguration config) {
        super(config);
    }

    public SdcctSaxonController(SdcctSaxonConfiguration config, Executable exec) {
        super(config, exec);
    }

    @Override
    public SdcctSaxonConfiguration getConfiguration() {
        return ((SdcctSaxonConfiguration) super.getConfiguration());
    }

    public Map<Object, Object> getContextData() {
        return this.contextData;
    }
}
