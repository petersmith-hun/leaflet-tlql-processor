package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ConditionsSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ConditionsSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private ConditionsSectionParser conditionsSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingConditionsKeywordToken() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("conditions source");
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_CONDITIONS));
        assertThat(grammarParserContext.getUpcomingToken(), equalTo(QueryLanguageToken.OBJECT_SOURCE));

        // when
        conditionsSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.OBJECT_SOURCE));
    }

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingConditionsKeywordTokenAndOpeningConditionGroup() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("conditions (timestamp");
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_CONDITIONS));
        assertThat(grammarParserContext.getUpcomingToken(), equalTo(QueryLanguageToken.SYMBOL_OPENING_BRACKET));

        // when
        conditionsSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.OBJECT_TIMESTAMP));
        assertThat(grammarParserContext.getCurrentConditionGroup(), notNullValue());
    }

    @ParameterizedTest
    @MethodSource("conditionsSectionChainToArgumentsSource")
    public void shouldChainToRespondBasedOnContext(String queryPart, QuerySection expectedTargetSection) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        QuerySection result = conditionsSectionParser.chainTo(grammarParserContext);

        // then
        assertThat(result, equalTo(expectedTargetSection));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "and source",
            "limit timestamp",
            "either level",
            "source (",
            "100 =",
            "search search"
    })
    public void shouldChainToThrowExceptionForInvalidTokensAfterConditions(String queryPart) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        Assertions.assertThrows(DSLParserException.class, () -> conditionsSectionParser.chainTo(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldForSectionReturnConditionsSection() {

        // when
        QuerySection result = conditionsSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.CONDITIONS));
    }

    private static Stream<Arguments> conditionsSectionChainToArgumentsSource() {

        return Stream.of(
                Arguments.of("timestamp >", QuerySection.TIMESTAMP_CONDITION),
                Arguments.of("source either", QuerySection.MULTI_MATCH_CONDITION),
                Arguments.of("level =", QuerySection.SIMPLE_CONDITION)
        );
    }
}
