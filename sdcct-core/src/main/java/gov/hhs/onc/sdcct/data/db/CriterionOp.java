package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum CriterionOp implements IdentifiedBean {
    EQ, NE, LIKE, GT, LT, GE, LE, BETWEEN;

    private final String id;

    private CriterionOp() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
