package gov.hhs.onc.sdcct.config.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.config.Option;
import gov.hhs.onc.sdcct.config.Options;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractOptions<T extends Options<T>> extends TreeMap<String, Object> implements Options<T> {
    private final static long serialVersionUID = 0L;

    protected AbstractOptions() {
        super();
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setOptions(Map<Option<Object>, ?> opts) {
        opts.forEach(this::setOption);

        return ((T) this);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <U> T setOption(Option<U> opt, @Nullable U optValue) {
        this.put(opt.getName(), optValue);

        return ((T) this);
    }

    @Nullable
    @Override
    public <U> U getOption(Option<U> opt) {
        return this.getOption(opt, null);
    }

    @Nullable
    @Override
    public <U> U getOption(Option<U> opt, @Nullable U defaultOptValue) {
        return (this.hasOption(opt) ? opt.getValueClass().cast(this.get(opt.getName())) : defaultOptValue);
    }

    @Override
    public boolean hasOption(Option<?> opt) {
        return this.containsKey(opt.getName());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T merge(@Nullable T ... mergeOpts) {
        if (!ArrayUtils.isEmpty(mergeOpts)) {
            Stream.of(mergeOpts).filter(Objects::nonNull).forEach(this::mergeInternal);
        }

        return ((T) this);
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone", CompilerWarnings.UNCHECKED })
    public T clone() {
        return this.cloneInternal().merge(((T) this));
    }

    protected void mergeInternal(T mergeOpts) {
        this.putAll(mergeOpts);
    }

    protected abstract T cloneInternal();
}
