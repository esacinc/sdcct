package gov.hhs.onc.sdcct.data.db.logging.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import javax.persistence.Entity;
import org.apache.commons.lang3.text.StrBuilder;
import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("entityIndexingInterceptorLogging")
public class LoggingEntityIndexingInterceptor implements EntityIndexingInterceptor<SdcctEntity> {
    private static enum EntityIndexingEventType implements IdentifiedBean {
        ADD("added"), UPDATE("updated"), COLLECTION_UPDATE("collection updated"), DELETE("deleted");

        private final String id;

        private EntityIndexingEventType(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return this.id;
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingEntityIndexingInterceptor.class);

    @Override
    public IndexingOverride onDelete(SdcctEntity entity) {
        LOGGER.trace(buildMessage(entity, EntityIndexingEventType.DELETE));

        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onCollectionUpdate(SdcctEntity entity) {
        LOGGER.trace(buildMessage(entity, EntityIndexingEventType.COLLECTION_UPDATE));

        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onUpdate(SdcctEntity entity) {
        LOGGER.trace(buildMessage(entity, EntityIndexingEventType.UPDATE));

        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onAdd(SdcctEntity entity) {
        LOGGER.trace(buildMessage(entity, EntityIndexingEventType.ADD));

        return IndexingOverride.APPLY_DEFAULT;
    }

    private static String buildMessage(SdcctEntity entity, EntityIndexingEventType eventType) {
        Class<? extends SdcctEntity> entityClass = entity.getClass();

        StrBuilder msgBuilder = new StrBuilder("Entity (class=");
        msgBuilder.append(entityClass.getName());
        msgBuilder.append(", name=");
        msgBuilder.append(entityClass.getAnnotation(Entity.class).name());
        msgBuilder.append(", id=");
        msgBuilder.append(entity.getEntityId());
        msgBuilder.append(") ");
        msgBuilder.append(eventType.getId());
        msgBuilder.append(" in index.");

        return msgBuilder.build();
    }
}
