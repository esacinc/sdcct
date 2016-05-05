package gov.hhs.onc.sdcct.xml.jaxb.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.io.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.io.impl.ByteArraySource;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JaxbResult<T> extends ByteArrayResult implements Closeable {
    public class JaxbOutputStream extends ByteArrayOutputStream {
        @Override
        public void close() throws IOException {
            try {
                JaxbResult.this.result =
                    JaxbResult.this.unmarshaller.unmarshal(new ByteArraySource(this.toByteArray(), JaxbResult.this.getSystemId()), JaxbResult.this.resultClass)
                        .getValue();
            } catch (JAXBException e) {
                throw new IOException(e);
            }
        }
    }

    private Unmarshaller unmarshaller;
    private Class<T> resultClass;
    private T result;

    public JaxbResult(Unmarshaller unmarshaller, Class<T> resultClass) {
        this(unmarshaller, resultClass, null);
    }

    public JaxbResult(Unmarshaller unmarshaller, Class<T> resultClass, @Nullable String sysId) {
        super(sysId, null);

        this.unmarshaller = unmarshaller;
        this.resultClass = resultClass;

        this.setOutputStream(new JaxbOutputStream());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public JaxbOutputStream getOutputStream() {
        return ((JaxbOutputStream) super.getOutputStream());
    }

    public T getResult() throws JAXBException {
        return this.result;
    }

    @Override
    public void close() throws IOException {
        this.getOutputStream().close();
    }
}
