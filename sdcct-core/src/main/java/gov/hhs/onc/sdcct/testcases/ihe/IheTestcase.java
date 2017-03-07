package gov.hhs.onc.sdcct.testcases.ihe;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsResponseType;
import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import java.util.List;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

public interface IheTestcase extends SdcctTestcase<IheTestcaseDescription> {
    @JsonProperty
    public RfdWsResponseType getContentType();

    public void setContentType(RfdWsResponseType contentType);

    @JsonProperty
    public List<String> getFormIds();

    public void setFormIds(@Nullable List<String> formIds);

    @JsonProperty
    @Nullable
    public IheTestcaseRequestInfo getRequestInfo();

    public void setRequestInfo(@Nullable IheTestcaseRequestInfo requestInfo);

    @JsonProperty
    @Nullable
    public IheTestcaseResponseInfo getResponseInfo();

    public void setResponseInfo(@Nullable IheTestcaseResponseInfo responseInfo);

    @JsonProperty
    public QName getTransaction();

    public void setTransaction(QName transaction);
}
