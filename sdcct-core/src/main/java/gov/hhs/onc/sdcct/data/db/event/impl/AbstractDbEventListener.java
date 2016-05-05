package gov.hhs.onc.sdcct.data.db.event.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.event.DbEventListener;
import gov.hhs.onc.sdcct.data.metadata.MetadataService;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.Status;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDbEventListener<T extends SdcctEntity> implements DbEventListener<T> {
    @Autowired
    protected MetadataService metadataService;

    protected Class<T> entityClass;
    protected Class<? extends T> entityImplClass;

    private final static long serialVersionUID = 0L;

    protected AbstractDbEventListener(Class<T> entityClass, Class<? extends T> entityImplClass) {
        this.entityClass = entityClass;
        this.entityImplClass = entityImplClass;
    }

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        Object entity = event.getObject();

        if (!this.entityClass.isAssignableFrom(entity.getClass())) {
            return;
        }

        EntityEntry entry = event.getEntry();

        if (entry != null) {
            if (entry.getStatus() != Status.DELETED) {
                this.onUpdate(event);
            }
        } else {
            this.onSave(event);
        }
    }

    protected void onUpdate(SaveOrUpdateEvent event) throws HibernateException {
    }

    protected void onSave(SaveOrUpdateEvent event) throws HibernateException {
    }
}
