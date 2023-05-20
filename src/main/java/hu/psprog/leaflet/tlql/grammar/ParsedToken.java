package hu.psprog.leaflet.tlql.grammar;

import lombok.Data;

import java.util.regex.MatchResult;

/**
 * Model class wrapping a recognized token and the original string represented this token.
 * In case of "parameter" tokens, value contains the parameter itself.
 *
 * @author Peter Smith
 */
@Data
public class ParsedToken implements Cloneable {

    public static final ParsedToken TERMINATION_TOKEN = new ParsedToken();

    private final QueryLanguageToken token;
    private final String value;
    private final int startIndex;
    private final int endIndex;

    public ParsedToken(QueryLanguageToken token, MatchResult matchResult) {
        this.token = token;
        this.value = matchResult.group(token.getGrabGroupIndex());
        this.startIndex = matchResult.start();
        this.endIndex = matchResult.end();
    }

    private ParsedToken() {
        this.token = QueryLanguageToken.TERMINATOR;
        this.value = null;
        this.startIndex = 0;
        this.endIndex = 0;
    }

    public QueryLanguageToken getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public ParsedToken clone() {
        try {
            return (ParsedToken) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
