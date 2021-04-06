package hu.psprog.leaflet.tlql.processor;

import hu.psprog.leaflet.tlql.ir.DSLQueryModel;

/**
 * Service to process a Tiny Log Query Language (TLQL) script.
 *
 * @author Peter Smith
 */
public interface TLQLProcessorService {

    /**
     * Parses the given TLQL script and prepares an intermediate representation.
     *
     * @param inputQuery TLQL script to be parsed
     * @return intermediate representation object as {@link DSLQueryModel}
     */
    DSLQueryModel parse(String inputQuery);
}
