package hu.psprog.leaflet.tlql.ir;

/**
 * Possible condition operators in IR.
 *
 * @author Peter Smith
 */
public enum DSLOperator {

    EQUALS,
    NOT_EQUALS,
    LIKE,
    EITHER,
    NONE,
    BETWEEN,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL
}
