package gov.hhs.onc.sdcct.data.db;

public final class DbFieldNames {
    public final static String CONTENT_PREFIX = "content_";
    public final static String TEXT_PREFIX = "text_";

    public final static String CONTENT_EDGE_NGRAM = CONTENT_PREFIX + DbAnalyzerNames.EDGE_NGRAM;
    public final static String CONTENT_LOWERCASE = CONTENT_PREFIX + DbAnalyzerNames.LOWERCASE;
    public final static String CONTENT_NGRAM = CONTENT_PREFIX + DbAnalyzerNames.NGRAM;
    public final static String CONTENT_PHONETIC = CONTENT_PREFIX + DbAnalyzerNames.PHONETIC;

    public final static String ID = "id";

    public final static String TEXT_EDGE_NGRAM = TEXT_PREFIX + DbAnalyzerNames.EDGE_NGRAM;
    public final static String TEXT_LOWERCASE = TEXT_PREFIX + DbAnalyzerNames.LOWERCASE;
    public final static String TEXT_NGRAM = TEXT_PREFIX + DbAnalyzerNames.NGRAM;
    public final static String TEXT_PHONETIC = TEXT_PREFIX + DbAnalyzerNames.PHONETIC;

    private DbFieldNames() {
    }
}
