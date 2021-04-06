package hu.psprog.leaflet.tlql.processor.engine;

import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;

import java.util.List;

/**
 * TLQL parser.
 * Implementation should be able to take a list of {@link ParsedToken} objects containing token information,
 * and convert them into an Intermediate Representation.
 *
 * @author Peter Smith
 */
public interface QueryLanguageParser {

    /**
     * Generates an intermediate representation based on the given list of {@link ParsedToken} objects.
     *
     * @param tokenList list of {@link ParsedToken} objects.
     * @return generated intermediate representation as {@link DSLQueryModel}
     */
    DSLQueryModel parse(List<ParsedToken> tokenList);
}
