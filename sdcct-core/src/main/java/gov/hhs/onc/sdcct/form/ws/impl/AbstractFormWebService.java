package gov.hhs.onc.sdcct.form.ws.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.form.ws.FormWebService;
import gov.hhs.onc.sdcct.ws.metadata.InteractionWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.ResourceWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.WsMetadata;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFormWebService<T extends SdcctResource, U extends SdcctResourceRegistry<?, ?, T>, V extends SearchService<?, ?, T, ?>, W extends InteractionWsMetadata, X extends ResourceWsMetadata<?, ?, W>, Y extends WsMetadata<W, X>>
    implements FormWebService<T, U, V, W, X, Y> {
    @Autowired
    protected Bus bus;

    protected Y metadata;
    protected Map<Class<?>, U> resourceRegistries = new TreeMap<>(Comparator.comparing(Class::getName));
    protected Map<Class<?>, V> searchServices = new TreeMap<>(Comparator.comparing(Class::getName));
    protected Map<String, X> resourceMetadatas;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected <Z> T saveBean(U resourceRegistry, Z bean, T entity) throws Exception {
        return ((SdcctResourceRegistry<Z, ?, T>) resourceRegistry).saveBean(bean, entity);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected <Z> List<Z> findBeans(V searchService, MultivaluedMap<String, String> params) throws Exception {
        return ((List<Z>) searchService.search(params));
    }

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected <Z> Z findBean(Class<Z> beanClass, V searchService, MultivaluedMap<String, String> params) throws Exception {
        List<?> beans = searchService.search(params);

        return (!beans.isEmpty() ? beanClass.cast(beans.get(0)) : null);
    }

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected <Z> Z findBean(Class<Z> beanClass, U resourceRegistry, long id, long instanceId, @Nullable Long version) throws Exception {
        return beanClass.cast(resourceRegistry.findBean(resourceRegistry.buildCriteria(SdcctCriterionUtils.matchId(id),
            SdcctCriterionUtils.matchInstanceId(instanceId), SdcctCriterionUtils.<T> matchDeleted().not(),
            ((version != null) ? SdcctCriterionUtils.matchVersion(version) : SdcctCriterionUtils.matchLatestVersion()))));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.resourceMetadatas = this.metadata.getResourceMetadatas();
    }

    @Override
    public Y getMetadata() {
        return this.metadata;
    }

    @Override
    public void setMetadata(Y metadata) {
        this.metadata = metadata;
    }

    @Override
    public Map<Class<?>, U> getResourceRegistries() {
        return this.resourceRegistries;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setResourceRegistries(U ... resourceRegistries) {
        this.resourceRegistries.clear();

        Stream.of(resourceRegistries).forEach(resourceRegistry -> this.resourceRegistries.put(resourceRegistry.getBeanClass(), resourceRegistry));
    }

    @Override
    public Map<Class<?>, V> getSearchServices() {
        return this.searchServices;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setSearchServices(V ... searchServices) {
        this.searchServices.clear();

        Stream.of(searchServices).forEach(searchService -> this.searchServices.put(searchService.getBeanClass(), searchService));
    }
}
