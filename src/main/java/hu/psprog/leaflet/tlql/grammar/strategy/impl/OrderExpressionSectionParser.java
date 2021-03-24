package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Ordering expression parser.
 * Responsible for adding an ordering directive in the IR.
 *
 * Represented presentation rules:
 *  - ORDER_EXPRESSION -> object direction_literal ORDER_CHAIN
 *
 * @author Peter Smith
 */
@Component
public class OrderExpressionSectionParser implements QuerySectionParser {

    private static final List<QueryLanguageToken> ORDER_DIRECTION_TOKENS =
            Arrays.asList(QueryLanguageToken.KEYWORD_ASC, QueryLanguageToken.KEYWORD_DESC,
                    QueryLanguageToken.KEYWORD_ASCENDING, QueryLanguageToken.KEYWORD_DESCENDING);

    @Override
    public void parseSection(GrammarParserContext context) {

        QueryLanguageToken objectToken = context.getNextToken();
        QueryLanguageToken directionToken = context.getUpcomingToken();

        if (!objectToken.isObject()) {
            throw new DSLParserException(String.format("Expected OBJECT token, got %s", objectToken));
        }

        if (!(directionToken.isKeyword() && ORDER_DIRECTION_TOKENS.contains(directionToken))) {
            throw new DSLParserException(String.format("Expected order direction expression (object asc|desc), got %s %s", objectToken, directionToken));
        }

        context.addOrderingAndAdvance();
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {
        return QuerySection.ORDER_CHAIN;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.ORDER_EXPRESSION;
    }
}
