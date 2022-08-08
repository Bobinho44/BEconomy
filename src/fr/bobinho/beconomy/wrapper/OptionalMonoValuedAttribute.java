package fr.bobinho.beconomy.wrapper;

import fr.bobinho.beconomy.api.validate.BValidate;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Wrapper of optional mono-valued attribute
 */
public class OptionalMonoValuedAttribute<T> {

    /**
     * Fields
     */
    private T value;

    /**
     * Gets the optional mono-valued attribute
     *
     * @return the optional mono-valued attribute
     */
    @Nonnull
    public Optional<T> get() {
        return Optional.ofNullable(value);
    }

    /**
     * Sets the value of the optional mono-valued attribute
     *
     * @param value the value of the optional mono-valued attribute
     */
    public void set(@Nonnull T value) {
        BValidate.notNull(value);

        this.value = value;
    }

    /**
     * Unsets the value of the optional mono-valued attribute
     */
    public void unset() {
        this.value = null;
    }

    /**
     * Checks if a value is present in the optional mono-valued attribute
     *
     * @return true if a value is present in the optional mono-valued attribute, false otherwise
     */
    public boolean isPresent() {
        return Optional.ofNullable(value).isPresent();
    }

    /**
     * Performs the given action if a value is present in the optional mono-valued attribute
     *
     * @param action the action
     */
    public void ifPresent(@Nonnull Consumer<? super T> action) {
        BValidate.notNull(action);

        Optional.ofNullable(value).ifPresent(action);
    }

    /**
     * Performs the given action if a value is present in the optional mono-valued attribute, otherwise performs the empty action
     *
     * @param action      the action
     * @param emptyAction the empty action
     */
    public void ifPresentOrElse(@Nonnull Consumer<? super T> action, @Nonnull Runnable emptyAction) {
        BValidate.notNull(action);
        BValidate.notNull(emptyAction);

        Optional.ofNullable(value).ifPresentOrElse(action, emptyAction);
    }

}
