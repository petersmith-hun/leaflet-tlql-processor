package hu.psprog.leaflet.tlql.processor.engine.impl;

import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.ParsedToken;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySectionParser;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;
import hu.psprog.leaflet.tlql.processor.engine.QueryLanguageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of {@link QueryLanguageParser}.
 *
 * @author Peter Smith
 */
@Component
public class StrategyBasedQueryLanguageParser implements QueryLanguageParser {

    private final Map<QuerySection, QuerySectionParser> sectionParserMap;

    @Autowired
    public StrategyBasedQueryLanguageParser(List<QuerySectionParser> querySectionParserList) {
        this.sectionParserMap = querySectionParserList.stream()
                .collect(Collectors.toMap(QuerySectionParser::forSection, Function.identity()));
    }

    @Override
    public DSLQueryModel parse(List<ParsedToken> tokenList) {

        GrammarParserContext context = new GrammarParserContext(tokenList);
        QuerySection currentQuerySection = QuerySection.SEARCH;

        do {
            QuerySectionParser querySectionParser = sectionParserMap.get(currentQuerySection);
            querySectionParser.parseSection(context);
            currentQuerySection = querySectionParser.chainTo(context);
        } while (Objects.nonNull(currentQuerySection));

        return context.getQueryModel();
    }
}
