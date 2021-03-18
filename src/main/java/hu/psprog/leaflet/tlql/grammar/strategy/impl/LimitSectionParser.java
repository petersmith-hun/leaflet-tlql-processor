package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

/**
 * Limit keyword parser.
 * Moves the parser into record limit value processing.
 *
 * Represented production rules:
 *  - LIMIT -> limit number_literal MAIN
 *
 * @author Peter Smith
 */
@Component
public class LimitSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {

        context.discardToken();
        context.getQueryModel().setLimit(context.extractValueAndAdvance(QueryLanguageToken.LITERAL_NUMBER, Integer::parseInt));
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {
        return QuerySection.MAIN;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.LIMIT;
    }
//
//    @Override
//    public String getKeyword(GrammarParserContext context) {
//        return "limit number_literal";
//    }
}
