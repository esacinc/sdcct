package gov.hhs.onc.sdcct.ws;

public interface WsClient<T, U> {
    public U buildInvocationDelegate();

    public T getDelegate();
}
