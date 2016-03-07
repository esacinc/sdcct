package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingIndexQueryInterceptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingIndexQueryInterceptor.class);

    private Class<? extends SdcctEntity> entityClass;
    private String entityName;

    public LoggingIndexQueryInterceptor(Class<? extends SdcctEntity> entityClass, String entityName) {
        this.entityClass = entityClass;
        this.entityName = entityName;
    }

    public Query intercept(Query query) {
        StrBuilder msgBuilder = new StrBuilder("Entity (class=");
        msgBuilder.append(this.entityClass.getName());
        msgBuilder.append(", name=");
        msgBuilder.append(this.entityName);
        msgBuilder.append(") Lucene query: ");
        msgBuilder.append(query.toString());

        LOGGER.trace(msgBuilder.build());

        return query;
    }
}
