package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import java.io.Serializable;
import org.apache.commons.lang3.text.StrBuilder;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("entityInterceptorLogging")
public class LoggingEntityInterceptor extends EmptyInterceptor {
    private static enum EntityEventType implements IdentifiedBean {
        LOADED, SAVED, DELETED;

        private final String id;

        private EntityEventType() {
            this.id = this.name().toLowerCase();
        }

        @Override
        public String getId() {
            return this.id;
        }
    }

    private final static long serialVersionUID = 0L;

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingEntityInterceptor.class);

    @Override
    public void onDelete(Object entity, Serializable id, Object[] states, String[] propNames, Type[] types) {
        LOGGER.trace(buildMessage(EntityEventType.DELETED, entity, id, true, states, propNames, types));
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] states, String[] propNames, Type[] types) {
        LOGGER.trace(buildMessage(EntityEventType.SAVED, entity, id, true, states, propNames, types));

        return super.onSave(entity, id, states, propNames, types);
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] states, String[] propNames, Type[] types) {
        LOGGER.trace(buildMessage(EntityEventType.LOADED, entity, id, false, states, propNames, types));

        return super.onLoad(entity, id, states, propNames, types);
    }

    private static String buildMessage(EntityEventType eventType, Object entity, Serializable id, boolean includeState, Object[] states, String[] propNames,
        Type[] types) {
        StrBuilder msgBuilder = new StrBuilder("Entity (class=");
        msgBuilder.append(entity.getClass().getName());
        msgBuilder.append(", id=");
        msgBuilder.append(id);
        msgBuilder.append(") ");
        msgBuilder.append(eventType.getId());
        msgBuilder.append(": [");

        for (int a = 0; a < propNames.length; a++) {
            if (a > 0) {
                msgBuilder.append("; ");
            }

            msgBuilder.append("{propName=");
            msgBuilder.append(propNames[a]);
            msgBuilder.append(", type=");
            msgBuilder.append(types[a].getName());

            if (includeState) {
                msgBuilder.append(", state=");
                msgBuilder.append(states[a]);
            }

            msgBuilder.append("}");
        }

        msgBuilder.append("]");

        return msgBuilder.build();
    }
}
