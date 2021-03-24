package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
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
 * Unit tests for {@link OrderChainSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class OrderChainSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private OrderChainSectionParser orderChainSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingThenToken() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("then timestamp");

        // when
        orderChainSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.OBJECT_TIMESTAMP));
    }

    @Test
    public void shouldParseSectionShouldDoNothingIfNextTokenIsNotThenKeyword() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("with limit");

        // when
        orderChainSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_WITH));
    }

    @ParameterizedTest
    @MethodSource("orderChainSectionChainToArgumentsSource")
    public void shouldChainToRespondBasedOnContext(String queryPart, QuerySection expectedTargetSection) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        QuerySection result = orderChainSectionParser.chainTo(grammarParserContext);

        // then
        assertThat(result, equalTo(expectedTargetSection));
    }

    @Test
    public void shouldForSectionReturnOrderChainSection() {

        // when
        QuerySection result = orderChainSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.ORDER_CHAIN));
    }

    private static Stream<Arguments> orderChainSectionChainToArgumentsSource() {

        return Stream.of(
                Arguments.of("timestamp desc", QuerySection.ORDER_EXPRESSION),
                Arguments.of("with limit", QuerySection.MAIN)
        );
    }
}
