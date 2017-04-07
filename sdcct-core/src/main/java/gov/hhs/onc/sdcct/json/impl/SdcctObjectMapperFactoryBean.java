package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

public class SdcctObjectMapperFactoryBean extends Jackson2ObjectMapperFactoryBean {
    private static class SdcctModuleList extends SetUniqueList<Module> {
        private final static long serialVersionUID = 0L;

        public SdcctModuleList() {
            super(new LinkedList<>(), new LinkedHashSet<>());

            this.addAll(DEFAULT_MODULES);
        }
    }

    private class SdcctPrettyPrinter extends DefaultPrettyPrinter {
        private final static long serialVersionUID = 0L;

        public SdcctPrettyPrinter(String rootIndentStr) {
            super(rootIndentStr);
        }

        @Override
        public void writeObjectFieldValueSeparator(JsonGenerator jsonGen) throws IOException {
            jsonGen.writeRaw(FIELD_VALUE_DELIM);
        }

        @Override
        public SdcctPrettyPrinter createInstance() {
            return this;
        }
    }

    private final static SerializedString FIELD_VALUE_DELIM = new SerializedString(": ");

    private final static List<Module> DEFAULT_MODULES =
        Stream.of(new Jdk8Module(), new GuavaModule().configureAbsentsAsNulls(false)).collect(Collectors.toList());

    private int indentSize;
    private List<Module> modules = new SdcctModuleList();
    private Value propInclusion = Value.empty();

    @Override
    public void afterPropertiesSet() {
        super.setModules(this.modules);

        super.afterPropertiesSet();

        ObjectMapper objMapper = this.getObject();
        DefaultIndenter indenter = new DefaultIndenter(StringUtils.repeat(StringUtils.SPACE, this.indentSize), StringUtils.LF);

        SdcctPrettyPrinter prettyPrinter = new SdcctPrettyPrinter(indenter.getIndent());
        prettyPrinter.indentArraysWith(indenter);
        prettyPrinter.indentObjectsWith(indenter);

        objMapper.setDefaultPrettyPrinter(prettyPrinter);

        objMapper.setPropertyInclusion(this.propInclusion);
    }

    public Include getContentInclusion() {
        return this.propInclusion.getContentInclusion();
    }

    public void setContentInclusion(Include contentInclusion) {
        this.propInclusion = this.propInclusion.withContentInclusion(contentInclusion);
    }

    public void setFeatures(Map<Object, Boolean> features) {
        features.forEach(this::setFeature);
    }

    public void setFeature(Object feature, boolean featureValue) {
        if (featureValue) {
            this.setFeaturesToEnable(feature);
        } else {
            this.setFeaturesToDisable(feature);
        }
    }

    public int getIndentSize() {
        return this.indentSize;
    }

    public void setIndentSize(int indentSize) {
        this.indentSize = indentSize;
    }

    public List<Module> getModules() {
        return this.modules;
    }

    @Override
    public void setModules(List<Module> modules) {
        this.modules.clear();
        this.modules.addAll(DEFAULT_MODULES);
        this.modules.addAll(modules);
    }

    public Include getValueInclusion() {
        return this.propInclusion.getValueInclusion();
    }

    public void setValueInclusion(Include valueInclusion) {
        this.propInclusion = this.propInclusion.withValueInclusion(valueInclusion);
    }
}
