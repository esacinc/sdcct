package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.SdcctCriterion;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.data.metadata.EntityMetadata;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractSdcctRegistry<T, U extends SdcctResource> extends AbstractSdcctDataAccessor<T, U> implements SdcctRegistry<T, U> {
    @Autowired
    protected SdcctRepository<U> repo;

    @Autowired
    protected XmlCodec xmlCodec;

    protected Supplier<U> entityBuilder;

    protected AbstractSdcctRegistry(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass,
        Supplier<U> entityBuilder) {
        super(beanClass, beanImplClass, entityClass, entityImplClass);

        this.entityBuilder = entityBuilder;
    }

    @Override
    @Transactional
    public boolean remove(T bean) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public boolean removeById(long id) throws Exception {
        return this.repo.removeById(id);
    }

    @Nonnegative
    @Override
    @Transactional
    public long remove(SdcctCriteria<U> criteria) throws Exception {
        return this.repo.remove(criteria);
    }

    @Nonnegative
    @Transactional
    public long save(T bean) throws Exception {
        return this.repo.save(this.encode(bean));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(SdcctCriteria<U> criteria) throws Exception {
        List<U> entities = this.repo.findAll(criteria);
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
    @Transactional(readOnly = true)
    public T findById(long id) throws Exception {
        return this.decode(this.repo.findById(id));
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public T find(SdcctCriteria<U> criteria) throws Exception {
        return this.decode(this.repo.find(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(T bean) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(long id) throws Exception {
        return this.repo.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(SdcctCriteria<U> criteria) throws Exception {
        return this.repo.exists(criteria);
    }

    @Nonnegative
    @Override
    @Transactional(readOnly = true)
    public long count(SdcctCriteria<U> criteria) throws Exception {
        return this.repo.count(criteria);
    }

    @Override
    @Transactional
    public void reindex() throws Exception {
        this.repo.reindex();
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<U> buildCriteria(SdcctCriterion<U> ... criterions) {
        return this.repo.buildCriteria(criterions);
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
        return this.repo.getEntityMetadata();
    }
}
