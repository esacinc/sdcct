package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctRegistry;
import gov.hhs.onc.sdcct.data.search.SearchParamNames;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Meta;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Supplier;

public abstract class AbstractFhirResourceRegistry<T extends DomainResource, U extends FhirResource, V extends SdcctDao<U>, W extends SdcctDataService<U, V>>
    extends AbstractSdcctRegistry<T, U, V, W> implements FhirResourceRegistry<T, U, V, W> {
    protected AbstractFhirResourceRegistry(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass,
        Supplier<U> entityBuilder) {
        super(beanClass, beanImplClass, entityClass, entityImplClass, entityBuilder);
    }

    @Override
    protected U buildSearchParams(T bean, U entity) throws Exception {
        super.buildSearchParams(bean, entity);

        if (bean.hasMeta()) {
            Meta meta = bean.getMeta();

            if (meta.hasLastUpdated()) {
                this.buildDateSearchParam(entity, SearchParamNames.LAST_UPDATED, new Date(meta.getLastUpdated().getValue().toGregorianCalendar().toInstant()
                    .toEpochMilli()));
            }

            if (meta.hasProfile()) {
                meta.getProfile().stream().forEach(profile -> this.buildUriSearchParam(entity, SearchParamNames.PROFILE, URI.create(profile.getValue())));
            }

            if (meta.hasSecurity()) {
                meta.getSecurity()
                    .stream()
                    .forEach(
                        security -> this.buildTokenSearchParam(entity, SearchParamNames.SECURITY,
                            (security.hasSystem() ? URI.create(security.getSystem().getValue()) : null), security.getCode().getValue()));
            }

            if (meta.hasTag()) {
                meta.getTag()
                    .stream()
                    .forEach(
                        tag -> this.buildTokenSearchParam(entity, SearchParamNames.TAG, (tag.hasSystem() ? URI.create(tag.getSystem().getValue()) : null), tag
                            .getCode().getValue()));
            }
        }

        return entity;
    }

    @Override
    protected U encode(T bean) throws Exception {
        U entity = super.encode(bean);

        if (bean.hasText()) {
            entity.setText(new String(this.xmlCodec.encode(bean.getText().getDiv(), null), StandardCharsets.UTF_8));
        }

        return entity;
    }
}
