package gov.hhs.onc.sdcct.data.db.server;

import java.io.File;

public interface HsqlServer extends DbServer {
    public File getDatabaseDirectory();

    public void setDatabaseDirectory(File dbDir);
}
