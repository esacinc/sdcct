package gov.hhs.onc.sdcct.config;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Map;
import java.util.SortedMap;
import javax.annotation.Nullable;

public interface Options<T extends Options<T>> extends Cloneable, SortedMap<String, Object> {
    public T setOptions(Map<Option<Object>, ?> opts);

    public <U> T setOption(Option<U> opt, @Nullable U optValue);

    @Nullable
    public <U> U getOption(Option<U> opt);

    @Nullable
    public <U> U getOption(Option<U> opt, @Nullable U defaultOptValue);

    public boolean hasOption(Option<?> opt);

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T merge(@Nullable T ... mergeOpts);

    public T clone();
}
