package fr.bobinho.beconomy.util.notification;

import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.api.notification.BNotification;
import fr.bobinho.beconomy.api.notification.BPlaceHolder;
import org.bukkit.ChatColor;

/**
 * Enum of admin notifications
 */
public enum AdminNotification implements BNotification {
    BANK_INFORMATIONS,
    BANK_INFORMATION,
    BANK_NAME_ALREADY_USED,
    BANK_LOCATION_ALREADY_USED,
    BANK_NAME_NOT_USED,
    BANK_CREATED,
    BANK_DELETED;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNotification() {
        return ChatColor.translateAlternateColorCodes('&', BEconomyCore.getMessagesSetting().getConfiguration().getString(this.name(), ""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNotification(BPlaceHolder... placeholders) {
        String notification = ChatColor.translateAlternateColorCodes('&', BEconomyCore.getMessagesSetting().getConfiguration().getString(this.name(), ""));

        for (BPlaceHolder placeHolder : placeholders) {
            notification = notification.replaceAll(placeHolder.getOldValue(), placeHolder.getReplacement());
        }

        return notification;
    }

}
