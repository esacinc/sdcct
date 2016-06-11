package gov.hhs.onc.sdcct.xml;

import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import java.util.Properties;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ParseOptions;

public interface XmlCodecOptions<T extends XmlCodecOptions<T>> extends ContentCodecOptions<T> {
    public ParseOptions getParseOptions();

    public T addOutputProperty(String outPropName, @Nullable Object outPropValue);

    public boolean hasOutputProperties();

    public Properties getOutputProperties();
}
