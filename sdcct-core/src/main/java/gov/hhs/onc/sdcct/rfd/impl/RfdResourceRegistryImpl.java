package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class RfdResourceRegistryImpl<T extends IdentifiedExtensionType> extends AbstractSdcctResourceRegistry<T, RfdResourceMetadata<T>, RfdResource>
    implements RfdResourceRegistry<T> {
    public RfdResourceRegistryImpl(RfdResourceMetadata<T> resourceMetadata) {
        super(resourceMetadata, RfdResource.class, RfdResourceImpl.class, RfdResourceImpl::new);
    }

    @Override
    protected T buildBean(T bean, RfdResource entity) throws Exception {
        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

        // noinspection ConstantConditions
        beanWrapper.setPropertyValue(this.resourceParamMetadatas.get(ResourceParamNames.INSTANCE_ID).getExpression(), entity.getInstanceId().toString());

        // noinspection ConstantConditions
        beanWrapper.setPropertyValue(this.resourceParamMetadatas.get(ResourceParamNames.VERSION).getExpression(), entity.getVersion().toString());

        return super.buildBean(bean, entity);
    }
}
