package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import hu.psprog.leaflet.tlql.ir.DSLObject;
import hu.psprog.leaflet.tlql.ir.DSLObjectContext;
import hu.psprog.leaflet.tlql.ir.DSLOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link MultiMatchConditionSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class MultiMatchConditionSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private MultiMatchConditionSectionParser multiMatchConditionSectionParser;

    @Test
    public void shouldParseSectionThrowExceptionIfNextTokenIsNotObject() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("with limit");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> multiMatchConditionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldParseSectionThrowExceptionIfUpcomingTokenIsNotOperator() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("source with");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> multiMatchConditionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldParseSectionAdvanceContextByReadingMultiMatchConditionWithOneMember() {

        // given
        GrammarParserContext grammarParserContext =
                prepareGrammarParserContext("source either ('lcfa') with limit");
        DSLCondition expectedDSLCondition = new DSLCondition();
        expectedDSLCondition.setObjectContext(new DSLObjectContext(DSLObject.SOURCE, null));
        expectedDSLCondition.setOperator(DSLOperator.EITHER);
        expectedDSLCondition.setMultipleValue(Collections.singletonList("lcfa"));

        // when
        multiMatchConditionSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().size(), equalTo(1));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().get(0).getConditions().size(), equalTo(1));
        assertThat(grammarParserContext.getCurrentCondition(), equalTo(expectedDSLCondition));
    }

    @Test
    public void shouldParseSectionAdvanceContextByReadingMultiMatchConditionWithMultipleMembers() {

        // given
        GrammarParserContext grammarParserContext =
                prepareGrammarParserContext("level none ('info', 'warning', 'error') with limit");
        DSLCondition expectedDSLCondition = new DSLCondition();
        expectedDSLCondition.setObjectContext(new DSLObjectContext(DSLObject.LEVEL, null));
        expectedDSLCondition.setOperator(DSLOperator.NONE);
        expectedDSLCondition.setMultipleValue(Arrays.asList("info", "warning", "error"));

        // when
        multiMatchConditionSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().size(), equalTo(1));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().get(0).getConditions().size(), equalTo(1));
        assertThat(grammarParserContext.getCurrentCondition(), equalTo(expectedDSLCondition));
    }

    @Test
    public void shouldChainToReturnConditionChainSectionRegardlessContext() {

        // when
        QuerySection result = multiMatchConditionSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.CONDITION_CHAIN));
    }

    @Test
    public void shouldForSectionReturnMultiMatchConditionSection() {

        // when
        QuerySection result = multiMatchConditionSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.MULTI_MATCH_CONDITION));
    }
}
