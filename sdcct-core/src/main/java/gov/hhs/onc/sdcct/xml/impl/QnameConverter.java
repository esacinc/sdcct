package gov.hhs.onc.sdcct.xml.impl;

import net.sf.saxon.s9api.QName;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component("convQname")
public class QnameConverter implements Converter<String, QName> {
    @Override
    public QName convert(String src) {
        return QName.fromClarkName(src);
    }
}
