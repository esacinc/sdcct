package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.db.DbObjectType;
import gov.hhs.onc.sdcct.data.db.security.DbAuthority;
import gov.hhs.onc.sdcct.data.db.security.DbPrivilegeType;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hsqldb.persist.HsqlDatabaseProperties;

public class SdcctHsqlDialect extends HSQLDialect {
    public static class SdcctHsqlDialectResolver extends AbstractDbService implements DialectResolver {
        public final static SdcctHsqlDialectResolver INSTANCE = new SdcctHsqlDialectResolver();

        private final static long serialVersionUID = 0L;

        @Nullable
        @Override
        public Dialect resolveDialect(DialectResolutionInfo info) {
            return (info.getDatabaseName().equals(HsqlDatabaseProperties.PRODUCT_NAME) ? SdcctHsqlDialect.INSTANCE : null);
        }
    }

    public static class SdcctHsqlDialectResolverContributor extends AbstractDbServiceContributor<SdcctHsqlDialectResolver> {
        public SdcctHsqlDialectResolverContributor() {
            super(SdcctHsqlDialectResolver.class);
        }

        @Override
        @SuppressWarnings({ CompilerWarnings.RAWTYPES })
        public SdcctHsqlDialectResolver initiateService(Map configValues, ServiceRegistryImplementor serviceRegistry) {
            serviceRegistry.locateServiceBinding(DialectResolver.class).setService(SdcctHsqlDialectResolver.INSTANCE);

            return SdcctHsqlDialectResolver.INSTANCE;
        }
    }

    public final static SdcctHsqlDialect INSTANCE = new SdcctHsqlDialect();

    private final static String ARG_DELIM = ", ";

    private final static String CREATE_SQL_FORMAT_PREFIX = "create ";
    private final static String GRANT_SQL_FORMAT_PREFIX = "grant ";

    private final static String GRANT_TO_SQL_FORMAT_SUFFIX = "to \"%s\";";

    private final static String CREATE_ROLE_SQL_FORMAT = CREATE_SQL_FORMAT_PREFIX + "role \"%s\";";
    private final static String CREATE_USER_SQL_FORMAT = CREATE_SQL_FORMAT_PREFIX + "user \"%s\" password '%s';";
    private final static String GRANT_ON_SQL_FORMAT = GRANT_SQL_FORMAT_PREFIX + "%s on %s %s " + GRANT_TO_SQL_FORMAT_SUFFIX;
    private final static String GRANT_ROLE_SQL_FORMAT = GRANT_SQL_FORMAT_PREFIX + "\"%s\"" + GRANT_TO_SQL_FORMAT_SUFFIX;

    public String getGrantOnString(DbAuthority authority, String ... granteeNames) {
        return this.getGrantOnString(authority.getPrivileges(), authority.getObjectType(), authority.getObjectName(), granteeNames);
    }

    public String getGrantOnString(Set<DbPrivilegeType> privileges, DbObjectType objType, String name, String ... granteeNames) {
        return String.format(GRANT_ON_SQL_FORMAT, privileges.stream().map(IdentifiedBean::getId).collect(Collectors.joining(ARG_DELIM)), objType.getId(), name,
            StringUtils.join(granteeNames, ARG_DELIM));
    }

    public String getGrantRoleString(String name, String ... granteeNames) {
        return String.format(GRANT_ROLE_SQL_FORMAT, name, StringUtils.join(granteeNames, ARG_DELIM));
    }

    public String getCreateRoleString(String name) {
        return String.format(CREATE_ROLE_SQL_FORMAT, name);
    }

    public String getCreateUserString(String name, String pass) {
        return String.format(CREATE_USER_SQL_FORMAT, name, pass);
    }
}
