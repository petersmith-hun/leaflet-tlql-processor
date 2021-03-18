package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

/**
 * Order by keyword-pair parser.
 * This parser is the entry point for order expression parsing.
 * Expects the "order" and the "by" keywords one after the other.
 *
 * Represented production rules:
 *  - ORDER_BY -> order by ORDER_EXPRESSION
 *
 * @author Peter Smith
 */
@Component
public class OrderSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {
        context.discardToken(QueryLanguageToken.KEYWORD_ORDER);
        context.discardToken(QueryLanguageToken.KEYWORD_BY);
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {
        return QuerySection.ORDER_EXPRESSION;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.ORDER_BY;
    }
//
//    @Override
//    public String getKeyword(GrammarParserContext context) {
//        return "order by";
//    }
}
