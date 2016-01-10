package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.fhir.FhirWsQueryParamNames;
import gov.hhs.onc.sdcct.transform.render.RenderOptions;
import gov.hhs.onc.sdcct.transform.render.SdcctRenderer;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
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
import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyWriter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.provider.AbstractConfigurableProvider;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Exchange;
import org.springframework.beans.factory.annotation.Autowired;

@Priority(1)
public class FhirRendererProvider extends AbstractConfigurableProvider implements MessageBodyWriter<Object> {
    @Autowired
    private List<SdcctRenderer> renderers;

    private Map<String, String> defaultQueryParams = new HashMap<>();
    private Map<FhirFormatType, SdcctRenderer> rendererFormatTypes;
    private Map<MediaType, SdcctRenderer> rendererMediaTypes;

    @Override
    public void writeTo(Object obj, Class<?> type, Type genericType, Annotation[] annos, MediaType mediaType, MultivaluedMap<String, Object> headers,
        OutputStream entityStream) throws IOException, WebApplicationException {
        Exchange exchange = JAXRSUtils.getCurrentMessage().getExchange();
        MultivaluedMap<String, String> queryParams = new UriInfoImpl(exchange.getInMessage()).getQueryParameters();
        Map<String, String> mergedQueryParams = new HashMap<>(this.defaultQueryParams);

        queryParams.keySet().stream().forEach(queryParamName -> mergedQueryParams.put(queryParamName, queryParams.getFirst(queryParamName)));

        SdcctRenderer renderer;
        Map<String, Object> renderOpts = new HashMap<>();

        // TODO: handle invalid render option values

        if (mergedQueryParams.containsKey(FhirWsQueryParamNames.FORMAT)) {
            String formatValue = mergedQueryParams.get(FhirWsQueryParamNames.FORMAT);

            // noinspection ConstantConditions
            renderer =
                rendererFormatTypes.get(SdcctEnumUtils
                    .findByPredicate(FhirFormatType.class, formatTypeItem -> formatTypeItem.getValues().contains(formatValue)));
        } else {
            renderer = this.rendererMediaTypes.keySet().stream().filter(mediaType::isCompatible).map(this.rendererMediaTypes::get).findFirst().get();
        }

        if (mergedQueryParams.containsKey(FhirWsQueryParamNames.PRETTY)) {
            renderOpts.put(RenderOptions.PRETTY_NAME, BooleanUtils.toBoolean(mergedQueryParams.get(FhirWsQueryParamNames.PRETTY)));
        }

        try {
            entityStream.write(renderer.render(obj, renderOpts));
        } catch (Exception e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annos, MediaType mediaType) {
        return this.rendererMediaTypes.keySet().stream().anyMatch(mediaType::isCompatible);
    }

    @Override
    public long getSize(Object obj, Class<?> type, Type genericType, Annotation[] annos, MediaType mediaType) {
        return -1;
    }

    @Override
    public void init(List<ClassResourceInfo> classResourceInfos) {
        this.setProduceMediaTypes((this.rendererFormatTypes =
            (this.renderers.stream().collect(SdcctStreamUtils.toMap(SdcctRenderer::getType, Function.identity(),
                () -> new LinkedHashMap<>(this.renderers.size()))))
                .entrySet()
                .stream()
                .collect(
                    SdcctStreamUtils.toMap(rendererTypeEntry -> SdcctEnumUtils.findByPredicate(FhirFormatType.class,
                        formatTypeItem -> (formatTypeItem.getRenderType() == rendererTypeEntry.getKey())), Entry::getValue, () -> new LinkedHashMap<>(
                        this.renderers.size())))).keySet().stream().map(formatType -> formatType.getMediaType().toString()).collect(Collectors.toList()));

        this.rendererMediaTypes =
            this.rendererFormatTypes
                .entrySet()
                .stream()
                .collect(
                    SdcctStreamUtils.toMap(rendererFormatTypeEntry -> MediaType.valueOf(rendererFormatTypeEntry.getKey().getMediaType().toString()),
                        Entry::getValue, () -> new LinkedHashMap<>(this.renderers.size())));
    }

    public Map<String, String> getDefaultQueryParameters() {
        return this.defaultQueryParams;
    }

    public void setDefaultQueryParameters(Map<String, String> defaultQueryParams) {
        this.defaultQueryParams.clear();
        this.defaultQueryParams.putAll(defaultQueryParams);
    }
}
