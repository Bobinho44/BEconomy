package fr.bobinho.beconomy.api.validate;

import org.apache.commons.lang.Validate;

/**
 * Bobinho validate library
 */
public final class BValidate {

    /**
     * Unitilizable constructor (utility class)
     */
    private BValidate() {}

    /**
     * Validates the not null object status
     */
    public static void notNull(Object object) {
        Validate.notNull(object, object.getClass().getCanonicalName() + " cannot be null!");
    }

}
