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
 * Unit tests for {@link SearchSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class SearchSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private SearchSectionParser searchSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingSearchKeywordToken() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("search with conditions");
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_SEARCH));
        assertThat(grammarParserContext.getUpcomingToken(), equalTo(QueryLanguageToken.KEYWORD_WITH));

        // when
        searchSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_WITH));
    }

    @Test
    public void shouldChainToReturnWithSectionRegardlessContext() {

        // when
        QuerySection result = searchSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.WITH));
    }

    @Test
    public void shouldForSectionReturnSearchSection() {

        // when
        QuerySection result = searchSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.SEARCH));
    }
}
