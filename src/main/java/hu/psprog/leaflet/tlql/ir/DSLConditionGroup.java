package hu.psprog.leaflet.tlql.ir;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Condition groups used in IR.
 *
 * @author Peter Smith
 */
@Data
public class DSLConditionGroup {

    private final List<DSLCondition> conditions = new LinkedList<>();
    private DSLLogicalOperator nextConditionGroupOperator;

}
