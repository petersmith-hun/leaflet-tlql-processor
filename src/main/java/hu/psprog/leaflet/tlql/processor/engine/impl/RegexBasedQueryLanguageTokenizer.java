package hu.psprog.leaflet.tlql.processor.engine.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import hu.psprog.leaflet.tlql.processor.engine.QueryLanguageTokenizer;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link QueryLanguageTokenizer}.
 *
 * @author Peter Smith
 */
@Component
public class RegexBasedQueryLanguageTokenizer implements QueryLanguageTokenizer {

    @Override
    public List<ParsedToken> tokenize(String inputQuery) {

        Map<Integer, ParsedToken> matches = Stream.of(QueryLanguageToken.values())
                .filter(queryLanguageToken -> queryLanguageToken != QueryLanguageToken.TERMINATOR)
                .flatMap(queryLanguageToken -> queryLanguageToken.match(inputQuery).stream())
                .collect(Collectors.groupingBy(ParsedToken::getStartIndex))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, this::selectTopByPrecedence, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        List<ParsedToken> cleanedMatches = new LinkedList<>();
        ParsedToken lastMatch = null;
        for (ParsedToken currentMatch : matches.values()) {
            if (Objects.nonNull(lastMatch) && currentMatch.getStartIndex() < lastMatch.getEndIndex()) {
                continue;
            }

            cleanedMatches.add(currentMatch);
            lastMatch = currentMatch;
        }

        return cleanedMatches;
    }

    private ParsedToken selectTopByPrecedence(Map.Entry<Integer, List<ParsedToken>> tokenMatchEntry) {

        Optional<ParsedToken> selectedTokenMatch = tokenMatchEntry.getValue().stream()
                .min(Comparator.comparing(parsedToken -> parsedToken.getToken().getPrecedence()));

        if (selectedTokenMatch.isPresent()) {
            return selectedTokenMatch.get();
        } else {
            throw new DSLParserException("Minimum item cannot be picked by precedence");
        }
    }
}
