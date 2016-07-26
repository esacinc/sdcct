package gov.hhs.onc.sdcct.api;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import org.slf4j.event.Level;

public enum IssueLevel implements IdentifiedBean {
    INFORMATION(Level.DEBUG), WARNING(Level.WARN), ERROR(Level.ERROR), FATAL(Level.ERROR);

    private final String id;
    private final Level loggingLevel;

    private IssueLevel(Level loggingLevel) {
        this.id = this.name().toLowerCase();
        this.loggingLevel = loggingLevel;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Level getLoggingLevel() {
        return this.loggingLevel;
    }
}
