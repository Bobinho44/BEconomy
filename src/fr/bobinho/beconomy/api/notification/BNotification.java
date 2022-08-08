package fr.bobinho.beconomy.api.notification;

/**
 * Bobinho notification library
 */
public interface BNotification {

    /**
     * Gets the text of the notification
     *
     * @return the text of the notification
     */
    String getNotification();

    /**
     * Gets the text of the notification after modification via placeholders
     *
     * @param placeholders all placeholders
     * @return the text of the notification after modification via placeholders
     */
    String getNotification(BPlaceHolder... placeholders);

}
