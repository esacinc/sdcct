package gov.hhs.onc.sdcct.config.impl;

import gov.hhs.onc.sdcct.config.Option;

public class OptionImpl<T> implements Option<T> {
    private String name;
    private Class<T> valueClass;

    public OptionImpl(String name, Class<T> valueClass) {
        this.name = name;
        this.valueClass = valueClass;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<T> getValueClass() {
        return this.valueClass;
    }
}
