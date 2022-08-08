package fr.bobinho.beconomy.api.notification;

/**
 * Bobinho placeholder library
 */
public record BPlaceHolder(String oldValue, String newValue) {

    /**
     * Gets the value to be changed
     *
     * @return the value to be changed
     */
    public String getOldValue() {
        return this.oldValue;
    }

    /**
     * Gets the value to be applied
     *
     * @return the value to be applied
     */
    public String getReplacement() {
        return this.newValue;
    }

}
