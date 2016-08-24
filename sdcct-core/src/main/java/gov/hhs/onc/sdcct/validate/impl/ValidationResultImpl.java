package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationResultImpl implements ValidationResult {
    private List<ValidationIssue> issues = new ArrayList<>();

    @Override
    public ValidationResult append(ValidationResult result) {
        this.issues.addAll(result.getIssues());

        return this;
    }

    @Override
    public ValidationResult addIssues(ValidationIssue ... issues) {
        Stream.of(issues).forEach(this.issues::add);

        return this;
    }

    @Override
    public boolean hasIssues() {
        return !this.issues.isEmpty();
    }

    @Override
    public List<ValidationIssue> getIssues() {
        return this.issues;
    }

    @Override
    public ValidationResult setIssues(ValidationIssue ... issues) {
        this.issues.clear();

        return this.addIssues(issues);
    }

    @Override
    public Map<SdcctIssueSeverity, Integer> getIssueTotals() {
        return issues.stream().collect(Collectors.groupingBy(ValidationIssue::getSeverity, Collectors.reducing(0, issue -> 1, Integer::sum)));
    }
}
