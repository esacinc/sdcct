package gov.hhs.onc.sdcct.data;

public interface SdcctResourceDescriptor<T> {
    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();
}
