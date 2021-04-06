package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLLogicalOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for {@link ConditionChainSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ConditionChainSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private ConditionChainSectionParser conditionChainSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByExtractingLogicalOperatorInSameConditionGroup() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("or source");
        grammarParserContext.createDSLCondition();

        // when
        conditionChainSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.OBJECT_SOURCE));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().size(), equalTo(1));
        assertThat(grammarParserContext.getCurrentConditionGroup().getNextConditionGroupOperator(),
                nullValue());
        assertThat(grammarParserContext.getCurrentCondition().getNextConditionOperator(),
                equalTo(DSLLogicalOperator.OR));
        assertConditionGroupStatus(grammarParserContext, true);
    }

    @Test
    public void shouldParseSectionAdvanceContextByExtractingLogicalOperatorForNewConditionGroup() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("and (source");
        grammarParserContext.createDSLCondition();

        // when
        conditionChainSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.OBJECT_SOURCE));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().size(), equalTo(2));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().get(0).getNextConditionGroupOperator(),
                equalTo(DSLLogicalOperator.AND));
        assertThat(grammarParserContext.getCurrentConditionGroup().getNextConditionGroupOperator(), nullValue());
        assertThat(grammarParserContext.getCurrentCondition().getNextConditionOperator(), nullValue());
        assertConditionGroupStatus(grammarParserContext, true);
    }

    @Test
    public void shouldParseSectionAdvanceContextByClosingConditionGroup() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(") with limit");
        grammarParserContext.createDSLCondition();

        // when
        conditionChainSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_WITH));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().size(), equalTo(1));
        assertConditionGroupStatus(grammarParserContext, false);
    }

    @Test
    public void shouldParseSectionAdvanceContextByClosingConditionGroupAndChainingNewGroup() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(") or (source");
        grammarParserContext.createDSLCondition();

        // when
        conditionChainSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.OBJECT_SOURCE));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().size(), equalTo(2));
        assertThat(grammarParserContext.getQueryModel().getConditionGroups().get(0).getNextConditionGroupOperator(),
                equalTo(DSLLogicalOperator.OR));
        assertThat(grammarParserContext.getCurrentConditionGroup().getNextConditionGroupOperator(), nullValue());
        assertThat(grammarParserContext.getCurrentCondition().getNextConditionOperator(), nullValue());
        assertConditionGroupStatus(grammarParserContext, true);
    }

    @ParameterizedTest
    @MethodSource("conditionChainSectionChainToArgumentsSource")
    public void shouldChainToRespondBasedOnContext(String queryPart, QuerySection expectedTargetSection) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        QuerySection result = conditionChainSectionParser.chainTo(grammarParserContext);

        // then
        assertThat(result, equalTo(expectedTargetSection));
    }

    @Test
    public void shouldForSectionReturnConditionChainSection() {

        // when
        QuerySection result = conditionChainSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.CONDITION_CHAIN));
    }

    private void assertConditionGroupStatus(GrammarParserContext grammarParserContext, boolean isOpen) {

        try {
            Field conditionGroupOpenField = ReflectionUtils.findField(GrammarParserContext.class, "conditionGroupOpen");
            conditionGroupOpenField.setAccessible(true);
            assertThat(conditionGroupOpenField.get(grammarParserContext), equalTo(isOpen));
        } catch (NullPointerException | IllegalAccessException e) {
            fail("Failed to check condition group status");
        }
    }

    private static Stream<Arguments> conditionChainSectionChainToArgumentsSource() {

        return Stream.of(
                Arguments.of("timestamp >", QuerySection.TIMESTAMP_CONDITION),
                Arguments.of("source either", QuerySection.MULTI_MATCH_CONDITION),
                Arguments.of("level =", QuerySection.SIMPLE_CONDITION),
                Arguments.of("with limit", QuerySection.MAIN)
        );
    }
}
