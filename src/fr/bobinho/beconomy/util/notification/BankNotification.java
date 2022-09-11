package fr.bobinho.beconomy.util.notification;

import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.api.notification.BNotification;
import fr.bobinho.beconomy.api.notification.BPlaceHolder;
import org.bukkit.ChatColor;

/**
 * Enum of bank notifications
 */
public enum BankNotification implements BNotification {
    BALANCE,
    NOT_ENOUGH_MONEY,
    NOT_ENOUGH_COIN,
    MONEY_RECEIVED,
    MONEY_SENT,
    WITHDRAWAL_EFFECTED,
    DEPOSIT_EFFECTED,
    BALANCE_TOP,
    NO_BANK_NEARBY,
    COMBINE,
    SPLIT,
    NO_GOLD,
    NO_IRON,
    MONEY_FORMAT;

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
