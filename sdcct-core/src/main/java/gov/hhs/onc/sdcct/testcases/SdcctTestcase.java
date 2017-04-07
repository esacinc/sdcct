package gov.hhs.onc.sdcct.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStep;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.springframework.core.Ordered;

public interface SdcctTestcase<T extends SdcctTestcaseDescription> extends IdentifiedBean, NamedBean, Ordered {
    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public T getDescription();

    public void setDescription(@Nullable T desc);

    @JsonProperty
    @Override
    public String getId();

    public void setId(String id);

    public boolean hasName();

    @JsonProperty
    @Nullable
    public String getName();

    public void setName(@Nullable String name);

    @JsonProperty
    public boolean isNegative();

    public void setNegative(boolean neg);

    @JsonProperty
    public boolean isOptional();

    public void setOptional(boolean optional);

    @JsonProperty
    @Nonnegative
    public int getOrder();

    public void setOrder(@Nonnegative int order);

    @JsonProperty
    public SpecificationRole getRoleTested();

    public void setRoleTested(SpecificationRole roleTested);

    @JsonProperty
    public boolean isSdcctInitiated();

    public void setSdcctInitiated(boolean sdcctInitiated);

    @JsonProperty
    public SpecificationRole getSdcctRole();

    public void setSdcctRole(SpecificationRole sdcctRole);

    public boolean hasSteps();

    @JsonProperty
    public List<SdcctTestcaseStep> getSteps();

    public void setSteps(List<SdcctTestcaseStep> steps);
}
