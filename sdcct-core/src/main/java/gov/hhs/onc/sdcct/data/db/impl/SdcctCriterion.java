package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.CriterionOp;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class SdcctCriterion extends AbstractSdcctCriteriaComponent<SdcctCriterion> {
    private CriterionOp op;
    private Object value;

    public SdcctCriterion(String propName, CriterionOp op, Object value) {
        this(null, propName, op, value);
    }

    public SdcctCriterion(@Nullable Class<? extends SdcctEntity> entityClass, String propName, CriterionOp op, Object value) {
        super(entityClass, propName);
        
        this.op = op;
        this.value = value;
    }

    public static SdcctCriterion between(String propName, Object value1, Object value2) {
        return between(null, propName, value1, value2);
    }

    public static SdcctCriterion between(@Nullable Class<? extends SdcctEntity> entityClass, String propName, Object value1, Object value2) {
        return new SdcctCriterion(entityClass, propName, CriterionOp.BETWEEN, ArrayUtils.toArray(value1, value2));
    }

    public static SdcctCriterion le(String propName, Object value) {
        return le(null, propName, value);
    }

    public static SdcctCriterion le(@Nullable Class<? extends SdcctEntity> entityClass, String propName, Object value) {
        return new SdcctCriterion(entityClass, propName, CriterionOp.LE, value);
    }

    public static SdcctCriterion ge(String propName, Object value) {
        return ge(null, propName, value);
    }

    public static SdcctCriterion ge(@Nullable Class<? extends SdcctEntity> entityClass, String propName, Object value) {
        return new SdcctCriterion(entityClass, propName, CriterionOp.GE, value);
    }

    public static SdcctCriterion lt(String propName, Object value) {
        return lt(null, propName, value);
    }

    public static SdcctCriterion lt(@Nullable Class<? extends SdcctEntity> entityClass, String propName, Object value) {
        return new SdcctCriterion(entityClass, propName, CriterionOp.LT, value);
    }

    public static SdcctCriterion gt(String propName, Object value) {
        return gt(null, propName, value);
    }

    public static SdcctCriterion gt(@Nullable Class<? extends SdcctEntity> entityClass, String propName, Object value) {
        return new SdcctCriterion(entityClass, propName, CriterionOp.GT, value);
    }

    public static SdcctCriterion ne(String propName, Object value) {
        return ne(null, propName, value);
    }

    public static SdcctCriterion ne(@Nullable Class<? extends SdcctEntity> entityClass, String propName, Object value) {
        return new SdcctCriterion(entityClass, propName, CriterionOp.NE, value);
    }

    public static SdcctCriterion eq(String propName, Object value) {
        return eq(null, propName, value);
    }

    public static SdcctCriterion eq(@Nullable Class<? extends SdcctEntity> entityClass, String propName, Object value) {
        return new SdcctCriterion(entityClass, propName, CriterionOp.EQ, value);
    }

    public CriterionOp getOperation() {
        return this.op;
    }

    public SdcctCriterion setOperation(CriterionOp op) {
        this.op = op;

        return this;
    }

    public Object getValue() {
        return this.value;
    }

    public SdcctCriterion setValue(Object value) {
        this.value = value;

        return this;
    }
}
