package gov.hhs.onc.sdcct.rfd.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.db.impl.AbstractDbBeanDefinitionRegistryPostProcessor;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.RfdResourceSearchService;
import gov.hhs.onc.sdcct.rfd.RfdResourceType;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import gov.hhs.onc.sdcct.sdc.impl.AbstractIdentifiedExtensionType;
import org.springframework.stereotype.Component;

@Component("beanDefRegistryPostProcDbRfd")
public class RfdDbBeanDefinitionRegistryPostProcessor
    extends
    AbstractDbBeanDefinitionRegistryPostProcessor<RfdResourceType, IdentifiedExtensionType, RfdResource, RfdResourceRegistry<IdentifiedExtensionType>, RfdResourceSearchService<IdentifiedExtensionType>> {
    @SuppressWarnings({ CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED })
    public RfdDbBeanDefinitionRegistryPostProcessor() {
        super(
            SpecificationType.RFD,
            RfdResourceType.class,
            IdentifiedExtensionType.class,
            AbstractIdentifiedExtensionType.class,
            RfdResource.class,
            ((Class<RfdResourceRegistry<IdentifiedExtensionType>>) ((Class<? extends RfdResourceRegistry>) RfdResourceRegistry.class)),
            ((Class<RfdResourceRegistryImpl<IdentifiedExtensionType>>) ((Class<? extends RfdResourceRegistryImpl>) RfdResourceRegistryImpl.class)),
            ((Class<RfdResourceSearchService<IdentifiedExtensionType>>) ((Class<? extends RfdResourceSearchService>) RfdResourceSearchService.class)),
            ((Class<RfdResourceSearchServiceImpl<IdentifiedExtensionType>>) ((Class<? extends RfdResourceSearchServiceImpl>) RfdResourceSearchServiceImpl.class)));
    }
}
