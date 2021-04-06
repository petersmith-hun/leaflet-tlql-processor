package hu.psprog.leaflet.tlql.ir;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * Condition groups used in IR.
 *
 * @author Peter Smith
 */
public class DSLConditionGroup {

    private final List<DSLCondition> conditions = new LinkedList<>();
    private DSLLogicalOperator nextConditionGroupOperator;

    public List<DSLCondition> getConditions() {
        return conditions;
    }

    public DSLLogicalOperator getNextConditionGroupOperator() {
        return nextConditionGroupOperator;
    }

    public void setNextConditionGroupOperator(DSLLogicalOperator nextConditionGroupOperator) {
        this.nextConditionGroupOperator = nextConditionGroupOperator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DSLConditionGroup that = (DSLConditionGroup) o;

        return new EqualsBuilder()
                .append(conditions, that.conditions)
                .append(nextConditionGroupOperator, that.nextConditionGroupOperator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(conditions)
                .append(nextConditionGroupOperator)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("conditions", conditions)
                .append("nextConditionGroupOperator", nextConditionGroupOperator)
                .toString();
    }
}
