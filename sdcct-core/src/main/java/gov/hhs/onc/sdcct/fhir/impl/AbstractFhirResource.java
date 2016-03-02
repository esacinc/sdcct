package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@MappedSuperclass
public abstract class AbstractFhirResource extends AbstractResourceEntity implements FhirResource {
    protected String text;

    @Override
    public boolean hasText() {
        return (this.text != null);
    }

    @Column(name = DbColumnNames.TEXT)
    @Field(name = DbColumnNames.TEXT, store = Store.YES)
    @Lob
    @Nullable
    @Override
    @SortableField(forField = DbColumnNames.TEXT)
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(@Nullable String text) {
        this.text = text;
    }
}
