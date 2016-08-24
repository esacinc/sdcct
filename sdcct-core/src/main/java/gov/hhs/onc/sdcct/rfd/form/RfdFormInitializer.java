package gov.hhs.onc.sdcct.rfd.form;

import gov.hhs.onc.sdcct.form.FormInitializer;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.sdc.FormDesignType;

public interface RfdFormInitializer extends FormInitializer<FormDesignType, RfdResource, RfdResourceRegistry<FormDesignType>, RfdForm> {
}
