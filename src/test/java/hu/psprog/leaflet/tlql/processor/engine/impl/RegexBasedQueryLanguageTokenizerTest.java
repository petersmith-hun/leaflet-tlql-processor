package hu.psprog.leaflet.tlql.processor.engine.impl;

import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link RegexBasedQueryLanguageTokenizer}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class RegexBasedQueryLanguageTokenizerTest {

    @InjectMocks
    private RegexBasedQueryLanguageTokenizer regexBasedQueryLanguageTokenizer;

    @ParameterizedTest
    @ArgumentsSource(TokenizerArgumentsProvider.class)
    public void shouldTokenizeGivenQueryString(String inputQuery, List<ParsedToken> expectedParsedTokenList) {

        // when
        List<ParsedToken> result = regexBasedQueryLanguageTokenizer.tokenize(inputQuery);

        // then
        assertThat(result, equalTo(expectedParsedTokenList));
    }

    static class TokenizerArgumentsProvider implements ArgumentsProvider {

        private final MatchResult mockedMatchResult = Mockito.mock(MatchResult.class);
        private final static List<QueryLanguageToken> NON_INDEX_ADVANCING_TOKENS = Arrays.asList(
                QueryLanguageToken.SYMBOL_OPENING_BRACKET,
                QueryLanguageToken.SYMBOL_OPENING_SQUARE_BRACKET
        );
        private final static List<QueryLanguageToken> TOKEN_INDEX_DECREASING_TOKENS = Arrays.asList(
                QueryLanguageToken.SYMBOL_COMMA,
                QueryLanguageToken.SYMBOL_CLOSING_BRACKET,
                QueryLanguageToken.SYMBOL_CLOSING_SQUARE_BRACKET
        );
        private int tokenIndex = 0;

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {

            Arguments[] argumentsArray = {

                    // simple limit query
                    prepareArguments("search with limit 100",
                            parsedToken(QueryLanguageToken.KEYWORD_SEARCH),
                            parsedToken(QueryLanguageToken.KEYWORD_WITH),
                            parsedToken(QueryLanguageToken.KEYWORD_LIMIT),
                            parsedToken(QueryLanguageToken.LITERAL_NUMBER, "100")),

                    // offset and limit query with one complex condition
                    prepareArguments("search with conditions source either ('lflt', 'cbfa') with offset 20 limit 10",
                            parsedToken(QueryLanguageToken.KEYWORD_SEARCH),
                            parsedToken(QueryLanguageToken.KEYWORD_WITH),
                            parsedToken(QueryLanguageToken.KEYWORD_CONDITIONS),
                            parsedToken(QueryLanguageToken.OBJECT_SOURCE),
                            parsedToken(QueryLanguageToken.OPERATOR_EITHER),
                            parsedToken(QueryLanguageToken.SYMBOL_OPENING_BRACKET, "("),
                            parsedToken(QueryLanguageToken.LITERAL_STRING_APOSTROPHE, "lflt"),
                            parsedToken(QueryLanguageToken.SYMBOL_COMMA, ","),
                            parsedToken(QueryLanguageToken.LITERAL_STRING_APOSTROPHE, "cbfa"),
                            parsedToken(QueryLanguageToken.SYMBOL_CLOSING_BRACKET, ")"),
                            parsedToken(QueryLanguageToken.KEYWORD_WITH),
                            parsedToken(QueryLanguageToken.KEYWORD_OFFSET),
                            parsedToken(QueryLanguageToken.LITERAL_NUMBER, "20"),
                            parsedToken(QueryLanguageToken.KEYWORD_LIMIT),
                            parsedToken(QueryLanguageToken.LITERAL_NUMBER, "10")),

                    // multi-condition query
                    prepareArguments("search with conditions (timestamp > 2021-03-02 20:26:32 and level = 'error') or source = 'lcfa'",
                            parsedToken(QueryLanguageToken.KEYWORD_SEARCH),
                            parsedToken(QueryLanguageToken.KEYWORD_WITH),
                            parsedToken(QueryLanguageToken.KEYWORD_CONDITIONS),
                            parsedToken(QueryLanguageToken.SYMBOL_OPENING_BRACKET, "("),
                            parsedToken(QueryLanguageToken.OBJECT_TIMESTAMP),
                            parsedToken(QueryLanguageToken.OPERATOR_GREATER),
                            parsedToken(QueryLanguageToken.LITERAL_DATETIME, "2021-03-02 20:26:32"),
                            parsedToken(QueryLanguageToken.OPERATOR_AND),
                            parsedToken(QueryLanguageToken.OBJECT_LEVEL),
                            parsedToken(QueryLanguageToken.OPERATOR_EQUAL),
                            parsedToken(QueryLanguageToken.LITERAL_STRING_APOSTROPHE, "error"),
                            parsedToken(QueryLanguageToken.SYMBOL_CLOSING_BRACKET, ")"),
                            parsedToken(QueryLanguageToken.OPERATOR_OR),
                            parsedToken(QueryLanguageToken.OBJECT_SOURCE),
                            parsedToken(QueryLanguageToken.OPERATOR_EQUAL),
                            parsedToken(QueryLanguageToken.LITERAL_STRING_APOSTROPHE, "lcfa")),

                    // multi-order query
                    prepareArguments("search with order by timestamp asc then source descending",
                            parsedToken(QueryLanguageToken.KEYWORD_SEARCH),
                            parsedToken(QueryLanguageToken.KEYWORD_WITH),
                            parsedToken(QueryLanguageToken.KEYWORD_ORDER),
                            parsedToken(QueryLanguageToken.KEYWORD_BY),
                            parsedToken(QueryLanguageToken.OBJECT_TIMESTAMP),
                            parsedToken(QueryLanguageToken.KEYWORD_ASC),
                            parsedToken(QueryLanguageToken.KEYWORD_THEN),
                            parsedToken(QueryLanguageToken.OBJECT_SOURCE),
                            parsedToken(QueryLanguageToken.KEYWORD_DESCENDING))
            };

            return Stream.of(argumentsArray);
        }

        private Arguments prepareArguments(String inputQuery, ParsedToken... parsedToken) {

            tokenIndex = 0;

            return Arguments.arguments(inputQuery, List.of(parsedToken));
        }

        private ParsedToken parsedToken(QueryLanguageToken token) {

            return parsedToken(token, token.getTokenMatcher().pattern());
        }

        private ParsedToken parsedToken(QueryLanguageToken token, String value) {

            given(mockedMatchResult.group(anyInt())).willReturn(value);

            if (TOKEN_INDEX_DECREASING_TOKENS.contains(token)) {
                tokenIndex--;
            }

            given(mockedMatchResult.start()).willReturn(tokenIndex);
            tokenIndex += value.length();

            if (token == QueryLanguageToken.LITERAL_STRING_QUOTE || token == QueryLanguageToken.LITERAL_STRING_APOSTROPHE) {
                tokenIndex += 2;
            }

            given(mockedMatchResult.end()).willReturn(tokenIndex);

            if (!NON_INDEX_ADVANCING_TOKENS.contains(token)) {
                tokenIndex++;
            }

            return new ParsedToken(token, mockedMatchResult);
        }
    }
}
