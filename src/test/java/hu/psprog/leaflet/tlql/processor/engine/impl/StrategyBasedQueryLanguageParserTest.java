package hu.psprog.leaflet.tlql.processor.engine.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

/**
 * Unit tests for {@link StrategyBasedQueryLanguageParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class StrategyBasedQueryLanguageParserTest {

    @Mock
    private QuerySectionParser querySectionParser1;

    @Mock
    private QuerySectionParser querySectionParser2;

    @Mock
    private QuerySectionParser querySectionParser3;

    @Mock
    private QuerySectionParser querySectionParser4;

    @Mock
    private MatchResult mockedMatchResult;

    @Captor
    private ArgumentCaptor<GrammarParserContext> grammarParserContextArgumentCaptor;

    private StrategyBasedQueryLanguageParser strategyBasedQueryLanguageParser;

    @BeforeEach
    public void setup() {

        given(querySectionParser1.forSection()).willReturn(QuerySection.SEARCH);
        given(querySectionParser2.forSection()).willReturn(QuerySection.WITH);
        given(querySectionParser3.forSection()).willReturn(QuerySection.CONDITIONS);
        given(querySectionParser4.forSection()).willReturn(QuerySection.ORDER_BY);

        strategyBasedQueryLanguageParser = new StrategyBasedQueryLanguageParser(Arrays.asList(
                querySectionParser1,
                querySectionParser2,
                querySectionParser3,
                querySectionParser4
        ));
    }

    @Test
    public void shouldParseFollowStrategyChain() {

        // given
        ParsedToken firstLookAhead = new ParsedToken(QueryLanguageToken.KEYWORD_SEARCH, mockedMatchResult);
        ParsedToken secondLookAhead = new ParsedToken(QueryLanguageToken.KEYWORD_WITH, mockedMatchResult);
        List<ParsedToken> parsedTokenList = Arrays.asList(firstLookAhead, secondLookAhead, ParsedToken.TERMINATION_TOKEN);

        given(querySectionParser1.chainTo(any(GrammarParserContext.class))).willReturn(QuerySection.WITH);
        given(querySectionParser2.chainTo(any(GrammarParserContext.class)))
                .willReturn(QuerySection.CONDITIONS)
                .willReturn(QuerySection.ORDER_BY);
        given(querySectionParser3.chainTo(any(GrammarParserContext.class))).willReturn(QuerySection.WITH);
        given(querySectionParser4.chainTo(any(GrammarParserContext.class))).willReturn(null);

        // when
        DSLQueryModel result = strategyBasedQueryLanguageParser.parse(parsedTokenList);

        // then
        assertThat(result, notNullValue());
        InOrder querySectionParserOrder = inOrder(querySectionParser1, querySectionParser2,
                querySectionParser3, querySectionParser4);

        Arrays.asList(querySectionParser1, querySectionParser2, querySectionParser3, querySectionParser2, querySectionParser4)
                .forEach(querySectionParser -> querySectionParserOrder.verify(querySectionParser)
                        .parseSection(grammarParserContextArgumentCaptor.capture()));

        assertThat(grammarParserContextArgumentCaptor.getAllValues().size(), equalTo(5));
        grammarParserContextArgumentCaptor.getAllValues().forEach(grammarParserContext -> {
            assertThat(grammarParserContext.getLookAheadFirst(), equalTo(firstLookAhead));
            assertThat(grammarParserContext.getLookAheadSecond(), equalTo(secondLookAhead));
        });

    }
}
