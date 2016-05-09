package gov.hhs.onc.sdcct.net.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component("convInetAddr")
public class InetAddressConverter implements Converter<String, InetAddress> {
    @Override
    public InetAddress convert(String src) {
        try {
            return InetAddress.getByName(src);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
