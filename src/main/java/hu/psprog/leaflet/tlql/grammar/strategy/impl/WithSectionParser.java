package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

/**
 * With keyword parser.
 * With keyword is the entry point expression for different kinds of query sections,
 * such as conditions, ordering, limit and offset parameters.
 *
 * Represented production rules:
 *  - WITH -> with CONDITIONS
 *  - WITH -> with ORDER_BY
 *  - WITH -> with LIMIT
 *  - WITH -> with OFFSET
 *
 * @author Peter Smith
 */
@Component
public class WithSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {
        context.discardToken(QueryLanguageToken.KEYWORD_WITH);
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {

        switch (context.getNextToken()) {
            case KEYWORD_CONDITIONS:
                return QuerySection.CONDITIONS;
            case KEYWORD_ORDER:
                return QuerySection.ORDER_BY;
            case KEYWORD_LIMIT:
                return QuerySection.LIMIT;
            case KEYWORD_OFFSET:
                return QuerySection.OFFSET;
            default:
                throw new DSLParserException(String.format("Unexpected token %s of '%s'", context.getNextToken(), context.getLookAheadFirst().getValue()));
        }
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.WITH;
    }
}
