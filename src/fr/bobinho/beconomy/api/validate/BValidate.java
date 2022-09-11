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

    /**
     * Validates the truth expression status
     */
    public static void isTrue(boolean expression) {
        Validate.isTrue(expression, "The expression cannot be false!");
    }

    /**
     * Validates the falsehood expression status
     */
    public static void isFalse(boolean expression) {
        Validate.isTrue(!expression, "The expression cannot be true!");
    }

}
