package gov.hhs.onc.sdcct.config;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Map;
import java.util.SortedMap;
import javax.annotation.Nullable;

public interface SdcctOptions<T extends SdcctOptions<T>> extends Cloneable, SortedMap<String, Object> {
    public T setOptions(Map<SdcctOption<Object>, ?> opts);

    public <U> T setOption(SdcctOption<U> opt, @Nullable U optValue);

    @Nullable
    public <U> U getOption(SdcctOption<U> opt);

    @Nullable
    public <U> U getOption(SdcctOption<U> opt, @Nullable U defaultOptValue);

    public boolean hasOption(SdcctOption<?> opt);

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T merge(@Nullable T ... mergeOpts);

    public T clone();
}
