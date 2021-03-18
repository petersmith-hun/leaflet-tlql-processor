package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

/**
 * Offset keyword parser.
 * Moves the parser into record offset value processing.
 *
 * Represented production rules:
 *  - OFFSET -> offset number_literal MAIN
 *
 * @author Peter Smith
 */
@Component
public class OffsetSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {
        context.discardToken();
        context.getQueryModel().setOffset(context.extractValueAndAdvance(QueryLanguageToken.LITERAL_NUMBER, Integer::parseInt));
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {
        return QuerySection.MAIN;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.OFFSET;
    }
//
//    @Override
//    public String getKeyword(GrammarParserContext context) {
//        return "offset number_literal";
//    }
}
