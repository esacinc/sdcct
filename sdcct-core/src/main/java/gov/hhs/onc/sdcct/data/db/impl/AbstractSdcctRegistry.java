package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
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
        return dataService.remove(this.encode(bean));
    }

    @Override
    public boolean removeByNaturalId(Serializable naturalId) throws Exception {
        return dataService.removeByNaturalId(naturalId);
    }

    @Override
    public boolean removeById(Serializable id) throws Exception {
        return dataService.removeById(id);
    }

    @Nonnegative
    @Override
    public long remove(SdcctCriteria criteria) throws Exception {
        return dataService.remove(criteria);
    }

    @Nonnegative
    public long save(T bean) throws Exception {
        U entity = this.encode(bean);
        long entityId = this.dataService.save(entity);

        entity.setEntityId(entityId);

        return this.dataService.save(this.buildSearchParams(bean, entity, entityId));
    }

    @Override
    public List<T> findAll(SdcctCriteria criteria) throws Exception {
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
        return this.decode(dataService.findByNaturalId(naturalId));
    }

    @Nullable
    @Override
    public T findById(Serializable id) throws Exception {
        return this.decode(dataService.findById(id));
    }

    @Nullable
    @Override
    public T find(SdcctCriteria criteria) throws Exception {
        return this.decode(dataService.find(criteria));
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
    public boolean exists(SdcctCriteria criteria) throws Exception {
        return this.dataService.exists(criteria);
    }

    @Nonnegative
    @Override
    public long count(SdcctCriteria criteria) throws Exception {
        return this.dataService.count(criteria);
    }

    @Override
    public void reindex() throws Exception {
        this.dataService.reindex();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reindex();
    }

    protected U buildSearchParams(T bean, U entity, long entityId) throws Exception {
        entity.getCoordSearchParams().clear();
        entity.getDateSearchParams().clear();
        entity.getNumberSearchParams().clear();
        entity.getQuantitySearchParams().clear();
        entity.getStringSearchParams().clear();
        entity.getTokenSearchParams().clear();
        entity.getUriSearchParams().clear();

        return entity;
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
}
