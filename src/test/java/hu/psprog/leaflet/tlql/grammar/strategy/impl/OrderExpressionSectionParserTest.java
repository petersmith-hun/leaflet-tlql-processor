package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLObject;
import hu.psprog.leaflet.tlql.ir.DSLOrderDirection;
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
 * Unit tests for {@link OrderExpressionSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class OrderExpressionSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private OrderExpressionSectionParser orderExpressionSectionParser;

    @ParameterizedTest
    @MethodSource("orderExpressionParseSectionArgumentsSource")
    public void shouldParseSectionAdvanceContextByReadingOrderExpression(String queryPart, DSLObject expectedOrderByObject,
                                                                         DSLOrderDirection expectedOrderDirection,
                                                                         QueryLanguageToken expectedNextToken) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        orderExpressionSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getQueryModel().getOrdering().get(expectedOrderByObject), equalTo(expectedOrderDirection));
        assertThat(grammarParserContext.getNextToken(), equalTo(expectedNextToken));
    }

    @Test
    public void shouldParseSectionThrowExceptionWhenNextTokenIsNotObject() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("limit 100");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> orderExpressionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldParseSectionThrowExceptionWhenUpcomingTokenIsNotDirectionKeyword() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("timestamp >");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> orderExpressionSectionParser.parseSection(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldChainToReturnOrderChainSectionRegardlessContext() {

        // when
        QuerySection result = orderExpressionSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.ORDER_CHAIN));
    }

    @Test
    public void shouldForSectionReturnOrderExpressionSection() {

        // when
        QuerySection result = orderExpressionSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.ORDER_EXPRESSION));
    }

    private static Stream<Arguments> orderExpressionParseSectionArgumentsSource() {

        return Stream.of(
                Arguments.of("timestamp desc with limit", DSLObject.TIMESTAMP, DSLOrderDirection.DESC, QueryLanguageToken.KEYWORD_WITH),
                Arguments.of("source asc then", DSLObject.SOURCE, DSLOrderDirection.ASC, QueryLanguageToken.KEYWORD_THEN),
                Arguments.of("timestamp descending", DSLObject.TIMESTAMP, DSLOrderDirection.DESC, QueryLanguageToken.TERMINATOR),
                Arguments.of("source ascending", DSLObject.SOURCE, DSLOrderDirection.ASC, QueryLanguageToken.TERMINATOR)
        );
    }
}
