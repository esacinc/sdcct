package gov.hhs.onc.sdcct.json;

import com.fasterxml.jackson.databind.Module;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import java.util.List;

public interface JsonCodecOptions<T extends JsonCodecOptions<T>> extends ContentCodecOptions<T> {
    public T addModules(Module ... modules);

    public boolean hasModules();

    public List<Module> getModules();

    public T setModules(Module ... modules);
}
