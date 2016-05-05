package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import java.util.Map;
import org.apache.commons.lang3.builder.Builder;
import org.springframework.beans.factory.BeanClassLoaderAware;

public interface ResourceMetadataBuilder<T extends Enum<T> & IdentifiedBean, U, V extends ResourceMetadata<T, ? extends U>> extends BeanClassLoaderAware,
    Builder<Map<T, V>>, SpecifiedBean {
}
