package gov.hhs.onc.sdcct.net.convert.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.core.convert.converter.Converter;

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
