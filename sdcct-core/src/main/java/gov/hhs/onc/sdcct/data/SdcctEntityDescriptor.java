package gov.hhs.onc.sdcct.data;

public interface SdcctEntityDescriptor<T extends SdcctEntity> {
    public Class<T> getEntityClass();

    public Class<? extends T> getEntityImplClass();
}
