package fr.bobinho.beconomy.commands;

import co.aikar.commands.annotation.*;
import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.api.command.BCommand;
import fr.bobinho.beconomy.api.notification.BPlaceHolder;
import fr.bobinho.beconomy.api.validate.BValidate;
import fr.bobinho.beconomy.util.coin.Coin;
import fr.bobinho.beconomy.util.notification.BankNotification;
import fr.bobinho.beconomy.util.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Command of coin
 */
@CommandAlias("coin")
public final class CoinCommand extends BCommand {

    /**
     * Command bank help
     */
    @Syntax("/coin help")
    @Default
    @Subcommand("coin")
    @CommandPermission("coin.help")
    @Description("Gets the coin command help.")
    public void onCommandCoinHelp(Player sender) {
        sendCommandHelp(this.getClass(), sender, "Coin");
    }

    /**
     * Command coin combine
     */
    @Syntax("/coin combine")
    @Subcommand("combine")
    @CommandPermission("coin.combine")
    @Description("Gets the example command help.")
    public void onCommandCoinCombine(Player sender) {
        List<ItemStack> coins = PlayerManager.moneyToCoin(PlayerManager.coinToMoney(PlayerManager.getAllCoin(sender)));

        PlayerManager.removeAllCoin(sender);
        PlayerManager.give(sender, coins);

        //Sends message
        sender.sendMessage(BankNotification.COMBINE.getNotification());
    }

    /**
     * Command coin split
     */
    @Syntax("/coin split")
    @Subcommand("split")
    @CommandPermission("coin.split")
    @CommandCompletion("gold|silver")
    @Description("Gets the example command help.")
    public void onCommandCoinCombine(Player sender, @Single String material) {
        BValidate.notNull(material);

        if (material.equalsIgnoreCase("gold") && !PlayerManager.hasGold(sender)) {
            sender.sendMessage(BankNotification.NO_GOLD.getNotification());
            return;
        }

        if (material.equalsIgnoreCase("silver") && !PlayerManager.hasIron(sender)) {
            sender.sendMessage(BankNotification.NO_IRON.getNotification());
            return;
        }

        switch (material.toLowerCase()) {
            case "gold" -> {
                PlayerManager.give(sender, List.of(new ItemStack(Coin.IRON.getMaterial(), 100)));
                PlayerManager.removeCoin(sender, Coin.GOLD);
            }
            case "silver" -> {
                PlayerManager.give(sender, List.of(new ItemStack(Coin.COPPER.getMaterial(), 100)));
                PlayerManager.removeCoin(sender, Coin.IRON);
            }
            default -> {
            }
        }

        //Sends message
        sender.sendMessage(BankNotification.SPLIT.getNotification(
                new BPlaceHolder("%coin%", material)
        ));
    }

}
