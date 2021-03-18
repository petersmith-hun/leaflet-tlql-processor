package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

/**
 * Conditions keyword parser.
 * Moves the parser into condition expression processing.
 *
 * Represented production rules:
 *  - CONDITIONS -> conditions TIMESTAMP_CONDITION
 *  - CONDITIONS -> conditions MULTI_MATCH_CONDITION
 *  - CONDITIONS -> conditions SIMPLE_CONDITION
 *
 * @author Peter Smith
 */
@Component
public class ConditionsSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {

        context.discardToken(QueryLanguageToken.KEYWORD_CONDITIONS);

        if (context.getNextToken() == QueryLanguageToken.SYMBOL_OPENING_BRACKET) {
            context.openConditionGroup();
            context.discardToken();
        }
    }

    @Override
    public QuerySection chainTo(GrammarParserContext context) {

        QuerySection chainToSection;
        if (context.getNextToken() == QueryLanguageToken.OBJECT_TIMESTAMP) {
            chainToSection = QuerySection.TIMESTAMP_CONDITION;
        } else if (MULTI_MATCH_CONDITIONAL_TOKENS.contains(context.getUpcomingToken())) {
            chainToSection = QuerySection.MULTI_MATCH_CONDITION;
        } else {
            chainToSection = QuerySection.SIMPLE_CONDITION;
        }

        return chainToSection;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.CONDITIONS;
    }
//
//    @Override
//    public String getKeyword(GrammarParserContext context) {
//        return "conditions";
//    }
}
