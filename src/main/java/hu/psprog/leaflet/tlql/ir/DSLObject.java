package hu.psprog.leaflet.tlql.ir;

import lombok.Getter;

/**
 * Possible condition or ordering objects in IR.
 *
 * @author Peter Smith
 */
@Getter
public enum DSLObject {

    SOURCE(false),
    LEVEL(false),
    TIMESTAMP(false),
    MESSAGE(false),
    LOGGER(false),
    CONTEXT(true);

    private final boolean specialized;

    DSLObject(boolean specialized) {
        this.specialized = specialized;
    }
}
