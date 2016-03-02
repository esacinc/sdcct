package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import javax.annotation.Nullable;

public interface FhirResource extends ResourceEntity {
    public boolean hasText();

    @Nullable
    public String getText();

    public void setText(@Nullable String text);
}
