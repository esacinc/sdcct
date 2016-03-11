package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import gov.hhs.onc.sdcct.data.search.impl.CoordEntityImpl;
import gov.hhs.onc.sdcct.data.search.impl.CoordSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.DateSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.NumberSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.QuantitySearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.RefSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.StringSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.TokenSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.UriSearchParamImpl;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctRegistry<T, U extends ResourceEntity, V extends SdcctDao<U>, W extends SdcctDataService<U, V>> extends
    AbstractSdcctDataAccessor<T, U> implements SdcctRegistry<T, U, V, W> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected W dataService;

    @Autowired
    protected XmlCodec xmlCodec;

    protected Supplier<U> entityBuilder;

    protected AbstractSdcctRegistry(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass,
        Supplier<U> entityBuilder) {
        super(beanClass, beanImplClass, entityClass, entityImplClass);

        this.entityBuilder = entityBuilder;
    }

    @Override
    public boolean remove(T bean) throws Exception {
        return this.dataService.remove(this.encode(bean));
    }

    @Override
    public boolean removeByNaturalId(Serializable naturalId) throws Exception {
        return this.dataService.removeByNaturalId(naturalId);
    }

    @Override
    public boolean removeById(Serializable id) throws Exception {
        return this.dataService.removeById(id);
    }

    @Nonnegative
    @Override
    public long remove(SdcctCriteria<U> criteria) throws Exception {
        return this.dataService.remove(criteria);
    }

    @Nonnegative
    public long save(T bean) throws Exception {
        U entity = this.encode(bean);
        long entityId = this.dataService.save(entity);

        entity.setEntityId(entityId);

        return this.dataService.save(this.buildSearchParams(bean, entity));
    }

    @Override
    public List<T> findAll(SdcctCriteria<U> criteria) throws Exception {
        List<U> entities = this.dataService.findAll(criteria);
        List<T> beans = new ArrayList<>(entities.size());

        if (!entities.isEmpty()) {
            for (U entity : entities) {
                beans.add(this.decode(entity));
            }
        }

        return beans;
    }

    @Nullable
    @Override
    public T findByNaturalId(Serializable naturalId) throws Exception {
        return this.decode(this.dataService.findByNaturalId(naturalId));
    }

    @Nullable
    @Override
    public T findById(Serializable id) throws Exception {
        return this.decode(this.dataService.findById(id));
    }

    @Nullable
    @Override
    public T find(SdcctCriteria<U> criteria) throws Exception {
        return this.decode(this.dataService.find(criteria));
    }

    @Override
    public boolean exists(T bean) throws Exception {
        return this.dataService.exists(this.encode(bean));
    }

    @Override
    public boolean existsByNaturalId(Serializable naturalId) throws Exception {
        return this.dataService.existsByNaturalId(naturalId);
    }

    @Override
    public boolean existsById(Serializable id) throws Exception {
        return this.dataService.existsById(id);
    }

    @Override
    public boolean exists(SdcctCriteria<U> criteria) throws Exception {
        return this.dataService.exists(criteria);
    }

    @Nonnegative
    @Override
    public long count(SdcctCriteria<U> criteria) throws Exception {
        return this.dataService.count(criteria);
    }

    @Override
    public void reindex() throws Exception {
        this.dataService.reindex();
    }

    @Override
    public SdcctCriteria<U> buildCriteria(Criterion ... criterions) {
        return this.dataService.buildCriteria(criterions);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reindex();
    }

    protected U buildSearchParams(T bean, U entity) throws Exception {
        entity.getCoordSearchParams().clear();
        entity.getDateSearchParams().clear();
        entity.getNumberSearchParams().clear();
        entity.getQuantitySearchParams().clear();
        entity.getRefSearchParams().clear();
        entity.getStringSearchParams().clear();
        entity.getTokenSearchParams().clear();
        entity.getUriSearchParams().clear();

        return entity;
    }

    protected void buildCoordSearchParam(U entity, String name, double latitude, double longitude) {
        entity.addCoordSearchParams(new CoordSearchParamImpl(entity, name, new CoordEntityImpl(latitude, longitude)));
    }

    protected void buildDateSearchParam(U entity, String name, Date value) {
        entity.addDateSearchParams(new DateSearchParamImpl(entity, name, value));
    }

    protected void buildNumberSearchParam(U entity, String name, BigDecimal value) {
        entity.addNumberSearchParams(new NumberSearchParamImpl(entity, name, value));
    }

    protected void buildQuantitySearchParam(U entity, String name, @Nullable URI codeSystemUri, @Nullable String units, BigDecimal value) {
        entity.addQuantitySearchParams(new QuantitySearchParamImpl(entity, name, codeSystemUri, units, value));
    }

    protected void buildRefSearchParam(U entity, String name, String value) {
        entity.addRefSearchParams(new RefSearchParamImpl(entity, name, value));
    }

    protected void buildStringSearchParam(U entity, String name, String value) {
        entity.addStringSearchParams(new StringSearchParamImpl(entity, name, value));
    }

    protected void buildTokenSearchParam(U entity, String name, @Nullable URI codeSystemUri, String value) {
        entity.addTokenSearchParams(new TokenSearchParamImpl(entity, name, codeSystemUri, value));
    }

    protected void buildUriSearchParam(U entity, String name, URI value) {
        entity.addUriSearchParams(new UriSearchParamImpl(entity, name, value));
    }

    protected U encode(T bean) throws Exception {
        U entity = this.entityBuilder.get();
        entity.setContent(new String(this.xmlCodec.encode(bean, null), StandardCharsets.UTF_8));

        return entity;
    }

    @Nullable
    protected T decode(@Nullable U entity) throws Exception {
        return ((entity != null) ? this.xmlCodec.decode(entity.getContent().getBytes(StandardCharsets.UTF_8), this.beanImplClass, null) : null);
    }

    @Override
    public EntityMetadata getEntityMetadata() {
        return this.dataService.getEntityMetadata();
    }
}
