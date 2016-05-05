package gov.hhs.onc.sdcct.data.db.event.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.ResourceProcessor;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.event.ResourceDbEventListener;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.util.Arrays;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractResourceDbEventListener<T extends Enum<T> & IdentifiedBean, U, V extends SdcctResource, W extends ResourceMetadata<T, ? extends U>, X extends ResourceProcessor<T, U, V, W>>
    extends AbstractDbEventListener<V> implements ResourceDbEventListener<T, U, V, W, X> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected X resourceProc;

    @Autowired
    protected XmlCodec xmlCodec;

    protected Class<T> typeClass;

    private final static long serialVersionUID = 0L;

    protected AbstractResourceDbEventListener(Class<T> typeClass, Class<V> entityClass, Class<? extends V> entityImplClass) {
        super(entityClass, entityImplClass);

        this.typeClass = typeClass;
    }

    @Override
    protected void onUpdate(SaveOrUpdateEvent event) throws HibernateException {
        V entity = this.entityClass.cast(event.getEntity());
        EntityPersister persister = event.getEntry().getPersister();
        int versionPropIndex = Arrays.binarySearch(persister.getPropertyNames(), DbPropertyNames.VERSION);

        persister.setPropertyValue(entity, versionPropIndex, (((long) persister.getPropertyValue(entity, versionPropIndex)) + 1L));

        this.resourceProc.process(entity, SdcctEnumUtils.findById(this.typeClass, entity.getType()));
    }

    @Override
    protected void onSave(SaveOrUpdateEvent event) throws HibernateException {
        V entity = this.entityClass.cast(event.getObject());
        entity.setVersion(1L);

        this.resourceProc.process(entity, SdcctEnumUtils.findById(this.typeClass, entity.getType()));
    }
}
