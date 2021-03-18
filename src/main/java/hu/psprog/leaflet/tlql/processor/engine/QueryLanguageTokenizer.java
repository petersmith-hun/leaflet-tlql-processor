package hu.psprog.leaflet.tlql.processor.engine;

import hu.psprog.leaflet.tlql.grammar.ParsedToken;

import java.util.List;

/**
 * TLQL tokenizer.
 * Implementations should be able to take a string as the input query and tokenize it into {@link ParsedToken} objects,
 * containing the type of the recognized token and its actual value (to be used as parameters).
 *
 * @author Peter Smith
 */
public interface QueryLanguageTokenizer {

    /**
     * Tokenizes the given TLQL query string.
     *
     * @param inputQuery TLQL script to be parsed
     * @return list of tokens as {@link ParsedToken} objects
     */
    List<ParsedToken> tokenize(String inputQuery);
}
