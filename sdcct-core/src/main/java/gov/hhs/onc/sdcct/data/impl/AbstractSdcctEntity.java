package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbAnalyzerNames;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.phonetic.PhoneticFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.Proxy;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

@Access(AccessType.PROPERTY)
@AnalyzerDefs({
    @AnalyzerDef(filters = {
        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
        @TokenFilterDef(factory = StopFilterFactory.class),
        @TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = { @Parameter(name = "maxGramSize", value = "100"),
            @Parameter(name = "minGramSize", value = "3") }) }, name = DbAnalyzerNames.EDGE_NGRAM, tokenizer = @TokenizerDef(
        factory = StandardTokenizerFactory.class)),
    @AnalyzerDef(filters = { @TokenFilterDef(factory = LowerCaseFilterFactory.class) }, name = DbAnalyzerNames.LOWERCASE, tokenizer = @TokenizerDef(
        factory = StandardTokenizerFactory.class)),
    @AnalyzerDef(filters = {
        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
        @TokenFilterDef(factory = NGramFilterFactory.class, params = { @Parameter(name = "maxGramSize", value = "100"),
            @Parameter(name = "minGramSize", value = "3") }) }, name = DbAnalyzerNames.NGRAM, tokenizer = @TokenizerDef(
        factory = StandardTokenizerFactory.class)),
    @AnalyzerDef(filters = { @TokenFilterDef(factory = StandardFilterFactory.class), @TokenFilterDef(factory = StopFilterFactory.class),
        @TokenFilterDef(factory = PhoneticFilterFactory.class, params = { @Parameter(name = "encoder", value = "DoubleMetaphone") }),
        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = { @Parameter(name = "language", value = "English") }) },
        name = DbAnalyzerNames.PHONETIC, tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class)) })
@MappedSuperclass
@Proxy(lazy = false)
public abstract class AbstractSdcctEntity implements SdcctEntity {
    protected Long id;

    private final static long serialVersionUID = 0L;

    @Override
    @Transient
    public boolean hasId() {
        return (this.id != null);
    }

    @Nullable
    @Override
    @Transient
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(@Nullable Long id) {
        this.id = id;
    }
}
