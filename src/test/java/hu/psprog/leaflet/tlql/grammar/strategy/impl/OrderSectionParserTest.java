package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link OrderSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class OrderSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private OrderSectionParser orderSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingOrderAndByKeywordTokens() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("order by timestamp");
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_ORDER));
        assertThat(grammarParserContext.getUpcomingToken(), equalTo(QueryLanguageToken.KEYWORD_BY));

        // when
        orderSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.OBJECT_TIMESTAMP));
    }

    @Test
    public void shouldChainToReturnOrderExpressionSectionRegardlessContext() {

        // when
        QuerySection result = orderSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.ORDER_EXPRESSION));
    }

    @Test
    public void shouldForSectionReturnOrderBySection() {

        // when
        QuerySection result = orderSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.ORDER_BY));
    }
}
