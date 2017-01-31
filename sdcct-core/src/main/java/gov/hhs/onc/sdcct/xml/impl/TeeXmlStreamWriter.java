package gov.hhs.onc.sdcct.xml.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.codehaus.stax2.typed.Base64Variant;
import org.codehaus.stax2.util.StreamWriter2Delegate;

public class TeeXmlStreamWriter extends StreamWriter2Delegate {
    private XMLStreamWriter2 delegate2;

    public TeeXmlStreamWriter(XMLStreamWriter2 delegate1, XMLStreamWriter2 delegate2) {
        super(delegate1);

        this.delegate2 = delegate2;

        this.setParent(delegate1);
    }

    @Override
    public void closeCompletely() throws XMLStreamException {
        try {
            super.closeCompletely();
        } finally {
            this.delegate2.closeCompletely();
        }
    }

    @Override
    public void close() throws XMLStreamException {
        try {
            super.close();
        } finally {
            this.delegate2.close();
        }
    }

    @Override
    public void flush() throws XMLStreamException {
        super.flush();

        this.delegate2.flush();
    }

    @Override
    public void copyEventFromReader(XMLStreamReader2 reader, boolean preserveEventData) throws XMLStreamException {
        super.copyEventFromReader(reader, preserveEventData);

        this.delegate2.copyEventFromReader(reader, preserveEventData);
    }

    @Override
    public void writeEndDocument() throws XMLStreamException {
        super.writeEndDocument();

        this.delegate2.writeEndDocument();
    }

    @Override
    public void writeEndElement() throws XMLStreamException {
        super.writeEndElement();

        this.delegate2.writeEndElement();
    }

    @Override
    public void writeBinary(Base64Variant variant, byte[] value, int offset, int len) throws XMLStreamException {
        super.writeBinary(variant, value, offset, len);

        this.delegate2.writeBinary(variant, value, offset, len);
    }

    @Override
    public void writeBinary(byte[] value, int offset, int len) throws XMLStreamException {
        super.writeBinary(value, offset, len);

        this.delegate2.writeBinary(value, offset, len);
    }

