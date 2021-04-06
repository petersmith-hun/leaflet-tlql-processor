package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.DSLMapping;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * "Multi-match" condition expression parser.
 * Multi-match conditions are conditions produced by either and none operators.
 *
 * Represented production rules:
 *  - MULTI_MATCH_CONDITION -> object either array_of_string_literals CONDITION_CHAIN
 *  - MULTI_MATCH_CONDITION -> object none array_of_string_literals CONDITION_CHAIN
 *
 * @author Peter Smith
 */
@Component
public class MultiMatchConditionSectionParser extends AbstractConditionSectionParser {

    @Override
    public QuerySection forSection() {
        return QuerySection.MULTI_MATCH_CONDITION;
    }

    @Override
    protected void processCondition(GrammarParserContext context) {

        DSLCondition currentCondition = context.createDSLCondition();

        currentCondition.setObject(context.extractValueAndAdvance(DSLMapping.TOKEN_TO_OBJECT_MAP::get));
        currentCondition.setOperator(context.extractValueAndAdvance(DSLMapping.TOKEN_TO_OPERATOR_MAP::get));
        currentCondition.setMultipleValue(extractParameters(context));
    }

    private List<String> extractParameters(GrammarParserContext context) {

        context.discardToken(QueryLanguageToken.SYMBOL_OPENING_BRACKET);

        List<String> valueList = new LinkedList<>();
        while (true) {

            QueryLanguageToken token = context.getNextToken();

            if (token == QueryLanguageToken.SYMBOL_CLOSING_BRACKET) {
                context.discardToken();
                break;
            }

            if (token == QueryLanguageToken.SYMBOL_COMMA) {
                context.discardToken();
                continue;
            }

            valueList.add(context.readToken(QueryLanguageToken.TokenGroup.LITERAL).getValue());
            context.discardToken();
        }

        return valueList;
    }
}
