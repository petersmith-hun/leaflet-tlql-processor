package hu.psprog.leaflet.tlql.ir;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Intermediate Representation (IR) model class for TLQL.
 *
 * @author Peter Smith
 */
@Data
public class DSLQueryModel {

    private final List<DSLConditionGroup> conditionGroups = new LinkedList<>();
    private final Map<DSLObject, DSLOrderDirection> ordering = new LinkedHashMap<>();
    private int offset;
    private int limit;

}
