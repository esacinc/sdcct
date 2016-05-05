package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import java.math.BigInteger;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface SdcctRepository<T extends SdcctEntity> extends Repository<T, BigInteger>, SdcctDataAccessor<T, T> {
}
