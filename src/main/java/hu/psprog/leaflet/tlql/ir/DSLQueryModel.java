package hu.psprog.leaflet.tlql.ir;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Intermediate Representation (IR) model class for TLQL.
 *
 * @author Peter Smith
 */
public class DSLQueryModel {

    private final List<DSLConditionGroup> conditionGroups = new LinkedList<>();
    private final Map<DSLObject, DSLOrderDirection> ordering = new LinkedHashMap<>();
    private int offset;
    private int limit;

    public List<DSLConditionGroup> getConditionGroups() {
        return conditionGroups;
    }

    public Map<DSLObject, DSLOrderDirection> getOrdering() {
        return ordering;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DSLQueryModel that = (DSLQueryModel) o;

        return new EqualsBuilder()
                .append(offset, that.offset)
                .append(limit, that.limit)
                .append(conditionGroups, that.conditionGroups)
                .append(ordering, that.ordering)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(conditionGroups)
                .append(ordering)
                .append(offset)
                .append(limit)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("conditionGroups", conditionGroups)
                .append("ordering", ordering)
                .append("offset", offset)
                .append("limit", limit)
                .toString();
    }
}
