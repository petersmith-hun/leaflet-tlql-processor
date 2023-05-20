package hu.psprog.leaflet.tlql.ir;

import lombok.Data;

import java.util.List;

/**
 * Condition representation in IR.
 *
 * @author Peter Smith
 */
@Data
public class DSLCondition {

    private DSLObject object;
    private DSLOperator operator;
    private String value;
    private List<String> multipleValue;
    private DSLTimestampValue timestampValue;
    private DSLLogicalOperator nextConditionOperator;

}
