package hu.psprog.leaflet.tlql.grammar;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import hu.psprog.leaflet.tlql.ir.DSLConditionGroup;
import hu.psprog.leaflet.tlql.ir.DSLObject;
import hu.psprog.leaflet.tlql.ir.DSLOrderDirection;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Function;

/**
 * TLQL parser context containing every necessary information for query parsing.
 *
 * @author Peter Smith
 */
public class GrammarParserContext {

    private final DSLQueryModel queryModel = new DSLQueryModel();
    private final Stack<ParsedToken> tokenSequence = new Stack<>();

    private ParsedToken lookAheadFirst;
    private ParsedToken lookAheadSecond;
    private DSLCondition currentCondition;
    private DSLConditionGroup currentConditionGroup;
    private boolean conditionGroupOpen = false;

    /**
     * Creates a {@link GrammarParserContext} object based on the passed list of {@link ParsedToken} objects.
     * Initialization fills up the initial sequence stack and the look-ahead tokens (2-level).
     *
     * @param tokenList list of {@link ParsedToken} object to be used in the context
     */
    public GrammarParserContext(List<ParsedToken> tokenList) {
        loadSequenceStack(tokenList);
        prepareLookaheads();
    }

    public ParsedToken readToken(QueryLanguageToken token) {

        assertExpectedToken(token);

        return lookAheadFirst;
    }

    public ParsedToken readToken(QueryLanguageToken.TokenGroup tokenGroup) {

        assertExpectedToken(tokenGroup);

        return lookAheadFirst;
    }

    public void discardToken() {

        lookAheadFirst = lookAheadSecond.clone();
        lookAheadSecond = tokenSequence.isEmpty()
                ? new ParsedToken(QueryLanguageToken.TERMINATOR, "")
                : tokenSequence.pop();
    }

    public void discardToken(QueryLanguageToken token) {

        assertExpectedToken(token);
        discardToken();
    }

    public void discardToken(QueryLanguageToken.TokenGroup tokenGroup) {

        assertExpectedToken(tokenGroup);
        discardToken();
    }

    public DSLCondition createDSLCondition() {

        if (Objects.isNull(currentConditionGroup)) {
            currentConditionGroup = new DSLConditionGroup();
            queryModel.getConditionGroups().add(currentConditionGroup);
        }

        currentCondition = new DSLCondition();
        currentConditionGroup.getConditions().add(currentCondition);

        return currentCondition;
    }

    public void openConditionGroup() {

        if (!conditionGroupOpen) {
            DSLConditionGroup conditionGroup = new DSLConditionGroup(currentConditionGroup);
            queryModel.getConditionGroups().add(conditionGroup);
            currentConditionGroup = conditionGroup;
            conditionGroupOpen = true;
        }
    }

    public void closeConditionGroup() {

        if (conditionGroupOpen) {
            currentConditionGroup = currentConditionGroup.getParentGroup();
            conditionGroupOpen = false;
            openConditionGroup();
        }
    }

    public void addOrderingAndAdvance() {

        DSLObject object = DSLMapping.TOKEN_TO_OBJECT_MAP.get(readToken(QueryLanguageToken.TokenGroup.OBJECT).getToken());
        discardToken();
        DSLOrderDirection direction = DSLMapping.TOKEN_TO_ORDER_DIRECTION_MAP.get(readToken(QueryLanguageToken.TokenGroup.KEYWORD).getToken());
        discardToken();

        queryModel.getOrdering().put(object, direction);
    }

    public DSLQueryModel getQueryModel() {
        return queryModel;
    }

    public DSLCondition getCurrentCondition() {
        return currentCondition;
    }

    public DSLConditionGroup getCurrentConditionGroup() {
        return currentConditionGroup;
    }

    public QueryLanguageToken getNextToken() {
        return lookAheadFirst.getToken();
    }

    public QueryLanguageToken getUpcomingToken() {
        return lookAheadSecond.getToken();
    }

    public ParsedToken getLookAheadFirst() {
        return lookAheadFirst;
    }

    public ParsedToken getLookAheadSecond() {
        return lookAheadSecond;
    }

    public <T> T extractValueAndAdvance(QueryLanguageToken assertToken, Function<String, T> mapperFunction) {

        T value = assertNonNullValue(mapperFunction.apply(readToken(assertToken).getValue()));
        discardToken();

        return value;
    }

    public <T> T extractValueAndAdvance(Function<QueryLanguageToken, T> valueFunction) {

        T value = assertNonNullValue(valueFunction.apply(lookAheadFirst.getToken()));
        discardToken();

        return value;
    }

    private <T> T assertNonNullValue(T value) {

        if (Objects.isNull(value)) {
            throw new DSLParserException(String.format("Null value extracted near %s %s", lookAheadFirst.getToken(), lookAheadSecond.getToken()));
        }

        return value;
    }

    private void loadSequenceStack(List<ParsedToken> tokenList) {
        new LinkedList<>(tokenList)
                .descendingIterator()
                .forEachRemaining(tokenSequence::push);
    }

    private void prepareLookaheads() {
        lookAheadFirst = tokenSequence.pop();
        lookAheadSecond = tokenSequence.pop();
    }

    private void assertExpectedToken(QueryLanguageToken token) {

        if (lookAheadFirst.getToken() != token) {
            throw new DSLParserException(String.format("Expected token %s but found %s of '%s'",
                    token, lookAheadFirst.getToken(), lookAheadFirst.getValue()));
        }
    }

    private void assertExpectedToken(QueryLanguageToken.TokenGroup tokenGroup) {

        if (lookAheadFirst.getToken().getGroup() != tokenGroup) {
            throw new DSLParserException(String.format("Expected token of group %s but found %s of '%s'",
                    tokenGroup, lookAheadFirst.getToken().getGroup(), lookAheadFirst.getValue()));
        }
    }
}
