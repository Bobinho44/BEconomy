package fr.bobinho.beconomy.commands;

import co.aikar.commands.annotation.*;
import fr.bobinho.beconomy.api.command.BCommand;
import fr.bobinho.beconomy.api.location.BLocation;
import fr.bobinho.beconomy.api.notification.BPlaceHolder;
import fr.bobinho.beconomy.api.validate.BValidate;
import fr.bobinho.beconomy.util.economy.EconomyManager;
import fr.bobinho.beconomy.util.bank.BankManager;
import fr.bobinho.beconomy.util.notification.AdminNotification;
import fr.bobinho.beconomy.util.notification.BankNotification;
import fr.bobinho.beconomy.util.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Command of bank
 */
@CommandAlias("bank")
public final class BankCommand extends BCommand {

    /**
     * Command bank help
     */
    @Syntax("/bank help")
    @Subcommand("help")
    @CommandPermission("bank.help")
    @Description("Gets the bank command help.")
    public void onCommandBankHelp(Player sender) {
        sendCommandHelp(this.getClass(), sender, "Bank");
    }

    /**
     * Command bank add
     */
    @Syntax("/bank add")
    @Subcommand("add")
    @CommandPermission("bank.add")
    @Description("Gets the example command help.")
    @OpCommand
    public void onCommandBankAdd(Player sender, @Nonnull String name) {
        BValidate.notNull(name);

        if (BankManager.isBank(name)) {
            sender.sendMessage(AdminNotification.BANK_NAME_ALREADY_USED.getNotification(
                    new BPlaceHolder("%name%", name)));
            return;
        }

        if (BankManager.isBank(sender.getLocation().getBlock().getLocation())) {
            sender.sendMessage(AdminNotification.BANK_LOCATION_ALREADY_USED.getNotification(
                    new BPlaceHolder("%location%", BLocation.getAsString(sender.getLocation().getBlock().getLocation()))));
            return;
        }

        //Creates bank
        BankManager.createBank(name, sender.getLocation().getBlock().getLocation());

        //Sends message
        sender.sendMessage(AdminNotification.BANK_CREATED.getNotification(
                new BPlaceHolder("%name%", name),
                new BPlaceHolder("%location%", BLocation.getAsString(sender.getLocation().getBlock().getLocation()))));
    }

    /**
     * Command bank list
     */
    @Syntax("/bank list")
    @Subcommand("list")
    @CommandPermission("bank.list")
    @Description("Gets the example command help.")
    @OpCommand
    public void onCommandBankList(Player sender) {

        //Sends the header
        sender.sendMessage(AdminNotification.BANK_INFORMATIONS.getNotification());

        //Sends the list
        BankManager.getBanks().forEach(bank -> sender.sendMessage(AdminNotification.BANK_INFORMATION.getNotification(
                new BPlaceHolder("%name%", bank.getName()),
                new BPlaceHolder("%location%", BLocation.getAsString(bank.getLocation())))));
    }

    /**
     * Command bank remove
     */
    @Syntax("/bank remove")
    @Subcommand("remove")
    @CommandPermission("bank.remove")
    @Description("Gets the example command help.")
    @OpCommand
    public void onCommandBankRemove(Player sender, @Nonnull String name) {
        BValidate.notNull(name);

        if (!BankManager.isBank(name)) {
            sender.sendMessage(AdminNotification.BANK_NAME_NOT_USED.getNotification(
                    new BPlaceHolder("%name%", name)));
            return;
        }

        //Deletes
        BankManager.deleteBank(name);

        //Sends message
        sender.sendMessage(AdminNotification.BANK_DELETED.getNotification(
                new BPlaceHolder("%name%", name)));
    }

    /**
     * Command bank
     */
    @Syntax("/bank")
    @Default
    @CommandAlias("balance")
    @CommandPermission("bank.balance")
    @Description("Gets the example command help.")
    public void onCommandBank(Player sender) {
        if (BankManager.isThereNoBankNearby(sender)) {
            sender.sendMessage(BankNotification.NO_BANK_NEARBY.getNotification());
            return;
        }

        //Sends message
        sender.sendMessage(BankNotification.BALANCE.getNotification(
                new BPlaceHolder("%money%", "" + EconomyManager.format(EconomyManager.getBalance(sender)))));
    }

