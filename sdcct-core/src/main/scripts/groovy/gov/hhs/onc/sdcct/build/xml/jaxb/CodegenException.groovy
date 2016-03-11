package gov.hhs.onc.sdcct.build.xml.jaxb

import javax.annotation.Nullable

class CodegenException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    CodegenException(@Nullable String msg) {
        this(msg, null);
    }

    CodegenException(@Nullable Throwable cause) {
        this(null, cause);
    }

    CodegenException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
