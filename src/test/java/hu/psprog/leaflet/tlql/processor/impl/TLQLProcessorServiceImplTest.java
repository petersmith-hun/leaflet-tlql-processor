package hu.psprog.leaflet.tlql.processor.impl;

import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;
import hu.psprog.leaflet.tlql.processor.engine.impl.RegexBasedQueryLanguageTokenizer;
import hu.psprog.leaflet.tlql.processor.engine.impl.StrategyBasedQueryLanguageParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link TLQLProcessorServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class TLQLProcessorServiceImplTest {

    @Mock
    private StrategyBasedQueryLanguageParser strategyBasedQueryLanguageParser;

    @Mock
    private RegexBasedQueryLanguageTokenizer regexBasedQueryLanguageTokenizer;

    @InjectMocks
    private TLQLProcessorServiceImpl tlqlProcessorService;

    @Test
    public void shouldParseDelegateProcessing() {

        // given
        String query = "search with limit 100";
        List<ParsedToken> parsedTokenList = Collections.emptyList();
        DSLQueryModel dslQueryModel = new DSLQueryModel();

        given(regexBasedQueryLanguageTokenizer.tokenize(query)).willReturn(parsedTokenList);
        given(strategyBasedQueryLanguageParser.parse(parsedTokenList)).willReturn(dslQueryModel);

        // when
        DSLQueryModel result = tlqlProcessorService.parse(query);

        // then
        assertThat(result, equalTo(dslQueryModel));
    }
}
