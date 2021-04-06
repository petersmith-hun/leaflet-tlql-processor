package hu.psprog.leaflet.tlql.grammar.strategy;

/**
 * Possible query section IDs.
 *
 * @author Peter Smith
 */
public enum QuerySection {

    SEARCH,
    MAIN,
    WITH,
    CONDITIONS,
    SIMPLE_CONDITION,
    MULTI_MATCH_CONDITION,
    TIMESTAMP_CONDITION,
    CONDITION_CHAIN,
    ORDER_BY,
    ORDER_EXPRESSION,
    ORDER_CHAIN,
    LIMIT,
    OFFSET
}
