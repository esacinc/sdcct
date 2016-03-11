package gov.hhs.onc.sdcct.fhir.ws.impl;

import gov.hhs.onc.sdcct.config.utils.SdcctOptionUtils;
import gov.hhs.onc.sdcct.fhir.FhirException;
import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.fhir.IssueTypeList;
import gov.hhs.onc.sdcct.fhir.ws.FhirWsQueryParamNames;
import gov.hhs.onc.sdcct.io.utils.SdcctMediaTypeUtils;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.impl.ContentEncodeOptions;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.io.IOException;
import java.io.InputStream;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.provider.AbstractConfigurableProvider;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Priority(2)
public class FhirContentProvider<T> extends AbstractConfigurableProvider implements MessageBodyReader<T>, MessageBodyWriter<T> {
    @Autowired
    private List<ContentCodec> codecs;

    private Map<String, String> defaultQueryParams = new HashMap<>();
    private Map<FhirFormatType, ContentCodec> formatCodecs;
    private Map<String, FhirFormatType> formatQueryParamValues;

    @Override
    public void writeTo(T obj, Class<?> type, Type genericType, Annotation[] annos, javax.ws.rs.core.MediaType mediaType,
        MultivaluedMap<String, Object> headers, OutputStream entityStream) throws IOException, WebApplicationException {
        Message msg = JAXRSUtils.getCurrentMessage();
        MultivaluedMap<String, String> queryParams = new UriInfoImpl(msg.getExchange().getInMessage()).getQueryParameters();
        Map<String, String> mergedQueryParams = new HashMap<>(this.defaultQueryParams);

        queryParams.keySet().stream().forEach(queryParamName -> mergedQueryParams.put(queryParamName, queryParams.getFirst(queryParamName)));

        ContentEncodeOptions encodeOpts = new ContentEncodeOptions();
        FhirFormatType format;

        if (mergedQueryParams.containsKey(FhirWsQueryParamNames.FORMAT)) {
            String formatQueryParamValue = mergedQueryParams.get(FhirWsQueryParamNames.FORMAT);

            if (!this.formatQueryParamValues.containsKey(formatQueryParamValue)) {
                throw new FhirException(
                    String.format("Invalid format query parameter (name=%s) value: %s", FhirWsQueryParamNames.FORMAT, formatQueryParamValue))
                    .setIssueType(IssueTypeList.PROCESSING).setIssueDetailConceptParts("MSG_PARAM_INVALID", FhirWsQueryParamNames.FORMAT)
                    .setResponseStatus(Status.BAD_REQUEST);
            }

            format = this.formatQueryParamValues.get(formatQueryParamValue);
        } else {
            format = SdcctMediaTypeUtils.findCompatible(FhirFormatType.class, MediaType.valueOf(mediaType.toString()));
        }

        // noinspection ConstantConditions
        MediaType formatEncMediaType = format.getEncodedMediaType();
        String formatEncMediaTypeValue = formatEncMediaType.toString();

        headers.putSingle(HttpHeaders.CONTENT_TYPE, formatEncMediaTypeValue);

        msg.put(Message.CONTENT_TYPE, formatEncMediaTypeValue);
        msg.put(Message.ENCODING, formatEncMediaType.getCharSet().name());

        ContentCodec codec = this.formatCodecs.get(format);

        msg.put(ContentCodec.class, codec);

        if (mergedQueryParams.containsKey(FhirWsQueryParamNames.PRETTY)) {
            String prettyQueryParamValue = mergedQueryParams.get(FhirWsQueryParamNames.PRETTY);

            try {
                encodeOpts.setOption(ContentEncodeOptions.PRETTY, SdcctOptionUtils.getBooleanValue(FhirWsQueryParamNames.PRETTY, prettyQueryParamValue));
            } catch (IllegalArgumentException e) {
                throw new FhirException(
                    String.format("Invalid pretty query parameter (name=%s) value: %s", FhirWsQueryParamNames.PRETTY, prettyQueryParamValue), e)
                    .setIssueType(IssueTypeList.PROCESSING).setIssueDetailConceptParts("MSG_PARAM_INVALID", FhirWsQueryParamNames.PRETTY)
                    .setResponseStatus(Status.BAD_REQUEST);
            }
        }

        try {
            entityStream.write(codec.encode(obj, encodeOpts));

            entityStream.close();
        } catch (Exception e) {
            throw new FhirException(String.format("Unable to encode (mediaType=%s) content object (class=%s).", formatEncMediaTypeValue, obj.getClass()
                .getName()), e).setIssueType(IssueTypeList.STRUCTURE);
        }
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annos, javax.ws.rs.core.MediaType mediaType, MultivaluedMap<String, String> headers,
        InputStream entityStream) throws IOException, WebApplicationException {
        ContentCodec codec = this.formatCodecs.get(SdcctMediaTypeUtils.findCompatible(FhirFormatType.class, MediaType.valueOf(mediaType.toString())));

        // JAXRSUtils.getCurrentMessage().put(ContentCodec.class, codec);

        try {
            byte[] entityBytes = IOUtils.toByteArray(entityStream);

            entityStream.close();

            return (type.equals(byte[].class) ? type.cast(entityBytes) : codec.decode(entityBytes, type, null));
        } catch (Exception e) {
            throw new FhirException(String.format("Unable to decode (mediaType=%s) content object (class=%s).", mediaType, type.getName()), e)
                .setIssueType(IssueTypeList.STRUCTURE);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annos, javax.ws.rs.core.MediaType mediaType) {
        return this.isCompatible(mediaType);
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annos, javax.ws.rs.core.MediaType mediaType) {
        return this.isCompatible(mediaType);
    }

    public boolean isCompatible(javax.ws.rs.core.MediaType mediaType) {
        return SdcctMediaTypeUtils.isCompatible(FhirFormatType.class, MediaType.valueOf(mediaType.toString()));
    }

    @Override
    public long getSize(T obj, Class<?> type, Type genericType, Annotation[] annos, javax.ws.rs.core.MediaType mediaType) {
        return -1;
    }

    @Override
    public void init(List<ClassResourceInfo> classResourceInfos) {
        this.setProduceMediaTypes((this.formatCodecs =
            (this.codecs.stream().collect(SdcctStreamUtils.toMap(ContentCodec::getType, Function.identity(), () -> new LinkedHashMap<>(this.codecs.size()))))
                .entrySet()
                .stream()
                .collect(
                    SdcctStreamUtils.toMap(contentTypeEntry -> SdcctEnumUtils.findByPredicate(FhirFormatType.class,
                        formatTypeItem -> (formatTypeItem.getContentType() == contentTypeEntry.getKey())), Entry::getValue, () -> new LinkedHashMap<>(
                        this.codecs.size())))).keySet().stream().map(formatType -> formatType.getEncodedMediaType().toString()).collect(Collectors.toList()));

        FhirFormatType[] formats = FhirFormatType.values();

        this.formatQueryParamValues = new LinkedHashMap<>();

        Stream.of(formats).forEach(
            format -> format.getQueryParamValues().forEach(formatQueryParamValue -> this.formatQueryParamValues.put(formatQueryParamValue, format)));
    }

    public Map<String, String> getDefaultQueryParameters() {
        return this.defaultQueryParams;
    }

    public void setDefaultQueryParameters(Map<String, String> defaultQueryParams) {
        this.defaultQueryParams.clear();
        this.defaultQueryParams.putAll(defaultQueryParams);
    }
}
