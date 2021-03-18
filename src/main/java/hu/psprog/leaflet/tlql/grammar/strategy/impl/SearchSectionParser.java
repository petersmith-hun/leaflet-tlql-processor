package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

/**
 * Search keyword parser.
 * Search keyword is the entry point of query strings therefore this should be the worst keyword in the query string.
 *
 * Represented production rules:
 *  - SEARCH -> search WITH
 *
 * @author Peter Smith
 */
@Component
public class SearchSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {
        context.discardToken(QueryLanguageToken.KEYWORD_SEARCH);
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {
        return QuerySection.WITH;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.SEARCH;
    }
//
//    @Override
//    public String getKeyword(GrammarParserContext context) {
//        return "search";
//    }
}
