package hu.psprog.leaflet.tlql.ir;

import hu.psprog.leaflet.tlql.grammar.QueryLanguageToken;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

/**
 * Domain class to handle timestamp (interval) condition values.
 *
 * @author Peter Smith
 */
public class DSLTimestampValue {

    private final IntervalType intervalType;
    private final LocalDateTime leftOrSimple;
    private final LocalDateTime right;

    /**
     * Creates a {@link DSLTimestampValue} object with only its "left" interval boundary.
     * In this case the condition will be as used with simple comparison operators like equal to, greater than, etc.
     *
     * @param simple simple timestamp value as {@link LocalDateTime}
     */
    public DSLTimestampValue(LocalDateTime simple) {
        this.intervalType = IntervalType.NONE;
        this.leftOrSimple = simple;
        this.right = null;
    }

    /**
     * Creates a {@link DSLTimestampValue} object as a bounded interval.
     * In this case, both left and right side of the boundary should be specified, also the boundary type is required.
     * Boundary types can be
     *  * inclusive;
     *  * exclusive;
     *  * inclusive from left and exclusive from right;
     *  * exclusive from left and inclusive from right.
     *
     * @param intervalType interval type as {@link IntervalType} enum constant
     * @param left left boundary timestamp as {@link LocalDateTime}
     * @param right right boundary timestamp as {@link LocalDateTime}
     */
    public DSLTimestampValue(IntervalType intervalType, LocalDateTime left, LocalDateTime right) {
        this.intervalType = intervalType;
        this.leftOrSimple = left;
        this.right = right;
    }

    public IntervalType getIntervalType() {
        return intervalType;
    }

    public LocalDateTime getLeftOrSimple() {
        return leftOrSimple;
    }

    public LocalDateTime getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DSLTimestampValue that = (DSLTimestampValue) o;

        return new EqualsBuilder()
                .append(intervalType, that.intervalType)
                .append(leftOrSimple, that.leftOrSimple)
                .append(right, that.right)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(intervalType)
                .append(leftOrSimple)
                .append(right)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("intervalType", intervalType)
                .append("leftOrSimple", leftOrSimple)
                .append("right", right)
                .toString();
    }

    public enum IntervalType {
        NONE(null, null),
        FULL_INCLUSIVE(QueryLanguageToken.SYMBOL_OPENING_SQUARE_BRACKET, QueryLanguageToken.SYMBOL_CLOSING_SQUARE_BRACKET),
        FULL_EXCLUSIVE(QueryLanguageToken.SYMBOL_CLOSING_SQUARE_BRACKET, QueryLanguageToken.SYMBOL_OPENING_SQUARE_BRACKET),
        INCLUSIVE_TO_EXCLUSIVE(QueryLanguageToken.SYMBOL_OPENING_SQUARE_BRACKET, QueryLanguageToken.SYMBOL_OPENING_SQUARE_BRACKET),
        EXCLUSIVE_TO_INCLUSIVE(QueryLanguageToken.SYMBOL_CLOSING_SQUARE_BRACKET, QueryLanguageToken.SYMBOL_CLOSING_SQUARE_BRACKET);

        private final QueryLanguageToken left;
        private final QueryLanguageToken right;

        IntervalType(QueryLanguageToken left, QueryLanguageToken right) {
            this.left = left;
            this.right = right;
        }

        public static IntervalType mapSymbols(QueryLanguageToken left, QueryLanguageToken right) {

            IntervalType mappedIntervalType = null;
            for (IntervalType currentIntervalType : IntervalType.values()) {
                if (currentIntervalType.left == left && currentIntervalType.right == right) {
                    mappedIntervalType = currentIntervalType;
                    break;
                }
            }

            return mappedIntervalType;
        }
    }
}
