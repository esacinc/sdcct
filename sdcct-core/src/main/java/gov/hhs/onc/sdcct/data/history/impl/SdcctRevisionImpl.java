package gov.hhs.onc.sdcct.data.history.impl;

import gov.hhs.onc.sdcct.data.history.SdcctRevision;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractSdcctEntity;
import java.util.Date;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity(name = "revision")
@RevisionEntity
@Table(name = DbTableNames.REVISION)
public class SdcctRevisionImpl extends AbstractSdcctEntity implements SdcctRevision {
    private final static long serialVersionUID = 0L;

    private Date timestamp;

    @Column(name = DbColumnNames.ID)
    @GeneratedValue(generator = DbSequenceNames.REVISION_ID, strategy = GenerationType.SEQUENCE)
    @Id
    @Nullable
    @Override
    @RevisionNumber
    @SequenceGenerator(allocationSize = 1, name = DbSequenceNames.REVISION_ID, sequenceName = DbSequenceNames.REVISION_ID)
    public Long getId() {
        return super.getId();
    }

    @Column(name = DbColumnNames.TIMESTAMP)
    @Override
    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
