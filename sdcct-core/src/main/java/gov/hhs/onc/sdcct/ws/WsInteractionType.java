package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.fhir.SystemRestfulInteraction;
import gov.hhs.onc.sdcct.fhir.TypeRestfulInteraction;
import javax.annotation.Nullable;

public enum WsInteractionType implements IdentifiedBean {
    READ(TypeRestfulInteraction.READ.getId(), WsInteractionScopeType.INSTANCE), VREAD(TypeRestfulInteraction.VREAD.getId(), WsInteractionScopeType.INSTANCE),
    UPDATE(TypeRestfulInteraction.UPDATE.getId(), WsInteractionScopeType.INSTANCE),
    DELETE(TypeRestfulInteraction.DELETE.getId(), WsInteractionScopeType.INSTANCE),
    HISTORY_INSTANCE(TypeRestfulInteraction.HISTORY_INSTANCE.getId(), WsInteractionScopeType.INSTANCE),
    HISTORY_TYPE(TypeRestfulInteraction.HISTORY_TYPE.getId(), WsInteractionScopeType.TYPE),
    CREATE(TypeRestfulInteraction.CREATE.getId(), WsInteractionScopeType.TYPE),
    SEARCH_TYPE(TypeRestfulInteraction.SEARCH_TYPE.getId(), WsInteractionScopeType.TYPE), CONFORMANCE("conformance", WsInteractionScopeType.SYSTEM),
    SEARCH_SYSTEM(SystemRestfulInteraction.SEARCH_SYSTEM.getId(), WsInteractionScopeType.SYSTEM),
    HISTORY_SYSTEM(SystemRestfulInteraction.HISTORY_SYSTEM.getId(), WsInteractionScopeType.SYSTEM);

    private final String id;
    private final WsInteractionScopeType scope;

    private WsInteractionType(@Nullable String id, WsInteractionScopeType scope) {
        this.id = ((id != null) ? id : this.name().toLowerCase());
        this.scope = scope;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public WsInteractionScopeType getScope() {
        return this.scope;
    }
}
