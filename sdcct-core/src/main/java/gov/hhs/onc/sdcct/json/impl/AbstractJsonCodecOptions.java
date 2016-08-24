package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.Module;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.json.JsonCodecOptions;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodecOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractJsonCodecOptions<T extends JsonCodecOptions<T>> extends AbstractContentCodecOptions<T> implements JsonCodecOptions<T> {
    protected List<Module> modules = new ArrayList<>();

    private final static long serialVersionUID = 0L;

    protected AbstractJsonCodecOptions() {
        super();
    }

    @Override
    protected void mergeInternal(T mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.modules.addAll(mergeOpts.getModules());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T addModules(Module ... modules) {
        Stream.of(modules).forEach(this.modules::add);

        return ((T) this);
    }

    @Override
    public boolean hasModules() {
        return !this.modules.isEmpty();
    }

    @Override
    public List<Module> getModules() {
        return this.modules;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setModules(Module ... modules) {
        this.modules.clear();

        Stream.of(modules).forEach(this.modules::add);

        return ((T) this);
    }
}