    /**
     * Command bank pay
     */
    @Syntax("/bank pay <player> <amount>")
    @Subcommand("pay")
    @CommandPermission("bank.pay")
    @CommandCompletion("@players")
    @Description("Gets the example command help.")
    public void onCommandBankPay(Player sender, @Single OfflinePlayer target, int amount) {
        BValidate.notNull(target);

        if (BankManager.isThereNoBankNearby(sender)) {
            sender.sendMessage(BankNotification.NO_BANK_NEARBY.getNotification());
            return;
        }

        if (EconomyManager.getBalance(sender) < amount) {
            sender.sendMessage(BankNotification.NOT_ENOUGH_MONEY.getNotification(
                    new BPlaceHolder("%money%", "" + EconomyManager.format(amount))));
            return;
        }

        //Transfers the amount
        EconomyManager.withdrawPlayer(sender, amount);
        EconomyManager.depositPlayer(target, amount);

        //Sends message
        sender.sendMessage(BankNotification.MONEY_SENT.getNotification(
                new BPlaceHolder("%name%", target.getName()),
                new BPlaceHolder("%money%", "" + EconomyManager.format(amount))));

        if (target.isOnline()) {
            Objects.requireNonNull(Bukkit.getPlayer(target.getUniqueId())).sendMessage(BankNotification.MONEY_RECEIVED.getNotification(
                    new BPlaceHolder("%name%", sender.getName()),
                    new BPlaceHolder("%money%", "" + EconomyManager.format(amount))));
        }
    }

    /**
     * Command bank withdraw
     */
    @Syntax("/bank withdraw <amount>")
    @Subcommand("withdraw|withdrawal")
    @CommandPermission("bank.withdraw")
    @Description("Gets the example command help.")
    public void onCommandBankWithdraw(Player sender, int amount) {
        if (BankManager.isThereNoBankNearby(sender)) {
            sender.sendMessage(BankNotification.NO_BANK_NEARBY.getNotification());
            return;
        }

        if (EconomyManager.getBalance(sender) < amount) {
            sender.sendMessage(BankNotification.NOT_ENOUGH_MONEY.getNotification(
                    new BPlaceHolder("%money%", "" + EconomyManager.format(amount))));
            return;
        }

        //Transfers the money
        EconomyManager.withdrawPlayer(sender, amount);
        PlayerManager.give(sender, PlayerManager.moneyToCoin(amount));

        //Sends message
        sender.sendMessage(BankNotification.WITHDRAWAL_EFFECTED.getNotification(
                new BPlaceHolder("%balance%", "" + EconomyManager.format(EconomyManager.getBalance(sender))),
                new BPlaceHolder("%money%", "" + EconomyManager.format(amount))));

    }

    /**
     * Command bank deposit
     */
    @Syntax("/bank deposit <amount>")
    @Subcommand("deposit")
    @CommandPermission("bank.deposit")
    @Description("Gets the example command help.")
    public void onCommandBankDeposit(Player sender, int amount) {
        if (BankManager.isThereNoBankNearby(sender)) {
            sender.sendMessage(BankNotification.NO_BANK_NEARBY.getNotification());
            return;
        }

        if (PlayerManager.coinToMoney(PlayerManager.getAllCoin(sender)) < amount) {
            sender.sendMessage(BankNotification.NOT_ENOUGH_COIN.getNotification(
                    new BPlaceHolder("%money%", "" + EconomyManager.format(amount))));
            return;
        }

        int money = PlayerManager.coinToMoney(PlayerManager.getAllCoin(sender));
        //Transfers the money
        PlayerManager.removeAllCoin(sender);
        PlayerManager.give(sender, PlayerManager.moneyToCoin(money - amount));
        EconomyManager.depositPlayer(sender, amount);

        //Sends message
        sender.sendMessage(BankNotification.DEPOSIT_EFFECTED.getNotification(
                new BPlaceHolder("%balance%", "" + EconomyManager.format(EconomyManager.getBalance(sender))),
                new BPlaceHolder("%money%", "" + EconomyManager.format(amount))));

    }

    /**
     * Command bank top
     */
    @Syntax("/bank top")
    @Subcommand("top")
    @CommandPermission("bank.top")
    @Description("Gets the example command help.")
    public void onCommandBankTop(Player sender) {
        if (BankManager.isThereNoBankNearby(sender)) {
            sender.sendMessage(BankNotification.NO_BANK_NEARBY.getNotification());
            return;
        }

        //Gets top players accounts
        List<Map.Entry<String, Double>> playersAccount = Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(player -> player.getName() != null && EconomyManager.hasAccount(player))
                .map(player -> Map.entry(player.getName(), EconomyManager.getBalance(sender)))
                .sorted(Map.Entry.comparingByValue())
                .toList();

        //Sends the top
        for (int i = 0; i < 10; i++) {

            //Sends message
            sender.sendMessage(BankNotification.BALANCE_TOP.getNotification(
                    new BPlaceHolder("%rank%", "" + (i + 1)),
                    new BPlaceHolder("%balance%", "" + EconomyManager.format((playersAccount.size() > i ? playersAccount.get(i).getValue() : 0))),
                    new BPlaceHolder("%name%", "" + (playersAccount.size() > i ? playersAccount.get(i).getKey() : " N/D"))));
        }
    }

}
