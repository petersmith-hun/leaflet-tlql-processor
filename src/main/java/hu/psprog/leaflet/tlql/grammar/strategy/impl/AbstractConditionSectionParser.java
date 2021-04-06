package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;

/**
 * Abstract implementation of {@link QuerySectionParser} for handling condition sections.
 * Parsing always checks whether the next to tokens are object and operator (in this order).
 * Chaining always advances to the CONDITION_CHAIN parser.
 *
 * @author Peter Smith
 */
abstract class AbstractConditionSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {

        if (!context.getNextToken().isObject()) {
            throw new DSLParserException(String.format("Condition should start with OBJECT token, got %s of '%s'",
                    context.getNextToken().getGroup(), context.getLookAheadFirst().getValue()));
        }

        if (!context.getUpcomingToken().isOperator()) {
            throw new DSLParserException(String.format("Operator expected, got %s of '%s'",
                    context.getUpcomingToken().getGroup(), context.getLookAheadSecond().getValue()));
        }

        processCondition(context);
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {
        return QuerySection.CONDITION_CHAIN;
    }

    /**
     * Actual condition processing logic for a concrete parser.
     *
     * @param context containing information about the parsed query, including the currently selected token
     */
    protected abstract void processCondition(GrammarParserContext context);
}
