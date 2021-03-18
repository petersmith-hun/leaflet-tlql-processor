package hu.psprog.leaflet.tlql.processor.impl;

import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;
import hu.psprog.leaflet.tlql.processor.TLQLProcessorService;
import hu.psprog.leaflet.tlql.processor.engine.impl.StrategyBasedQueryLanguageParser;
import hu.psprog.leaflet.tlql.processor.engine.impl.RegexBasedQueryLanguageTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link TLQLProcessorService}.
 *
 * @author Peter Smith
 */
@Service
public class TLQLProcessorServiceImpl implements TLQLProcessorService {

    private final StrategyBasedQueryLanguageParser strategyBasedQueryLanguageParser;
    private final RegexBasedQueryLanguageTokenizer regexBasedQueryLanguageTokenizer;

    @Autowired
    public TLQLProcessorServiceImpl(StrategyBasedQueryLanguageParser strategyBasedQueryLanguageParser, RegexBasedQueryLanguageTokenizer regexBasedQueryLanguageTokenizer) {
        this.strategyBasedQueryLanguageParser = strategyBasedQueryLanguageParser;
        this.regexBasedQueryLanguageTokenizer = regexBasedQueryLanguageTokenizer;
    }

    @Override
    public DSLQueryModel parse(String inputQuery) {

        List<ParsedToken> parsedTokenList = regexBasedQueryLanguageTokenizer.tokenize(inputQuery);

        return strategyBasedQueryLanguageParser.parse(parsedTokenList);
    }
}
