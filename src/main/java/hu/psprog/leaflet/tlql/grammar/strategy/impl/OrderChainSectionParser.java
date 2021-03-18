package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

/**
 * Ordering expression chaining parser.
 * Responsible for adding further ordering expressions in the IR or terminating processing of ordering section
 * and moving on to another section.
 *
 * Represented production rules:
 *  - ORDER_CHAIN -> then ORDER_EXPRESSION
 *  - ORDER_CHAIN -> MAIN
 *
 * @author Peter Smith
 */
@Component
public class OrderChainSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {

        if (context.getNextToken() == QueryLanguageToken.KEYWORD_THEN) {
            context.discardToken(QueryLanguageToken.KEYWORD_THEN);
        }
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {

        return context.getNextToken().isObject()
                ? QuerySection.ORDER_EXPRESSION
                : QuerySection.MAIN;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.ORDER_CHAIN;
    }
//
//    @Override
//    public String getKeyword(GrammarParserContext context) {
//
//        return chainTo(context) == QuerySection.MAIN
//                ? ""
//                : "then";
//    }
}
