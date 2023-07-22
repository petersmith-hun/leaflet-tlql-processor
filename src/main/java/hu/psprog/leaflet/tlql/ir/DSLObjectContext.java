package hu.psprog.leaflet.tlql.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Helper class for holding contextual information for a {@link DSLObject}.
 *
 * @author Peter Smith
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DSLObjectContext {

    private DSLObject object;
    private String specialization;
}
