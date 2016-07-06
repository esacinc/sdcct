package gov.hhs.onc.sdcct.transform.content.path.impl;

import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.bind.v2.model.runtime.RuntimeClassInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeElementInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeLeafInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfoSet;
import gov.hhs.onc.sdcct.transform.content.path.AttributePathSegment;
import gov.hhs.onc.sdcct.transform.content.path.ContentPath;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import gov.hhs.onc.sdcct.transform.content.path.ElementPathSegment;
import gov.hhs.onc.sdcct.utils.SdcctClassUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSimpleTypeMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBException;
import net.sf.saxon.om.AxisInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.pattern.SameNameTest;
import net.sf.saxon.tree.iter.AxisIterator;
import net.sf.saxon.type.Type;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentPathBuilderImpl implements ContentPathBuilder {
    @Autowired
    private JaxbContextRepository jaxbContextRepo;

    private ClassLoader beanClassLoader;
    private BidiMap<String, String> namespaces = new DualTreeBidiMap<>();

    @Override
    public ContentPath build(boolean typed, NodeInfo nodeInfo) throws Exception {
        return this.build(typed, buildSegments(new LinkedList<>(), nodeInfo, nodeInfo.getPrefix(), nodeInfo.getURI(), nodeInfo.getLocalPart()));
    }

    @Override
    public ContentPath build(boolean typed, LinkedList<ContentPathSegment<?, ?>> segments) throws Exception {
        return new ContentPathImpl(this.namespaces, (typed ? this.buildSegmentTypes(segments) : segments), buildFluentPathExpression(segments),
            buildJsonPointerExpression(segments), this.buildXpathExpression(segments));
    }

    private static LinkedList<ContentPathSegment<?, ?>> buildSegments(LinkedList<ContentPathSegment<?, ?>> segments, NodeInfo nodeInfo,
        @Nullable String nsPrefix, @Nullable String nsUri, String localName) {
        boolean attrNode = (nodeInfo.getNodeKind() == Type.ATTRIBUTE);

        if (attrNode) {
            segments.add(0, new AttributePathSegmentImpl(nsPrefix, nsUri, localName));
        }

        ElementPathSegment elemSegment = new ElementPathSegmentImpl(nsPrefix, nsUri, localName);
        segments.add(0, elemSegment);

        NodeInfo parentNodeInfo = nodeInfo.getParent();

        if ((parentNodeInfo != null) && (parentNodeInfo.getNodeKind() != Type.DOCUMENT)) {
            if (!attrNode) {
                int nodeIndex = -1;
                SameNameTest siblingNodeTest = new SameNameTest(nodeInfo);
                AxisIterator precedingSiblingNodeIterator = nodeInfo.iterateAxis(AxisInfo.PRECEDING_SIBLING, siblingNodeTest);

                while (precedingSiblingNodeIterator.next() != null) {
                    nodeIndex++;
                }

                if (nodeIndex == -1) {
                    if (nodeInfo.iterateAxis(AxisInfo.FOLLOWING_SIBLING, siblingNodeTest).next() != null) {
                        nodeIndex = 0;
                    }
                } else {
                    nodeIndex++;
                }

                if (nodeIndex >= 0) {
                    elemSegment.setIndex(nodeIndex);
                }
            }

            buildSegments(segments, parentNodeInfo, parentNodeInfo.getPrefix(), parentNodeInfo.getURI(), parentNodeInfo.getLocalPart());
        }

        return segments;
    }

    private static String buildFluentPathExpression(LinkedList<ContentPathSegment<?, ?>> segments) {
        StrBuilder builder = new StrBuilder();
        ElementPathSegment elemSegment;

        for (ContentPathSegment<?, ?> segment : segments) {
            builder.appendSeparator(SdcctStringUtils.PERIOD_CHAR);
            builder.append(segment.getLocalName());

            if ((segment instanceof ElementPathSegment) && (elemSegment = ((ElementPathSegment) segment)).hasIndex()) {
                builder.append(SdcctStringUtils.L_BRACKET_CHAR);
                builder.append(elemSegment.getIndex());
                builder.append(SdcctStringUtils.R_BRACKET_CHAR);
            }
        }

        return builder.build();
    }

    private static String buildJsonPointerExpression(LinkedList<ContentPathSegment<?, ?>> segments) {
        StrBuilder builder = new StrBuilder();
        ElementPathSegment elemSegment;

        for (ContentPathSegment<?, ?> segment : segments) {
            builder.append(SdcctStringUtils.SLASH_CHAR);
            builder.append(segment.getLocalName());

            if ((segment instanceof ElementPathSegment) && (elemSegment = ((ElementPathSegment) segment)).hasIndex()) {
                builder.append(SdcctStringUtils.SLASH_CHAR);
                builder.append(elemSegment.getIndex());
            }
        }

        return builder.build();
    }

    private LinkedList<ContentPathSegment<?, ?>> buildSegmentTypes(LinkedList<ContentPathSegment<?, ?>> segments) throws Exception {
        String segmentLocalName;
        JaxbContextMetadata jaxbContextMetadata = null;
        RuntimeTypeInfoSet jaxbTypeInfoSet = null;
        RuntimeClassInfo jaxbClassInfo = null;
        JaxbComplexTypeMetadata<?> jaxbElemTypeMetadata;
        RuntimeNonElement jaxbPropTypeInfo;

        for (ContentPathSegment<?, ?> segment : segments) {
            segmentLocalName = segment.getLocalName();

            if (jaxbContextMetadata == null) {
                for (RuntimeElementInfo jaxbElemInfoItem : (jaxbTypeInfoSet =
                    (jaxbContextMetadata = this.jaxbContextRepo.findContextMetadata(segment.getNamespaceUri())).getContext().getRuntimeTypeInfoSet())
                        .getAllElements()) {
                    if (jaxbElemInfoItem.getElementName().getLocalPart().equals(segmentLocalName)) {
                        ((ElementPathSegment) segment).setJaxbTypeMetadata((jaxbElemTypeMetadata = ((JaxbComplexTypeMetadata<?>) this.jaxbContextRepo
                            .findTypeMetadata((jaxbClassInfo = ((RuntimeClassInfo) jaxbElemInfoItem.getContentType())).getClazz()))));
                        segment.setBeanClass(jaxbElemTypeMetadata.getBeanImplClass());

                        break;
                    }
                }
            } else {
                // noinspection ConstantConditions
                jaxbPropTypeInfo = this.buildSegmentJaxbPropertyTypeInfo(jaxbTypeInfoSet, segments, segment,
                    jaxbClassInfo.getProperty(StringUtils.uncapitalize(JAXBRIContext.mangleNameToPropertyName(segmentLocalName))));

                if (segment instanceof ElementPathSegment) {
                    jaxbClassInfo = ((RuntimeClassInfo) jaxbPropTypeInfo);
                }
            }
        }

        return segments;
    }

    private RuntimeNonElement buildSegmentJaxbPropertyTypeInfo(RuntimeTypeInfoSet jaxbTypeInfoSet, LinkedList<ContentPathSegment<?, ?>> segments,
        ContentPathSegment<?, ?> segment, RuntimePropertyInfo jaxbPropInfo) throws Exception {
        Class<?> jaxbPropTypeClass = ((Class<?>) jaxbPropInfo.getIndividualType());
        boolean jaxbLeafProp = (jaxbPropInfo instanceof RuntimeLeafInfo);

        if (!jaxbLeafProp && !StringUtils.endsWith(jaxbPropTypeClass.getSimpleName(), SdcctClassUtils.IMPL_CLASS_NAME_SUFFIX)) {
            try {
                jaxbPropTypeClass = SdcctClassUtils.buildImplClass(this.beanClassLoader, Object.class, jaxbPropTypeClass);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(
                    String.format("Unable to build content path (xpath=%s) segment (qname=%s) JAXB property type (class=%s) implementation class.",
                        buildXpathExpression(segments), segment.getQname(), jaxbPropTypeClass.getName()),
                    e);
            }
        }

        RuntimeNonElement jaxbPropTypeInfo = jaxbTypeInfoSet.getClassInfo(jaxbPropTypeClass);

        if (jaxbPropTypeInfo == null) {
            throw new IllegalStateException(String.format("Unable to build content path (xpath=%s) segment (qname=%s) JAXB property type (class=%s) info.",
                buildXpathExpression(segments), segment.getQname(), jaxbPropTypeClass.getName()));
        }

        if (jaxbLeafProp) {
            segment.setBeanClass(((Class<?>) jaxbPropTypeInfo.getType()));
        } else {
            try {
                JaxbTypeMetadata<?, ?> jaxbPropTypeMetadata = this.jaxbContextRepo.findTypeMetadata(jaxbPropTypeClass);

                if (segment instanceof AttributePathSegment) {
                    ((AttributePathSegment) segment).setJaxbTypeMetadata(((JaxbSimpleTypeMetadata<?>) jaxbPropTypeMetadata));
                } else {
                    ((ElementPathSegment) segment).setJaxbTypeMetadata(((JaxbComplexTypeMetadata<?>) jaxbPropTypeMetadata));
                }

                segment.setBeanClass(jaxbPropTypeMetadata.getBeanImplClass());
            } catch (JAXBException e) {
                throw new IllegalStateException(
                    String.format("Unable to find content path (xpath=%s) segment (qname=%s) JAXB property type (class=%s) metadata.",
                        buildXpathExpression(segments), segment.getQname(), jaxbPropTypeClass.getName()),
                    e);
            }
        }

        return jaxbPropTypeInfo;
    }

    private String buildXpathExpression(LinkedList<ContentPathSegment<?, ?>> segments) {
        StrBuilder builder = new StrBuilder();
        String nsPrefix, nsUri;
        boolean attrSegmentItem;
        ElementPathSegment elemSegment;

        for (ContentPathSegment<?, ?> segment : segments) {
            builder.append(SdcctStringUtils.SLASH_CHAR);

            if ((attrSegmentItem = (segment instanceof AttributePathSegment))) {
                builder.append(SdcctStringUtils.AT_CHAR);
            }

            if (!(nsPrefix = (namespaces.containsValue((nsUri = segment.getNamespaceUri())) ? namespaces.getKey(nsUri) : segment.getNamespacePrefix()))
                .isEmpty()) {
                builder.append(nsPrefix);
                builder.append(SdcctStringUtils.COLON_CHAR);
            }

            builder.append(segment.getLocalName());

            if (!attrSegmentItem && (elemSegment = ((ElementPathSegment) segment)).hasIndex()) {
                builder.append(SdcctStringUtils.L_BRACKET_CHAR);
                // noinspection ConstantConditions
                builder.append((elemSegment.getIndex() + 1));
                builder.append(SdcctStringUtils.R_BRACKET_CHAR);
            }
        }

        return builder.build();
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public BidiMap<String, String> getNamespaces() {
        return this.namespaces;
    }

    @Override
    public void setNamespaces(Map<String, String> namespaces) {
        this.namespaces.clear();
        this.namespaces.putAll(namespaces);
    }
}
