package fr.bobinho.beconomy.wrapper;

import fr.bobinho.beconomy.api.validate.BValidate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper of multi-valued attribute
 */
public class MultiValuedAttribute<T> {

    /**
     * Fields
     */
    private final List<T> values = new ArrayList<>();

    /**
     * Gets the value at index i of the multi-valued attribute
     *
     * @param i the index of the value
     * @return the value at index i of the multi-valued attribute
     */
    @Nonnull
    public T get(int i) {
        return values.get(i);
    }

    /**
     * Adds a new value to the multi-valued attribute
     *
     * @param value the value
     */
    public void add(@Nonnull T value) {
        BValidate.notNull(value);

        values.add(value);
    }

    /**
     * Removes a value from the multi-valued attribute
     *
     * @param value the value
     */
    public void remove(@Nonnull T value) {
        BValidate.notNull(value);

        values.remove(value);
    }

}
