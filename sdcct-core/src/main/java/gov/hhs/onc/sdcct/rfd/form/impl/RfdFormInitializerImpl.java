package gov.hhs.onc.sdcct.rfd.form.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.form.impl.AbstractFormInitializer;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.form.RfdFormInitializer;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import java.net.URI;

public class RfdFormInitializerImpl extends AbstractFormInitializer<FormDesignType, RfdResource, RfdResourceRegistry<FormDesignType>, RfdForm>
    implements RfdFormInitializer {
    public RfdFormInitializerImpl(RfdResourceRegistry<FormDesignType> registry) {
        super(registry);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected SdcctCriteria<RfdResource> buildCriteria(RfdForm form) {
        return this.registry
            .buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.URI_PARAMS, ResourceParamNames.IDENTIFIER, URI.create(form.getIdentifier())));
    }
}
