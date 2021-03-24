package hu.psprog.leaflet.tlql.grammar;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.regex.MatchResult;

/**
 * Model class wrapping a recognized token and the original string represented this token.
 * In case of "parameter" tokens, value contains the parameter itself.
 *
 * @author Peter Smith
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ParsedToken that = (ParsedToken) o;

        return new EqualsBuilder()
                .append(startIndex, that.startIndex)
                .append(endIndex, that.endIndex)
                .append(token, that.token)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(token)
                .append(value)
                .append(startIndex)
                .append(endIndex)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .append("value", value)
                .append("startIndex", startIndex)
                .append("endIndex", endIndex)
                .toString();
    }

    public ParsedToken clone() {
        try {
            return (ParsedToken) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
