package hu.psprog.leaflet.tlql.exception;

/**
 * Generic query parsing exception type.
 *
 * @author Peter Smith
 */
public class DSLParserException extends RuntimeException {

    public DSLParserException(String message) {
        super(message);
    }
}
