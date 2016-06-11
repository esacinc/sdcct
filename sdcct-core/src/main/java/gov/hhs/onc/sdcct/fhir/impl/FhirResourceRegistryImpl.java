package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctResourceRegistry;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.Meta;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.utils.SdcctDateUtils;
import gov.hhs.onc.sdcct.utils.SdcctLocaleUtils;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;

public class FhirResourceRegistryImpl<T extends Resource> extends AbstractSdcctResourceRegistry<T, FhirResourceMetadata<T>, FhirResource>
    implements FhirResourceRegistry<T> {
    public FhirResourceRegistryImpl(FhirResourceMetadata<T> resourceMetadata) {
        super(resourceMetadata, FhirResource.class, FhirResourceImpl.class, FhirResourceImpl::new);
    }

    @Override
    protected T buildBean(T bean, FhirResource entity) throws Exception {
        // noinspection ConstantConditions
        bean.setId(new IdTypeImpl().setValue(entity.getId().toString()));

        if (!bean.hasMeta()) {
            bean.setMeta(new MetaImpl());
        }

        Meta meta = bean.getMeta();

        // noinspection ConstantConditions
        meta.setVersionId(new IdTypeImpl().setValue(entity.getVersion().toString()));

        GregorianCalendar lastUpdatedCalendar = ((GregorianCalendar) GregorianCalendar.getInstance(SdcctDateUtils.UTC_TZ, SdcctLocaleUtils.DEFAULT_LOCALE));
        // noinspection ConstantConditions
        lastUpdatedCalendar.setTime(entity.getModifiedTimestamp());
        meta.setLastUpdated(new InstantTypeImpl().setValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(lastUpdatedCalendar)));

        return super.buildBean(bean, entity);
    }
}
