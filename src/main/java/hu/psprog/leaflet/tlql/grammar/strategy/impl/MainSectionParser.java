package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import org.springframework.stereotype.Component;

/**
 * Main section parser.
 * Implicit section for "routing" purposes.
 *
 * Represented production rules:
 *  - MAIN -> WITH
 *  - MAIN -> EOS
 *
 * @author Peter Smith
 */
@Component
public class MainSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {
        // do nothing at this point
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {

        return switch (context.getNextToken()) {
            case KEYWORD_WITH -> QuerySection.WITH;
            case TERMINATOR -> null;
            default -> throw new DSLParserException(String.format("Unexpected token %s", context.getNextToken()));
        };
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.MAIN;
    }
}
