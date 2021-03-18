package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.DSLMapping;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import org.springframework.stereotype.Component;

/**
 * Simple condition expression parser.
 * Parser to be used with generic object-operator-value condition expressions,
 * except for those where the object is a "timestamp".
 *
 * Represented production rules:
 *  - SIMPLE_CONDITION -> object operator string_literal CONDITION_CHAIN
 *
 * @author Peter Smith
 */
@Component
public class SimpleConditionSectionParser extends AbstractConditionSectionParser {

    @Override
    public QuerySection forSection() {
        return QuerySection.SIMPLE_CONDITION;
    }
//
//    @Override
//    public String getKeyword(GrammarParserContext context) {
//        return "object operator string_literal";
//    }

    @Override
    protected void processCondition(GrammarParserContext context) {

        DSLCondition currentCondition = context.createDSLCondition();

        currentCondition.setObject(context.extractValueAndAdvance(DSLMapping.TOKEN_TO_OBJECT_MAP::get));
        currentCondition.setOperator(context.extractValueAndAdvance(DSLMapping.TOKEN_TO_OPERATOR_MAP::get));
        currentCondition.setValue(context.extractValueAndAdvance(context::readToken).getValue());
    }
}
