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
 * Unit tests for {@link LimitSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class LimitSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private LimitSectionParser limitSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingLimitKeywordTokenAndReadingItsValue() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("limit 100 with");
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_LIMIT));
        assertThat(grammarParserContext.getUpcomingToken(), equalTo(QueryLanguageToken.LITERAL_NUMBER));

        // when
        limitSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_WITH));
        assertThat(grammarParserContext.getQueryModel().getLimit(), equalTo(100));
    }

    @Test
    public void shouldChainToReturnMainSectionRegardlessContext() {

        // when
        QuerySection result = limitSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.MAIN));
    }

    @Test
    public void shouldForSectionReturnLimitSection() {

        // when
        QuerySection result = limitSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.LIMIT));
    }
}
