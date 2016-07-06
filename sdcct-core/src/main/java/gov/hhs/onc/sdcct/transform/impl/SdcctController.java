package gov.hhs.onc.sdcct.transform.impl;

import java.util.HashMap;
import java.util.Map;
import net.sf.saxon.Controller;
import net.sf.saxon.expr.instruct.Executable;

public class SdcctController extends Controller {
    private Map<Object, Object> contextData = new HashMap<>();

    public SdcctController(SdcctConfiguration config) {
        super(config);
    }

    public SdcctController(SdcctConfiguration config, Executable exec) {
        super(config, exec);
    }

    @Override
    public SdcctConfiguration getConfiguration() {
        return ((SdcctConfiguration) super.getConfiguration());
    }

    public Map<Object, Object> getContextData() {
        return this.contextData;
    }
}
