package gov.hhs.onc.sdcct.testcases.results;

import gov.hhs.onc.sdcct.beans.ResultBean;
import gov.hhs.onc.sdcct.net.http.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import javax.annotation.Nullable;

public interface SdcctTestcaseResult<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>>
    extends ResultBean {
    public V getSubmission();

    public boolean hasHttpRequestEvent();

    @Nullable
    public HttpRequestEvent getHttpRequestEvent();

    public void setHttpRequestEvent(@Nullable HttpRequestEvent httpRequestEvent);

    public boolean hasHttpResponseEvent();

    @Nullable
    public HttpResponseEvent getHttpResponseEvent();

    public void setHttpResponseEvent(@Nullable HttpResponseEvent httpResponseEvent);

    public boolean hasWsRequestEvent();

    @Nullable
    public WsRequestEvent getWsRequestEvent();

    public void setWsRequestEvent(@Nullable WsRequestEvent wsRequestEvent);

    public boolean hasWsResponseEvent();

    @Nullable
    public WsResponseEvent getWsResponseEvent();

    public void setWsResponseEvent(@Nullable WsResponseEvent wsResponseEvent);
}
