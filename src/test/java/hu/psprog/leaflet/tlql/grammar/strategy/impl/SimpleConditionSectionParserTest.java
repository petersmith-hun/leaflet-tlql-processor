package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import hu.psprog.leaflet.tlql.ir.DSLObject;
import hu.psprog.leaflet.tlql.ir.DSLOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link SimpleConditionSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class SimpleConditionSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private SimpleConditionSectionParser simpleConditionSectionParser;

    @Test
    public void shouldParseSectionThrowExceptionIfNextTokenIsNotObject() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("with limit");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> simpleConditionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldParseSectionThrowExceptionIfUpcomingTokenIsNotOperator() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("source with");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> simpleConditionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @ParameterizedTest
    @MethodSource("conditionExpressionArgumentsSource")
    public void shouldParseSectionAdvanceContextByReadingSimpleConditionExpression(String queryPart, DSLCondition expectedCondition) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        simpleConditionSectionParser.processCondition(grammarParserContext);

        // then
        assertThat(grammarParserContext.getCurrentCondition(), equalTo(expectedCondition));
    }

    @Test
    public void shouldChainToReturnConditionChainSectionRegardlessContext() {

        // when
        QuerySection result = simpleConditionSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.CONDITION_CHAIN));
    }

    @Test
    public void shouldForSectionReturnSimpleConditionSection() {

        // when
        QuerySection result = simpleConditionSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.SIMPLE_CONDITION));
    }

    private static Stream<Arguments> conditionExpressionArgumentsSource() {

        return Stream.of(
                Arguments.of("source = 'lcfa'", prepareDSLCondition(DSLObject.SOURCE, DSLOperator.EQUALS, "lcfa")),
                Arguments.of("level != 'info'", prepareDSLCondition(DSLObject.LEVEL, DSLOperator.NOT_EQUALS, "info")),
                Arguments.of("message ~ 'something wrong'", prepareDSLCondition(DSLObject.MESSAGE, DSLOperator.LIKE, "something wrong"))
        );
    }

    private static DSLCondition prepareDSLCondition(DSLObject object, DSLOperator operator, String value) {

        DSLCondition dslCondition = new DSLCondition();
        dslCondition.setObject(object);
        dslCondition.setOperator(operator);
        dslCondition.setValue(value);

        return dslCondition;
    }
}
