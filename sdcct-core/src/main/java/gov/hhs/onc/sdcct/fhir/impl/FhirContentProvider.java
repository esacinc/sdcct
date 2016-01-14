package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.FhirException;
import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.fhir.FhirWsQueryParamNames;
import gov.hhs.onc.sdcct.fhir.IssueCodeType;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.ContentEncodeOptions;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.utils.SdcctOptionUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyWriter;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.provider.AbstractConfigurableProvider;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@Priority(1)
public class FhirContentProvider extends AbstractConfigurableProvider implements MessageBodyWriter<Object> {
    @Autowired
    private List<ContentCodec> codecs;

    private Map<String, String> defaultQueryParams = new HashMap<>();
    private Map<FhirFormatType, ContentCodec> formatCodecs;
    private Map<MediaType, FhirFormatType> formatMediaTypes;
    private Map<String, FhirFormatType> formatQueryParamValues;

    @Override
    public void writeTo(Object obj, Class<?> type, Type genericType, Annotation[] annos, MediaType mediaType, MultivaluedMap<String, Object> headers,
        OutputStream entityStream) throws IOException, WebApplicationException {
        Message msg = JAXRSUtils.getCurrentMessage();
        MultivaluedMap<String, String> queryParams = new UriInfoImpl(msg.getExchange().getInMessage()).getQueryParameters();
        Map<String, String> mergedQueryParams = new HashMap<>(this.defaultQueryParams);

        queryParams.keySet().stream().forEach(queryParamName -> mergedQueryParams.put(queryParamName, queryParams.getFirst(queryParamName)));

        Map<String, Object> encodeOpts = new HashMap<>();
        FhirFormatType format;

        if (mergedQueryParams.containsKey(FhirWsQueryParamNames.FORMAT)) {
            String formatQueryParamValue = mergedQueryParams.get(FhirWsQueryParamNames.FORMAT);

            if (!this.formatQueryParamValues.containsKey(formatQueryParamValue)) {
                throw new FhirException(
                    String.format("Invalid format query parameter (name=%s) value: %s", FhirWsQueryParamNames.FORMAT, formatQueryParamValue))
                        .setIssueCodeType(IssueCodeType.PROCESSING).setRespStatus(Status.BAD_REQUEST);
            }

            format = this.formatQueryParamValues.get(formatQueryParamValue);
        } else {
            format = this.formatMediaTypes.keySet().stream().filter(mediaType::isCompatible).map(this.formatMediaTypes::get).findFirst().get();
        }

        org.springframework.http.MediaType formatEncMediaType = format.getEncodedMediaType();
        String formatEncMediaTypeValue = formatEncMediaType.toString();

        headers.putSingle(HttpHeaders.CONTENT_TYPE, formatEncMediaTypeValue);

        msg.put(Message.CONTENT_TYPE, formatEncMediaTypeValue);
        msg.put(Message.ENCODING, formatEncMediaType.getCharSet().name());

        ContentCodec codec = this.formatCodecs.get(format);

        if (mergedQueryParams.containsKey(FhirWsQueryParamNames.PRETTY)) {
            String prettyQueryParamValue = mergedQueryParams.get(FhirWsQueryParamNames.PRETTY);

            try {
                encodeOpts.put(ContentEncodeOptions.PRETTY_NAME, SdcctOptionUtils.getBooleanValue(FhirWsQueryParamNames.PRETTY, prettyQueryParamValue));
            } catch (IllegalArgumentException e) {
                throw new FhirException(
                    String.format("Invalid pretty query parameter (name=%s) value: %s", FhirWsQueryParamNames.PRETTY, prettyQueryParamValue), e)
                        .setIssueCodeType(IssueCodeType.PROCESSING).setRespStatus(Status.BAD_REQUEST);
            }
        }

        try {
            entityStream.write(codec.encode(obj, encodeOpts));
        } catch (Exception e) {
            throw new FhirException(
                String.format("Unable to encode (mediaType=%s) content object (class=%s).", formatEncMediaTypeValue, obj.getClass().getName()))
                    .setIssueCodeType(IssueCodeType.STRUCTURE);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annos, MediaType mediaType) {
        return this.formatMediaTypes.keySet().stream().anyMatch(mediaType::isCompatible);
    }

    @Override
    public long getSize(Object obj, Class<?> type, Type genericType, Annotation[] annos, MediaType mediaType) {
        return -1;
    }

    @Override
    public void init(List<ClassResourceInfo> classResourceInfos) {
        this.setProduceMediaTypes((this.formatCodecs =
            (this.codecs.stream().collect(SdcctStreamUtils.toMap(ContentCodec::getType, Function.identity(), () -> new LinkedHashMap<>(this.codecs.size()))))
                .entrySet().stream()
                .collect(SdcctStreamUtils.toMap(
                    contentTypeEntry -> SdcctEnumUtils.findByPredicate(FhirFormatType.class,
                        formatTypeItem -> (formatTypeItem.getContentType() == contentTypeEntry.getKey())),
                    Entry::getValue, () -> new LinkedHashMap<>(this.codecs.size())))).keySet().stream()
                        .map(formatType -> formatType.getEncodedMediaType().toString()).collect(Collectors.toList()));

        FhirFormatType[] formats = FhirFormatType.values();

        this.formatMediaTypes = new LinkedHashMap<>(formats.length);
        this.formatQueryParamValues = new LinkedHashMap<>();

        Stream.of(formats).forEach(format -> {
            this.formatMediaTypes.put(MediaType.valueOf(format.getMediaType().toString()), format);

            format.getQueryParamValues().forEach(formatQueryParamValue -> this.formatQueryParamValues.put(formatQueryParamValue, format));
        });
    }

    public Map<String, String> getDefaultQueryParameters() {
        return this.defaultQueryParams;
    }

    public void setDefaultQueryParameters(Map<String, String> defaultQueryParams) {
        this.defaultQueryParams.clear();
        this.defaultQueryParams.putAll(defaultQueryParams);
    }
}
