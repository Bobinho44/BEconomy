package fr.bobinho.beconomy.wrapper;

import fr.bobinho.beconomy.api.validate.BValidate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Wrapper of read only mono-valued attribute
 */
public class ReadOnlyMonoValuedAttribute<T> {

    /**
     * Fields
     */
    protected T value;

    /**
     * Creates a new not empty read only mono-valued attribute
     *
     * @param value the initial wrapper value
     */
    public ReadOnlyMonoValuedAttribute(@Nonnull T value) {
        BValidate.notNull(value);

        this.value = value;
    }

    /**
     * Gets the wrapper value
     *
     * @return the wrapper value
     */
    @NotNull
    public T get() {
        return value;
    }

}
