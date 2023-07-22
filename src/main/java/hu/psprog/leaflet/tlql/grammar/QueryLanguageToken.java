package hu.psprog.leaflet.tlql.grammar;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Available tokens of TLQL.
 *
 * @author Peter Smith
 */
public enum QueryLanguageToken {

    KEYWORD_SEARCH("search", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_WITH("with", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_CONDITIONS("conditions", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_ORDER("order", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_BY("by", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_ASC("asc", 2, TokenGroup.KEYWORD, 0),
    KEYWORD_DESC("desc", 2, TokenGroup.KEYWORD, 0),
    KEYWORD_ASCENDING("ascending", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_DESCENDING("descending", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_THEN("then", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_LIMIT("limit", 1, TokenGroup.KEYWORD, 0),
    KEYWORD_OFFSET("offset", 1, TokenGroup.KEYWORD, 0),

    OBJECT_SOURCE("source", 1, TokenGroup.OBJECT, 0),
    OBJECT_LEVEL("level", 1, TokenGroup.OBJECT, 0),
    OBJECT_MESSAGE("message", 1, TokenGroup.OBJECT, 0),
    OBJECT_TIMESTAMP("timestamp", 1, TokenGroup.OBJECT, 0),
    OBJECT_LOGGER("logger", 1, TokenGroup.OBJECT, 0),
    OBJECT_THREAD("thread", 1, TokenGroup.OBJECT, 0),
    OBJECT_CONTEXT("([a-zA-Z-_]+)", 10, TokenGroup.OBJECT, 0),

    OPERATOR_EQUAL("=", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_NOT_EQUAL("!=", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_LIKE("~", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_GREATER(">", 2, TokenGroup.OPERATOR, 0),
    OPERATOR_GREATER_OR_EQUAL(">=", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_LESS("<", 2, TokenGroup.OPERATOR, 0),
    OPERATOR_LESS_OR_EQUAL("<=", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_EITHER("either", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_NONE("none", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_BETWEEN("between", 1, TokenGroup.OPERATOR, 0),
    OPERATOR_AND("and", 1, TokenGroup.LOGICAL_OPERATOR, 0),
    OPERATOR_OR("or", 1, TokenGroup.LOGICAL_OPERATOR, 0),

    SYMBOL_OPENING_BRACKET("\\(", 1, TokenGroup.SYMBOL, 0),
    SYMBOL_CLOSING_BRACKET("\\)", 1, TokenGroup.SYMBOL, 0),
    SYMBOL_OPENING_SQUARE_BRACKET("\\[", 1, TokenGroup.SYMBOL, 0),
    SYMBOL_CLOSING_SQUARE_BRACKET("\\]", 1, TokenGroup.SYMBOL, 0),
    SYMBOL_COMMA(",", 1, TokenGroup.SYMBOL, 0),

    LITERAL_STRING_APOSTROPHE("'([^']*)'", 1, TokenGroup.LITERAL, 1),
    LITERAL_STRING_QUOTE("\"([^\"]*)\"", 1, TokenGroup.LITERAL, 1),
    LITERAL_NUMBER("[0-9]+(\\.[0-9]+)?", 3, TokenGroup.LITERAL, 0),
    LITERAL_DATE("([0-9]{4}-[0-9]{2}-[0-9]{2})", 2, TokenGroup.LITERAL, 0),
    LITERAL_DATETIME("([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{1,2}:[0-9]{2}:[0-9]{2})", 1, TokenGroup.LITERAL, 0),

    TERMINATOR("", 0, TokenGroup.TERMINATOR, 0);

    private final Pattern tokenMatcher;
    private final int precedence;
    private final TokenGroup group;
    private final int grabGroupIndex;

    QueryLanguageToken(String tokenMatcher, int precedence, TokenGroup group, int grabGroupIndex) {
        this.tokenMatcher = Pattern.compile(tokenMatcher, Pattern.CASE_INSENSITIVE);
        this.precedence = precedence;
        this.group = group;
        this.grabGroupIndex = grabGroupIndex;
    }

    public Pattern getTokenMatcher() {
        return tokenMatcher;
    }

    public int getPrecedence() {
        return precedence;
    }

    /**
     * Tries to match the given input string against this token.
     * Matches are then collected and returned as list of {@link ParsedToken} objects.
     *
     * @param inputString input TLQL query string
     * @return matches as {@link List} of {@link ParsedToken} objects
     */
    public List<ParsedToken> match(String inputString) {

        return tokenMatcher.matcher(inputString)
                .results()
                .map(matchResult -> new ParsedToken(this, matchResult))
                .collect(Collectors.toList());
    }

    public boolean isKeyword() {
        return group == TokenGroup.KEYWORD;
    }

    public boolean isObject() {
        return group == TokenGroup.OBJECT;
    }

    public boolean isOperator() {
        return group == TokenGroup.OPERATOR;
    }

    public boolean isLogicalOperator() {
        return group == TokenGroup.LOGICAL_OPERATOR;
    }

    public TokenGroup getGroup() {
        return group;
    }

    public int getGrabGroupIndex() {
        return grabGroupIndex;
    }

    public enum TokenGroup {
        KEYWORD,
        OBJECT,
        OPERATOR,
        LOGICAL_OPERATOR,
        SYMBOL,
        LITERAL,
        TERMINATOR
    }
}
