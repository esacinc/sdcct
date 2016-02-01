package gov.hhs.onc.sdcct.crypto;

import java.security.Provider;
import java.security.Security;
import java.util.stream.Stream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public final class CryptoProviders {
    public final static BouncyCastleProvider BC = new BouncyCastleProvider();

    public final static String SUN_NAME = "SUN";
    public final static Provider SUN = Security.getProvider(SUN_NAME);

    public final static String SUN_EC_NAME = "SunEC";
    public final static Provider SUN_EC = Security.getProvider(SUN_EC_NAME);

    public final static String SUN_JCE_NAME = "SunJCE";
    public final static Provider SUN_JCE = Security.getProvider(SUN_JCE_NAME);

    public final static String SUN_JSSE_NAME = "SunJSSE";
    public final static Provider SUN_JSSE = Security.getProvider(SUN_JSSE_NAME);

    public final static String SUN_RSA_SIGN_NAME = "SunRsaSign";
    public final static Provider SUN_RSA_SIGN = Security.getProvider(SUN_RSA_SIGN_NAME);

    static {
        resetProviders();
    }

    private CryptoProviders() {
    }

    public static void resetProviders() {
        Stream.of(Security.getProviders()).forEach(prov -> Security.removeProvider(prov.getName()));

        Security.insertProviderAt(SUN, 1);
        Security.insertProviderAt(SUN_RSA_SIGN, 2);
        Security.insertProviderAt(SUN_EC, 3);
        Security.insertProviderAt(SUN_JSSE, 4);
        Security.insertProviderAt(SUN_JCE, 5);
        Security.insertProviderAt(BC, 6);
    }
}
