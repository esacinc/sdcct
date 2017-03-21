package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@SuppressWarnings({ "serial" })
@Component
public class SdcctTestcaseDeserializer<T extends SdcctTestcaseDescription> extends StdDeserializer<SdcctTestcase<T>> {
    @Autowired
    @Lazy
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<SdcctTestcase<T>> testcases;

    private final static String TESTCASE_ID = "id";

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected SdcctTestcaseDeserializer() {
        super(SdcctTestcase.class);
    }

    @Override
    public SdcctTestcase<T> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String testcaseId = node.has(TESTCASE_ID) ? node.get(TESTCASE_ID).asText() : node.asText();

        return this.testcases.stream().filter(sdcctTestcase -> sdcctTestcase.getId().equals(testcaseId)).findFirst().orElse(null);
    }
}
