package hu.psprog.leaflet.tlql.grammar;

import hu.psprog.leaflet.tlql.exception.DSLParserException;
import hu.psprog.leaflet.tlql.ir.DSLCondition;
import hu.psprog.leaflet.tlql.ir.DSLConditionGroup;
import hu.psprog.leaflet.tlql.ir.DSLObject;
import hu.psprog.leaflet.tlql.ir.DSLObjectContext;
import hu.psprog.leaflet.tlql.ir.DSLOrderDirection;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;

import java.util.EmptyStackException;
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
    private boolean conditionGroupCloseSeen = true;

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

    /**
     * Returns current first look-ahead {@link ParsedToken} after verifying its token type.
     *
     * @param token {@link QueryLanguageToken} enum constant to verify current {@link ParsedToken} against
     * @return current first look-ahead {@link ParsedToken} object
     */
    public ParsedToken readToken(QueryLanguageToken token) {

        assertExpectedToken(token);

        return lookAheadFirst;
    }

    /**
     * Returns current first look-ahead {@link ParsedToken} after verifying its token type.
     *
     * @param tokenGroup {@link QueryLanguageToken.TokenGroup} to verify current {@link ParsedToken} against
     * @return current first look-ahead {@link ParsedToken} object
     */
    public ParsedToken readToken(QueryLanguageToken.TokenGroup tokenGroup) {

        assertExpectedToken(tokenGroup);

        return lookAheadFirst;
    }

    /**
     * Advances context by overwriting the reference of first look-ahead with the second, and retrieving the next
     * available token from the sequence. If the token sequence is already empty, a termination token is set.
     */
    public void discardToken() {

        lookAheadFirst = lookAheadSecond.clone();
        lookAheadSecond = tokenSequence.isEmpty()
                ? ParsedToken.TERMINATION_TOKEN
                : tokenSequence.pop();
    }

    /**
     * Advances context by overwriting the reference of first look-ahead with the second, and retrieving the next
     * available token from the sequence. If the token sequence is already empty, a termination token is set.
     * Before doing so, verifies current first look-ahead {@link ParsedToken} againts the given {@link QueryLanguageToken}.
     *
     * @param token {@link QueryLanguageToken} enum constant to verify current {@link ParsedToken} against
     */
    public void discardToken(QueryLanguageToken token) {

        assertExpectedToken(token);
        discardToken();
    }

    /**
     * Creates a {@link DSLCondition} object and sets the currentCondition reference to point to this newly created one.
     * If no condition group exists yet, it creates one.
     *
     * @return created {@link DSLCondition} object
     */
    public DSLCondition createDSLCondition() {

        if (Objects.isNull(currentConditionGroup)) {
            openConditionGroup();
        }

        currentCondition = new DSLCondition();
        currentConditionGroup.getConditions().add(currentCondition);

        return currentCondition;
    }

    /**
     * Opens a new condition group if there's no open one yet.
     */
    public void openConditionGroup() {

        if (!conditionGroupOpen || conditionGroupCloseSeen) {
            DSLConditionGroup conditionGroup = new DSLConditionGroup();
            queryModel.getConditionGroups().add(conditionGroup);
            currentConditionGroup = conditionGroup;
            conditionGroupOpen = true;
        }
    }

    /**
     * Marks condition group close symbol as seen.
     */
    public void markConditionGroupCloseAsUnseen() {
        conditionGroupCloseSeen = false;
    }

    /**
     * Closes the currently open condition group.
     */
    public void closeConditionGroup() {

        if (conditionGroupOpen) {
            currentConditionGroup = null;
            conditionGroupOpen = false;
            conditionGroupCloseSeen = true;
        }
    }

    /**
     * Creates an ordering entry in the context and advances.
     */
    public void addOrderingAndAdvance() {

        DSLObject object = DSLMapping.TOKEN_TO_OBJECT_MAP.get(readToken(QueryLanguageToken.TokenGroup.OBJECT).getToken());
        discardToken();
        DSLOrderDirection direction = DSLMapping.TOKEN_TO_ORDER_DIRECTION_MAP.get(readToken(QueryLanguageToken.TokenGroup.KEYWORD).getToken());
        discardToken();

        queryModel.getOrdering().put(object, direction);
    }

    /**
     * Returns query model.
     *
     * @return query model as {@link DSLQueryModel} object
     */
    public DSLQueryModel getQueryModel() {
        return queryModel;
    }

    /**
     * Returns current condition object.
     *
     * @return current condition as {@link DSLCondition} object
     */
    public DSLCondition getCurrentCondition() {
        return currentCondition;
    }

    /**
     * Returns currently open condition group.
     *
     * @return current condition group as {@link DSLConditionGroup} object
     */
    public DSLConditionGroup getCurrentConditionGroup() {
        return currentConditionGroup;
    }

    /**
     * Returns the token type of the first look-ahead.
     *
     * @return token type of the first look-ahead token as {@link QueryLanguageToken}
     */
    public QueryLanguageToken getNextToken() {
        return lookAheadFirst.getToken();
    }

    /**
     * Returns the token type of the second look-ahead.
     *
     * @return token type of the second look-ahead token as {@link QueryLanguageToken}
     */
    public QueryLanguageToken getUpcomingToken() {
        return lookAheadSecond.getToken();
    }

    /**
     * Returns the first look-ahead token.
     *
     * @return first look-ahead token as {@link ParsedToken}
     */
    public ParsedToken getLookAheadFirst() {
        return lookAheadFirst;
    }

    /**
     * Returns the second look-ahead token.
     *
     * @return second look-ahead token as {@link ParsedToken}
     */
    public ParsedToken getLookAheadSecond() {
        return lookAheadSecond;
    }

    /**
     * Extracts value of the current literal token and advances context.
     * Also verifies token types beforehand.
     *
     * @param assertToken {@link QueryLanguageToken} enum constant to verify current {@link ParsedToken} against
     * @param mapperFunction mapper function to map the value to the proper target type
     * @param <T> return type (extracted value's target type)
     * @return extracted value as T
     */
    public <T> T extractValueAndAdvance(QueryLanguageToken assertToken, Function<String, T> mapperFunction) {

        T value = assertNonNullValue(mapperFunction.apply(readToken(assertToken).getValue()));
        discardToken();

        return value;
    }

    /**
     * Extracts value of the current token and maps to a target DSL entity type.
     * Can be used for mapping (logical) operators, objects, etc.
     *
     * @param valueFunction mapper function to map the value to the proper DSL entity type
     * @param <T> return type (extracted value's target type)
     * @return extracted value as T
     */
    public <T> T extractValueAndAdvance(Function<QueryLanguageToken, T> valueFunction) {

        T value = assertNonNullValue(valueFunction.apply(lookAheadFirst.getToken()));
        discardToken();

        return value;
    }

    /**
     * Extracts the current token as object (after verification), then advances the context.
     * Also extracts the contextual information for the given object (if any).
     *
     * @return extracted object and its contextual specification, wrapped as {@link DSLObjectContext}
     */
    public DSLObjectContext extractObjectAndAdvance() {

        assertExpectedToken(QueryLanguageToken.TokenGroup.OBJECT);
        DSLObject object = DSLMapping.TOKEN_TO_OBJECT_MAP.get(lookAheadFirst.getToken());
        String specialization = object.isSpecialized()
                ? lookAheadFirst.getValue()
                : null;

        discardToken();

        return new DSLObjectContext(object, specialization);
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
        try {
            lookAheadFirst = tokenSequence.pop();
            lookAheadSecond = tokenSequence.pop();
        } catch (EmptyStackException exc) {
            throw new DSLParserException("Too few tokens in input query.");
        }
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
