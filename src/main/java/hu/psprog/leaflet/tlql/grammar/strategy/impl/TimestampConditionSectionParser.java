package hu.psprog.leaflet.tlql.grammar.strategy.impl;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.grammar.DSLMapping;
import hu.psprog.leaflet.tlql.grammar.GrammarParserContext;
import hu.psprog.leaflet.tlql.grammar.strategy.QuerySection;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import hu.psprog.leaflet.tlql.ir.DSLOperator;
import hu.psprog.leaflet.tlql.ir.DSLTimestampValue;
import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Timestamp condition expression parser.
 * This parser is able to handle condition expressions where the object is "timestamp".
 * Timestamp expressions can be similar to simple conditions or interval based.
 *
 * Represented production rules:
 *  - TIMESTAMP_CONDITION -> timestamp operator timestamp_expression CONDITION_CHAIN
 *  - TIMESTAMP_CONDITION -> timestamp between timestamp_interval_expression CONDITION_CHAIN
 *
 * @author Peter Smith
 */
@Component
public class TimestampConditionSectionParser extends AbstractConditionSectionParser {

    private static final Map<QueryLanguageToken, Function<String, LocalDateTime>> LOCAL_DATE_TIME_PARSER_MAP = Map.of(
            QueryLanguageToken.LITERAL_DATE, value -> LocalDateTime.of(LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIDNIGHT),
            QueryLanguageToken.LITERAL_DATETIME, value -> LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    );

    @Override
    public QuerySection forSection() {
        return QuerySection.TIMESTAMP_CONDITION;
    }

    @Override
    protected void processCondition(GrammarParserContext context) {

        DSLCondition currentCondition = context.createDSLCondition();

        currentCondition.setObjectContext(context.extractObjectAndAdvance());
        currentCondition.setOperator(context.extractValueAndAdvance(DSLMapping.TOKEN_TO_OPERATOR_MAP::get));

        if (currentCondition.getOperator() == DSLOperator.BETWEEN) {
            processBetweenTimestampsCondition(context, currentCondition);
        } else {
            processSimpleTimestampCondition(context, currentCondition);
        }
    }

    private void processBetweenTimestampsCondition(GrammarParserContext context, DSLCondition currentCondition) {

        QueryLanguageToken leftSquareBracket = context.getNextToken();
        context.discardToken();

        LocalDateTime leftTimestamp = extractDateTime(context);
        context.discardToken(QueryLanguageToken.SYMBOL_COMMA);
        LocalDateTime rightTimestamp = extractDateTime(context);

        QueryLanguageToken rightSquareBracket = context.getNextToken();
        context.discardToken();

        DSLTimestampValue.IntervalType intervalType = DSLTimestampValue.IntervalType.mapSymbols(leftSquareBracket, rightSquareBracket);
        currentCondition.setTimestampValue(new DSLTimestampValue(intervalType, leftTimestamp, rightTimestamp));
    }

    private void processSimpleTimestampCondition(GrammarParserContext context, DSLCondition currentCondition) {

        LocalDateTime extractedDateTime = extractDateTime(context);
        currentCondition.setTimestampValue(new DSLTimestampValue(extractedDateTime));
    }

    private LocalDateTime extractDateTime(GrammarParserContext context) {

        Function<String, LocalDateTime> localDateTimeParserFunction = LOCAL_DATE_TIME_PARSER_MAP.get(context.getNextToken());
        if (Objects.isNull(localDateTimeParserFunction)) {
            throw new DSLParserException(String.format("Expected DATE or DATETIME literal, got %s instead", context.getNextToken()));
        }

        return context.extractValueAndAdvance(context.getNextToken(), localDateTimeParserFunction);
    }
}
