package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.DSLMapping;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.ir.DSLLogicalOperator;
import org.springframework.stereotype.Component;

/**
 * Condition chaining section parser.
 * Responsible for adding further conditions in the IR or terminating processing of condition section
 * and moving on to another section.
 *
 * Represented production rules:
 *  - CONDITION_CHAIN -> and TIMESTAMP_CONDITION
 *  - CONDITION_CHAIN -> or TIMESTAMP_CONDITION
 *  - CONDITION_CHAIN -> and SIMPLE_CONDITION
 *  - CONDITION_CHAIN -> or SIMPLE_CONDITION
 *  - CONDITION_CHAIN -> either MULTI_MATCH_CONDITION
 *  - CONDITION_CHAIN -> none MULTI_MATCH_CONDITION
 *  - CONDITION_CHAIN -> MAIN
 *
 * @author Peter Smith
 */
@Component
public class ConditionChainSectionParser implements QuerySectionParser {

    @Override
    public void parseSection(GrammarParserContext context) {

        if (context.getNextToken() == QueryLanguageToken.SYMBOL_CLOSING_BRACKET) {

            context.discardToken();

            if (context.getNextToken().isLogicalOperator()) {
                context.getCurrentConditionGroup().setNextConditionGroupOperator(context.extractValueAndAdvance(DSLMapping.TOKEN_TO_LOGICAL_OPERATOR_MAP::get));
            }

            context.closeConditionGroup();
        }

        if (context.getNextToken().isLogicalOperator()) {
            DSLLogicalOperator logicalOperator = context.extractValueAndAdvance(DSLMapping.TOKEN_TO_LOGICAL_OPERATOR_MAP::get);

            if (context.getNextToken() == QueryLanguageToken.SYMBOL_OPENING_BRACKET) {
                context.getCurrentConditionGroup().setNextConditionGroupOperator(logicalOperator);
            } else {
                context.getCurrentCondition().setNextConditionOperator(logicalOperator);
            }
        }

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
        } else if (context.getNextToken().isObject()) {
            chainToSection = QuerySection.SIMPLE_CONDITION;
        } else {
            chainToSection = QuerySection.MAIN;
        }

        return chainToSection;
    }

    @Override
    public QuerySection forSection() {
        return QuerySection.CONDITION_CHAIN;
    }
}
