package hu.psprog.leaflet.tlql.grammar;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Peter Smith
 */
public class ParsedToken implements Cloneable {

    private final QueryLanguageToken token;
    private final String value;

    public ParsedToken(QueryLanguageToken token, String value) {
        this.token = token;
        this.value = value;
    }

    public QueryLanguageToken getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .append("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ParsedToken that = (ParsedToken) o;

        return new EqualsBuilder()
                .append(token, that.token)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(token)
                .append(value)
                .toHashCode();
    }

    public ParsedToken clone() {
        try {
            return (ParsedToken) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
