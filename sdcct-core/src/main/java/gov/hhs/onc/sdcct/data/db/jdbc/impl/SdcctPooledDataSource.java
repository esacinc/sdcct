package gov.hhs.onc.sdcct.data.db.jdbc.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.DisposableBean;

public class SdcctPooledDataSource extends HikariDataSource implements DisposableBean {
    public SdcctPooledDataSource(SdcctDataSourceConfig config) {
        super(config);
    }

    @Override
    public void destroy() throws Exception {
        this.close();
    }
}
