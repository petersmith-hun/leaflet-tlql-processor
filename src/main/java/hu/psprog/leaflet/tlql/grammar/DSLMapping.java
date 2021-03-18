package hu.psprog.leaflet.tlql.grammar;

import hu.psprog.leaflet.tlql.ir.DSLLogicalOperator;
import hu.psprog.leaflet.tlql.ir.DSLObject;
import hu.psprog.leaflet.tlql.ir.DSLOperator;
import hu.psprog.leaflet.tlql.ir.DSLOrderDirection;

import java.util.Map;

/**
 * Mapping raw {@link QueryLanguageToken} enum constants to their IR counterparts.
 *
 * @author Peter Smith
 */
public interface DSLMapping {

    /**
     * "Object" token mapping.
     */
    Map<QueryLanguageToken, DSLObject> TOKEN_TO_OBJECT_MAP = Map.of(
            QueryLanguageToken.OBJECT_SOURCE, DSLObject.SOURCE,
            QueryLanguageToken.OBJECT_LEVEL, DSLObject.LEVEL,
            QueryLanguageToken.OBJECT_MESSAGE, DSLObject.MESSAGE,
            QueryLanguageToken.OBJECT_TIMESTAMP, DSLObject.TIMESTAMP
    );

    /**
     * "Operator" token mapping.
     */
    Map<QueryLanguageToken, DSLOperator> TOKEN_TO_OPERATOR_MAP = Map.of(
            QueryLanguageToken.OPERATOR_EQUAL, DSLOperator.EQUALS,
            QueryLanguageToken.OPERATOR_NOT_EQUAL, DSLOperator.NOT_EQUALS,
            QueryLanguageToken.OPERATOR_LIKE, DSLOperator.LIKE,
            QueryLanguageToken.OPERATOR_EITHER, DSLOperator.EITHER,
            QueryLanguageToken.OPERATOR_NONE, DSLOperator.NONE,
            QueryLanguageToken.OPERATOR_GREATER, DSLOperator.GREATER_THAN,
            QueryLanguageToken.OPERATOR_GREATER_OR_EQUAL, DSLOperator.GREATER_THAN_OR_EQUAL,
            QueryLanguageToken.OPERATOR_LESS, DSLOperator.LESS_THAN,
            QueryLanguageToken.OPERATOR_LESS_OR_EQUAL, DSLOperator.LESS_THAN_OR_EQUAL,
            QueryLanguageToken.OPERATOR_BETWEEN, DSLOperator.BETWEEN
    );

    /**
     * "Logical operator" token mapping.
     */
    Map<QueryLanguageToken, DSLLogicalOperator> TOKEN_TO_LOGICAL_OPERATOR_MAP = Map.of(
            QueryLanguageToken.OPERATOR_AND, DSLLogicalOperator.AND,
            QueryLanguageToken.OPERATOR_OR, DSLLogicalOperator.OR
    );

    /**
     * "Order direction" token mapping.
     */
    Map<QueryLanguageToken, DSLOrderDirection> TOKEN_TO_ORDER_DIRECTION_MAP = Map.of(
            QueryLanguageToken.KEYWORD_ASC, DSLOrderDirection.ASC,
            QueryLanguageToken.KEYWORD_DESC, DSLOrderDirection.DESC
    );
}
