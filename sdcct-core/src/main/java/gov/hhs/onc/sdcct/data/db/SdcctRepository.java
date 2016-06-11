package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface SdcctRepository<T extends SdcctEntity> extends Repository<T, Long>, SdcctDao<T> {
    public void reindex() throws Exception;
}
