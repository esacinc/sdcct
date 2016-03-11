package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import gov.hhs.onc.sdcct.build.xml.jaxb.CodegenNameConverter

class CodegenNameConverterProxy  implements CodegenNameConverter {
    private CodegenNameConverter delegate

    CodegenNameConverterProxy(CodegenNameConverter delegate) {
        this.delegate = delegate
    }

    @Override
    String toPackageName(String nsUri) {
        return this.delegate.toPackageName(nsUri)
    }

    @Override
    String toClassName(String token) {
        return this.delegate.toClassName(token)
    }
    
    @Override
    String toInterfaceName(String token) {
        return this.delegate.toInterfaceName(token)
    }

    @Override
    String toConstantName(String token) {
        return this.delegate.toConstantName(token)
    }

    @Override
    String toPropertyName(String token) {
        return this.delegate.toPropertyName(token)
    }

    @Override
    String toVariableName(String token) {
        return this.delegate.toVariableName(token)
    }

    CodegenNameConverter getDelegate() {
        return delegate
    }

    void setDelegate(CodegenNameConverter delegate) {
        this.delegate = delegate
    }
}
