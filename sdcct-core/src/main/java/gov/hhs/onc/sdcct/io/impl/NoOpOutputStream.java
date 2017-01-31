package gov.hhs.onc.sdcct.io.impl;

import java.io.IOException;
import java.io.OutputStream;

public class NoOpOutputStream extends OutputStream {
    public final static NoOpOutputStream INSTANCE = new NoOpOutputStream();

    @Override
    public void write(int data) throws IOException {
    }
}
