package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.processor.engine.QueryLanguageTokenizer;
import hu.psprog.leaflet.tlql.processor.engine.impl.RegexBasedQueryLanguageTokenizer;

/**
 * {@link QuerySectionParser} unit test base.
 *
 * @author Peter Smith
 */
abstract class AbstractSectionParserBaseTest {

    private static final QueryLanguageTokenizer QUERY_LANGUAGE_TOKENIZER = new RegexBasedQueryLanguageTokenizer();

    GrammarParserContext prepareGrammarParserContext(String queryPart) {

        return new GrammarParserContext(QUERY_LANGUAGE_TOKENIZER.tokenize(queryPart));
    }
}
