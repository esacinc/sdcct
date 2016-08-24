package gov.hhs.onc.sdcct.form;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.initialize.SdcctResourceInitializer;

public interface FormInitializer<T, U extends SdcctResource, V extends SdcctResourceRegistry<T, ?, U>, W extends SdcctForm<T>> extends
    SdcctResourceInitializer<T, U, V> {
}