    @Override
    public void writeBinaryAttribute(Base64Variant variant, String nsPrefix, String nsUri, String localName, byte[] value) throws XMLStreamException {
        super.writeBinaryAttribute(variant, nsPrefix, nsUri, localName, value);

        this.delegate2.writeBinaryAttribute(variant, nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeBinaryAttribute(String nsPrefix, String nsUri, String localName, byte[] value) throws XMLStreamException {
        super.writeBinaryAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeBinaryAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeBoolean(boolean value) throws XMLStreamException {
        super.writeBoolean(value);

        this.delegate2.writeBoolean(value);
    }

    @Override
    public void writeBooleanAttribute(String nsPrefix, String nsUri, String localName, boolean value) throws XMLStreamException {
        super.writeBooleanAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeBooleanAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeDecimal(BigDecimal value) throws XMLStreamException {
        super.writeDecimal(value);

        this.delegate2.writeDecimal(value);
    }

    @Override
    public void writeDecimalAttribute(String nsPrefix, String nsUri, String localName, BigDecimal value) throws XMLStreamException {
        super.writeDecimalAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeDecimalAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeDouble(double value) throws XMLStreamException {
        super.writeDouble(value);

        this.delegate2.writeDouble(value);
    }

    @Override
    public void writeDoubleArray(double[] value, int from, int length) throws XMLStreamException {
        super.writeDoubleArray(value, from, length);

        this.delegate2.writeDoubleArray(value, from, length);
    }

    @Override
    public void writeDoubleArrayAttribute(String nsPrefix, String nsUri, String localName, double[] value) throws XMLStreamException {
        super.writeDoubleArrayAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeDoubleArrayAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeDoubleAttribute(String nsPrefix, String nsUri, String localName, double value) throws XMLStreamException {
        super.writeDoubleAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeDoubleAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeFloat(float value) throws XMLStreamException {
        super.writeFloat(value);

        this.delegate2.writeFloat(value);
    }

    @Override
    public void writeFloatArray(float[] value, int from, int length) throws XMLStreamException {
        super.writeFloatArray(value, from, length);

        this.delegate2.writeFloatArray(value, from, length);
    }

    @Override
    public void writeFloatArrayAttribute(String nsPrefix, String nsUri, String localName, float[] value) throws XMLStreamException {
        super.writeFloatArrayAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeFloatArrayAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeFloatAttribute(String nsPrefix, String nsUri, String localName, float value) throws XMLStreamException {
        super.writeFloatAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeFloatAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeFullEndElement() throws XMLStreamException {
        super.writeFullEndElement();

        this.delegate2.writeFullEndElement();
    }

    @Override
    public void writeInt(int value) throws XMLStreamException {
        super.writeInt(value);

        this.delegate2.writeInt(value);
    }

    @Override
    public void writeIntArray(int[] value, int from, int length) throws XMLStreamException {
        super.writeIntArray(value, from, length);

        this.delegate2.writeIntArray(value, from, length);
    }

    @Override
    public void writeIntArrayAttribute(String nsPrefix, String nsUri, String localName, int[] value) throws XMLStreamException {
        super.writeIntArrayAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeIntArrayAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeIntAttribute(String nsPrefix, String nsUri, String localName, int value) throws XMLStreamException {
        super.writeIntAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeIntAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeInteger(BigInteger value) throws XMLStreamException {
        super.writeInteger(value);

        this.delegate2.writeInteger(value);
    }

    @Override
    public void writeIntegerAttribute(String nsPrefix, String nsUri, String localName, BigInteger value) throws XMLStreamException {
        super.writeIntegerAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeIntegerAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeLong(long value) throws XMLStreamException {
        super.writeLong(value);

        this.delegate2.writeLong(value);
    }

    @Override
    public void writeLongArray(long[] value, int from, int length) throws XMLStreamException {
        super.writeLongArray(value, from, length);

        this.delegate2.writeLongArray(value, from, length);
    }

    @Override
    public void writeLongArrayAttribute(String nsPrefix, String nsUri, String localName, long[] value) throws XMLStreamException {
        super.writeLongArrayAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeLongArrayAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeLongAttribute(String nsPrefix, String nsUri, String localName, long value) throws XMLStreamException {
        super.writeLongAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeLongAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeQName(QName value) throws XMLStreamException {
        super.writeQName(value);

        this.delegate2.writeQName(value);
    }

    @Override
    public void writeQNameAttribute(String nsPrefix, String nsUri, String localName, QName value) throws XMLStreamException {
        super.writeQNameAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeQNameAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeCharacters(char[] text, int offset, int len) throws XMLStreamException {
        super.writeCharacters(text, offset, len);

        this.delegate2.writeCharacters(text, offset, len);
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        super.writeCharacters(text);

        this.delegate2.writeCharacters(text);
    }

    @Override
    public void writeSpace(char[] text, int offset, int length) throws XMLStreamException {
        super.writeSpace(text, offset, length);

        this.delegate2.writeSpace(text, offset, length);
    }

    @Override
    public void writeSpace(String text) throws XMLStreamException {
        super.writeSpace(text);

        this.delegate2.writeSpace(text);
    }

    @Override
    public void writeRaw(char[] text, int offset, int length) throws XMLStreamException {
        super.writeRaw(text, offset, length);

        this.delegate2.writeRaw(text, offset, length);
    }

    @Override
    public void writeRaw(String text) throws XMLStreamException {
        super.writeRaw(text);

        this.delegate2.writeRaw(text);
    }

    @Override
    public void writeRaw(String text, int offset, int length) throws XMLStreamException {
        super.writeRaw(text, offset, length);

        this.delegate2.writeRaw(text, offset, length);
    }

    @Override
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        super.writeAttribute(localName, value);

        this.delegate2.writeAttribute(localName, value);
    }

    @Override
    public void writeAttribute(String nsUri, String localName, String value) throws XMLStreamException {
        super.writeAttribute(nsUri, localName, value);

        this.delegate2.writeAttribute(nsUri, localName, value);
    }

    @Override
    public void writeAttribute(String nsPrefix, String nsUri, String localName, String value) throws XMLStreamException {
        super.writeAttribute(nsPrefix, nsUri, localName, value);

        this.delegate2.writeAttribute(nsPrefix, nsUri, localName, value);
    }

    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        super.writeStartElement(localName);

        this.delegate2.writeStartElement(localName);
    }

    @Override
    public void writeStartElement(String nsUri, String localName) throws XMLStreamException {
        super.writeStartElement(nsUri, localName);

        this.delegate2.writeStartElement(nsUri, localName);
    }

    @Override
    public void writeStartElement(String nsPrefix, String localName, String nsUri) throws XMLStreamException {
        super.writeStartElement(nsPrefix, localName, nsUri);

        this.delegate2.writeStartElement(nsPrefix, localName, nsUri);
    }

    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException {
        super.writeEmptyElement(localName);

        this.delegate2.writeEmptyElement(localName);
    }

    @Override
    public void writeEmptyElement(String nsUri, String localName) throws XMLStreamException {
        super.writeEmptyElement(nsUri, localName);

        this.delegate2.writeEmptyElement(nsUri, localName);
    }

    @Override
    public void writeEmptyElement(String nsPrefix, String localName, String nsUri) throws XMLStreamException {
        super.writeEmptyElement(nsPrefix, localName, nsUri);

        this.delegate2.writeEmptyElement(nsPrefix, localName, nsUri);
    }

    @Override
    public void writeNamespace(String nsPrefix, String nsUri) throws XMLStreamException {
        super.writeNamespace(nsPrefix, nsUri);

        this.delegate2.writeNamespace(nsPrefix, nsUri);
    }

    @Override
    public void writeDefaultNamespace(String nsUri) throws XMLStreamException {
        super.writeDefaultNamespace(nsUri);

        this.delegate2.writeDefaultNamespace(nsUri);
    }

    @Override
    public void writeEntityRef(String name) throws XMLStreamException {
        super.writeEntityRef(name);

        this.delegate2.writeEntityRef(name);
    }

    @Override
    public void writeDTD(String data) throws XMLStreamException {
        super.writeDTD(data);

        this.delegate2.writeDTD(data);
    }

    @Override
    public void writeDTD(String rootName, String sysId, String publicId, String internalSubset) throws XMLStreamException {
        super.writeDTD(rootName, sysId, publicId, internalSubset);

        this.delegate2.writeDTD(rootName, sysId, publicId, internalSubset);
    }

    @Override
    public void writeCData(char[] text, int start, int len) throws XMLStreamException {
        super.writeCData(text, start, len);

        this.delegate2.writeCData(text, start, len);
    }

    @Override
    public void writeCData(String data) throws XMLStreamException {
        super.writeCData(data);

        this.delegate2.writeCData(data);
    }

    @Override
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        super.writeProcessingInstruction(target);

        this.delegate2.writeProcessingInstruction(target);
    }

    @Override
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        super.writeProcessingInstruction(target, data);

        this.delegate2.writeProcessingInstruction(target, data);
    }

    @Override
    public void writeComment(String data) throws XMLStreamException {
        super.writeComment(data);

        this.delegate2.writeComment(data);
    }

    @Override
    public void writeStartDocument() throws XMLStreamException {
        super.writeStartDocument();

        this.delegate2.writeStartDocument();
    }

    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
        super.writeStartDocument(version);

        this.delegate2.writeStartDocument(version);
    }

    @Override
    public void writeStartDocument(String enc, String version) throws XMLStreamException {
        super.writeStartDocument(enc, version);

        this.delegate2.writeStartDocument(enc, version);
    }

    @Override
    public void writeStartDocument(String version, String enc, boolean standalone) throws XMLStreamException {
        super.writeStartDocument(version, enc, standalone);

        this.delegate2.writeStartDocument(version, enc, standalone);
    }

    @Override
    public void setPrefix(String prefix, String nsUri) throws XMLStreamException {
        super.setPrefix(prefix, nsUri);

        this.delegate2.setPrefix(prefix, nsUri);
    }

    @Override
    public void setDefaultNamespace(String nsUri) throws XMLStreamException {
        super.setDefaultNamespace(nsUri);

        this.delegate2.setDefaultNamespace(nsUri);
    }

    @Override
    public void setNamespaceContext(NamespaceContext nc) throws XMLStreamException {
        super.setNamespaceContext(nc);

        this.delegate2.setNamespaceContext(nc);
    }

    @Override
    public boolean setProperty(String propName, Object propValue) {
        boolean propChanged = super.setProperty(propName, propValue);

        this.delegate2.setProperty(propName, propValue);

        return propChanged;
    }
}
