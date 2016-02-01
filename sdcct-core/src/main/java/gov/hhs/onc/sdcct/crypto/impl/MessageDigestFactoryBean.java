package gov.hhs.onc.sdcct.crypto.impl;

import java.security.MessageDigest;

public class MessageDigestFactoryBean extends AbstractCryptoFactoryBean<MessageDigest> {
    public MessageDigestFactoryBean() {
        super(MessageDigest.class);
    }

    @Override
    public MessageDigest getObject() throws Exception {
        return MessageDigest.getInstance(this.type, this.prov);
    }
}
