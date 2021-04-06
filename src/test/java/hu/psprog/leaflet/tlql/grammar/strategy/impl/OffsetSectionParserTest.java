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
 * Unit tests for {@link OffsetSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class OffsetSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private OffsetSectionParser offsetSectionParser;

    @Test
    public void shouldParseSectionAdvanceContextByDiscardingLimitKeywordTokenAndReadingItsValue() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("offset 50 with");
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_OFFSET));
        assertThat(grammarParserContext.getUpcomingToken(), equalTo(QueryLanguageToken.LITERAL_NUMBER));

        // when
        offsetSectionParser.parseSection(grammarParserContext);

        // then
        assertThat(grammarParserContext.getNextToken(), equalTo(QueryLanguageToken.KEYWORD_WITH));
        assertThat(grammarParserContext.getQueryModel().getOffset(), equalTo(50));
    }

    @Test
    public void shouldChainToReturnMainSectionRegardlessContext() {

        // when
        QuerySection result = offsetSectionParser.chainTo(null);

        // then
        assertThat(result, equalTo(QuerySection.MAIN));
    }

    @Test
    public void shouldForSectionReturnOffsetSection() {

        // when
        QuerySection result = offsetSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.OFFSET));
    }
}
