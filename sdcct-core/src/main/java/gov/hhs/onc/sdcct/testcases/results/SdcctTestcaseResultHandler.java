package gov.hhs.onc.sdcct.testcases.results;

import java.util.List;
import javax.annotation.Nonnegative;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctTestcaseResultHandler<T extends SdcctTestcaseResult<?, ?, ?>> extends InitializingBean {
    public void addResult(T result);

    public List<T> findResults(@Nonnegative long submittedAfterTimestamp);
}
