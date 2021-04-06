package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import hu.psprog.leaflet.tlql.ir.DSLObject;
import hu.psprog.leaflet.tlql.ir.DSLOperator;
import hu.psprog.leaflet.tlql.ir.DSLTimestampValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TimestampConditionSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class TimestampConditionSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private TimestampConditionSectionParser timestampConditionSectionParser;

    @Test
    public void shouldParseSectionThrowExceptionIfNextTokenIsNotObject() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("with limit");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> timestampConditionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldParseSectionThrowExceptionIfUpcomingTokenIsNotOperator() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("source with");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> timestampConditionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @ParameterizedTest
    @MethodSource("simpleTimestampConditionExpressionArgumentsSource")
    public void shouldParseSectionAdvanceContextByReadingSimpleTimestampCondition(String queryPart, DSLCondition expectedCondition) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        timestampConditionSectionParser.processCondition(grammarParserContext);

        // then
        assertThat(grammarParserContext.getCurrentCondition(), equalTo(expectedCondition));
    }

    @ParameterizedTest
    @MethodSource("timestampIntervalConditionExpressionArgumentsSource")
    public void shouldParseSectionAdvanceContextByReadingTimestampIntervalCondition(String queryPart, DSLCondition expectedCondition) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        timestampConditionSectionParser.processCondition(grammarParserContext);

        // then
        assertThat(grammarParserContext.getCurrentCondition(), equalTo(expectedCondition));
    }

    @Test
    public void shouldChainToReturnConditionChainSectionRegardlessContext() {

        // when
        QuerySection result = timestampConditionSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.CONDITION_CHAIN));
    }

    @Test
    public void shouldForSectionReturnTimestampConditionSection() {

        // when
        QuerySection result = timestampConditionSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.TIMESTAMP_CONDITION));
    }

    private static Stream<Arguments> simpleTimestampConditionExpressionArgumentsSource() {

        return Stream.of(
                Arguments.of("timestamp > 2021-03-24", prepareDSLCondition(DSLOperator.GREATER_THAN, LocalDateTime.of(2021, 3, 24, 0, 0, 0))),
                Arguments.of("timestamp <= 2020-08-28", prepareDSLCondition(DSLOperator.LESS_THAN_OR_EQUAL, LocalDateTime.of(2020, 8, 28, 0, 0, 0))),
                Arguments.of("timestamp < 2021-03-24 13:30:25", prepareDSLCondition(DSLOperator.LESS_THAN, LocalDateTime.of(2021, 3, 24, 13, 30, 25))),
                Arguments.of("timestamp >= 2020-08-28 21:00:05", prepareDSLCondition(DSLOperator.GREATER_THAN_OR_EQUAL, LocalDateTime.of(2020, 8, 28, 21, 0, 5)))
        );
    }

    private static Stream<Arguments> timestampIntervalConditionExpressionArgumentsSource() {

        return Stream.of(
                Arguments.of("timestamp between [2021-03-24, 2021-03-25]", prepareDSLCondition(
                        DSLTimestampValue.IntervalType.FULL_INCLUSIVE,
                        LocalDateTime.of(2021, 3, 24, 0, 0, 0),
                        LocalDateTime.of(2021, 3, 25, 0, 0, 0))),
                Arguments.of("timestamp between ]2021-03-24, 2021-03-25[", prepareDSLCondition(
                        DSLTimestampValue.IntervalType.FULL_EXCLUSIVE,
                        LocalDateTime.of(2021, 3, 24, 0, 0, 0),
                        LocalDateTime.of(2021, 3, 25, 0, 0, 0))),
                Arguments.of("timestamp between [2021-03-24, 2021-03-25[", prepareDSLCondition(
                        DSLTimestampValue.IntervalType.INCLUSIVE_TO_EXCLUSIVE,
                        LocalDateTime.of(2021, 3, 24, 0, 0, 0),
                        LocalDateTime.of(2021, 3, 25, 0, 0, 0))),
                Arguments.of("timestamp between ]2021-03-24, 2021-03-25]", prepareDSLCondition(
                        DSLTimestampValue.IntervalType.EXCLUSIVE_TO_INCLUSIVE,
                        LocalDateTime.of(2021, 3, 24, 0, 0, 0),
                        LocalDateTime.of(2021, 3, 25, 0, 0, 0))),
                Arguments.of("timestamp between [2021-03-24 11:00:30, 2021-03-25 13:30:15]", prepareDSLCondition(
                        DSLTimestampValue.IntervalType.FULL_INCLUSIVE,
                        LocalDateTime.of(2021, 3, 24, 11, 0, 30),
                        LocalDateTime.of(2021, 3, 25, 13, 30, 15))),
                Arguments.of("timestamp between [2021-03-24, 2021-03-25 13:30:15[", prepareDSLCondition(
                        DSLTimestampValue.IntervalType.INCLUSIVE_TO_EXCLUSIVE,
                        LocalDateTime.of(2021, 3, 24, 0, 0, 0),
                        LocalDateTime.of(2021, 3, 25, 13, 30, 15))),
                Arguments.of("timestamp between ]2021-03-24 11:00:30, 2021-03-25]", prepareDSLCondition(
                        DSLTimestampValue.IntervalType.EXCLUSIVE_TO_INCLUSIVE,
                        LocalDateTime.of(2021, 3, 24, 11, 0, 30),
                        LocalDateTime.of(2021, 3, 25, 0, 0, 0)))
        );
    }

    private static DSLCondition prepareDSLCondition(DSLOperator operator, LocalDateTime leftDateTime) {

        DSLCondition dslCondition = new DSLCondition();
        dslCondition.setObject(DSLObject.TIMESTAMP);
        dslCondition.setOperator(operator);
        dslCondition.setTimestampValue(new DSLTimestampValue(DSLTimestampValue.IntervalType.NONE, leftDateTime, null));

        return dslCondition;
    }

    private static DSLCondition prepareDSLCondition(DSLTimestampValue.IntervalType intervalType,
                                                    LocalDateTime leftDateTime, LocalDateTime rightDateTime) {

        DSLCondition dslCondition = new DSLCondition();
        dslCondition.setObject(DSLObject.TIMESTAMP);
        dslCondition.setOperator(DSLOperator.BETWEEN);
        dslCondition.setTimestampValue(new DSLTimestampValue(intervalType, leftDateTime, rightDateTime));

        return dslCondition;
    }
}
