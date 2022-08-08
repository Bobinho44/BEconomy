package fr.bobinho.beconomy.wrapper;

import fr.bobinho.beconomy.api.validate.BValidate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Wrapper of mono-valued attribute
 */
public class MonoValuedAttribute<T> extends ReadOnlyMonoValuedAttribute<T> {

    /**
     * Creates a new not empty mono-valued attribute
     *
     * @param value the initial wrapper value
     */
    public MonoValuedAttribute(@NotNull T value) {
        super(value);
    }

    /**
     * Sets the new wrapper value
     *
     * @param value the new wrapper value
     */
    public void set(@Nonnull T value) {
        BValidate.notNull(value);

        this.value = value;
    }

}
