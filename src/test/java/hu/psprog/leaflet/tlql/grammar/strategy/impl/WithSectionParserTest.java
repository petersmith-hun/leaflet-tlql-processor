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
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link WithSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class WithSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private WithSectionParser withSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingWithKeywordToken() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("with conditions");
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_WITH));
        assertThat(grammarParserContext.getUpcomingToken(), equalTo(QueryLanguageToken.KEYWORD_CONDITIONS));

        // when
        withSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_CONDITIONS));
    }

    @ParameterizedTest
    @MethodSource("withSectionChainToArgumentsSource")
    public void shouldChainToRespondBasedOnContext(String queryPart, QuerySection expectedTargetSection) {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext(queryPart);

        // when
        QuerySection result = withSectionParser.chainTo(grammarParserContext);

        // then
        assertThat(result, equalTo(expectedTargetSection));
    }

    @Test
    public void shouldChainToRaiseExceptionOnInvalidNextToken() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("timestamp > 2021-03-22");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> withSectionParser.chainTo(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldForSectionReturnWithSection() {

        // when
        QuerySection result = withSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.WITH));
    }

    private static Stream<Arguments> withSectionChainToArgumentsSource() {

        return Stream.of(
                Arguments.of("conditions source", QuerySection.CONDITIONS),
                Arguments.of("order by", QuerySection.ORDER_BY),
                Arguments.of("limit 100", QuerySection.LIMIT),
                Arguments.of("offset 50", QuerySection.OFFSET)
        );
    }
}
