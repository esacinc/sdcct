package gov.hhs.onc.sdcct.data.db.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.data.db.DbStatementType;
import java.util.List;

public interface DbStatementEvent extends DbEvent {
    public void addParameter(DbParam param);

    @JsonProperty
    public List<DbParam> getParameters();

    @JsonProperty
    public String getSql();

    public void setSql(String sql);

    @JsonProperty
    public DbStatementType getType();
}
