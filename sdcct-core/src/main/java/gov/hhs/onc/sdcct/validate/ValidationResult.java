package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import java.util.List;
import java.util.Map;

public interface ValidationResult {
    public ValidationResult append(ValidationResult result);
    
    public ValidationResult addIssues(ValidationIssue ... issues);

    public boolean hasIssues();

    public List<ValidationIssue> getIssues();

    public ValidationResult setIssues(ValidationIssue ... issues);

    public Map<SdcctIssueSeverity, Integer> getIssueTotals();
}
