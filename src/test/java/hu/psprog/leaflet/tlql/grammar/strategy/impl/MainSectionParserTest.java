package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link MainSectionParser}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class MainSectionParserTest extends AbstractSectionParserBaseTest {

    @InjectMocks
    private MainSectionParser mainSectionParser;

    @Test
    public void shouldParseSectionDoNothing() {

        // when
        mainSectionParser.parseSection(null);

        // then
        // no effect expected
    }

    @Test
    public void shouldChainToReturnWithSectionForWithToken() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("with conditions");

        // when
        QuerySection result = mainSectionParser.chainTo(grammarParserContext);

        // then
        assertThat(result, equalTo(QuerySection.WITH));
    }

    @Test
    public void shouldChainToReturnNullForTerminatorToken() {

        // given
        GrammarParserContext grammarParserContext =
                new GrammarParserContext(Arrays.asList(ParsedToken.TERMINATION_TOKEN, ParsedToken.TERMINATION_TOKEN));

        // when
        QuerySection result = mainSectionParser.chainTo(grammarParserContext);

        // then
        assertThat(result, nullValue());
    }

    @Test
    public void shouldChainToThrowExceptionForAnyOtherToken() {

        // given
        GrammarParserContext grammarParserContext = prepareGrammarParserContext("order by timestamp");

        // when
        Assertions.assertThrows(DSLParserException.class, () -> mainSectionParser.chainTo(grammarParserContext));

        // then
        // exception expected
    }

    @Test
    public void shouldForSectionReturnMainSection() {

        // when
        QuerySection result = mainSectionParser.forSection();

        // then
        assertThat(result, equalTo(QuerySection.MAIN));
    }
}
