package hu.psprog.leaflet.tlql.ir;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Condition representation in IR.
 *
 * @author Peter Smith
 */
public class DSLCondition {

    private DSLObject object;
    private DSLOperator operator;
    private String value;
    private List<String> multipleValue;
    private DSLTimestampValue timestampValue;
    private DSLLogicalOperator nextConditionOperator;

    public DSLObject getObject() {
        return object;
    }

    public void setObject(DSLObject object) {
        this.object = object;
    }

    public DSLOperator getOperator() {
        return operator;
    }

    public void setOperator(DSLOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getMultipleValue() {
        return multipleValue;
    }

    public void setMultipleValue(List<String> multipleValue) {
        this.multipleValue = multipleValue;
    }

    public DSLTimestampValue getTimestampValue() {
        return timestampValue;
    }

    public void setTimestampValue(DSLTimestampValue timestampValue) {
        this.timestampValue = timestampValue;
    }

    public DSLLogicalOperator getNextConditionOperator() {
        return nextConditionOperator;
    }

    public void setNextConditionOperator(DSLLogicalOperator nextConditionOperator) {
        this.nextConditionOperator = nextConditionOperator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DSLCondition that = (DSLCondition) o;

        return new EqualsBuilder()
                .append(object, that.object)
                .append(operator, that.operator)
                .append(value, that.value)
                .append(multipleValue, that.multipleValue)
                .append(timestampValue, that.timestampValue)
                .append(nextConditionOperator, that.nextConditionOperator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(object)
                .append(operator)
                .append(value)
                .append(multipleValue)
                .append(timestampValue)
                .append(nextConditionOperator)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("object", object)
                .append("operator", operator)
                .append("value", value)
                .append("multipleValue", multipleValue)
                .append("timestampValue", timestampValue)
                .append("nextConditionOperator", nextConditionOperator)
                .toString();
    }
}
