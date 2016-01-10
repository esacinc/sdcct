package gov.hhs.onc.sdcct.transform.render;

import java.util.Map;

public interface SdcctRenderer {
    public byte[] render(Object obj) throws Exception;

    public byte[] render(Object obj, Map<String, Object> opts) throws Exception;

    public Map<String, Object> getDefaultOptions();

    public void setDefaultOptions(Map<String, Object> defaultOpts);

    public RenderType getType();
}
