package hu.psprog.leaflet.tlql.grammar.strategy;


import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;

import java.util.Arrays;
import java.util.List;

/**
 * Parser component interface for different "sections" of a query string.
 * A query section parser implementation conforms a language production rule.
 *
 * @author Peter Smith
 */
public interface QuerySectionParser {

    List<QueryLanguageToken> MULTI_MATCH_CONDITIONAL_TOKENS =
            Arrays.asList(QueryLanguageToken.OPERATOR_EITHER, QueryLanguageToken.OPERATOR_NONE);

    /**
     * Translates the current token (section) of the query string into an entry in the Intermediate Representation.
     *
     * @param context {@link GrammarParserContext} containing information about the parsed query, including the currently selected token
     */
    void parseSection(GrammarParserContext context);

    /**
     * Defines the possible next steps in the processing chain.
     * Returning {@code null} will terminate the chain.
     *
     * @param context containing information about the parsed query, including the currently selected token
     * @return the ID of the next parser to be called
     */
    QuerySection chainTo(GrammarParserContext context);

    /**
     * Defines what section this parser can translate.
     * Used in chaining the parsers.
     *
     * @return ID of this parser as {@link QuerySection} enum constant
     */
    QuerySection forSection();
//
//    default String rule(GrammarParserContext context) {
//
//        String keyword = getKeyword(context);
//        String chainTo = Optional.ofNullable(chainTo(context))
//                .map(Enum::name)
//                .orElse("EOS");
//
//        return String.format("%s -> %s %s", forSection(), keyword, chainTo);
//    }
//
//    String getKeyword(GrammarParserContext context);
}
