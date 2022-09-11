package fr.bobinho.beconomy.util.player;

import fr.bobinho.beconomy.api.validate.BValidate;
import fr.bobinho.beconomy.util.coin.Coin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class PlayerManager {

    public static int coinToMoney(@Nonnull List<ItemStack> coins) {
        BValidate.notNull(coins);

        //Calculates the value of coins
        return coins.stream()
                .mapToInt(coin -> coin.getAmount() * Coin.getValue(coin))
                .sum();
    }

    public static List<ItemStack> moneyToCoin(int amount) {
        AtomicInteger rest = new AtomicInteger(amount);
        List<ItemStack> coins = new ArrayList<>();
        for (Coin coin : Coin.values()) {
            if (rest.get() / coin.getValue() > 0) {
                coins.add(new ItemStack(coin.getMaterial(), rest.get() / coin.getValue()));
                rest.set(rest.get() - (rest.get() / coin.getValue()) * coin.getValue());
            }
        }
        return coins;
        //Gets the list of coins
        /**return Arrays.stream(Coin.values())
                .filter(coin -> rest.get() / coin.getValue() > 0)
                .map(coin -> {
                    int number = rest.get() / coin.getValue();
                    rest.set(rest.get() - number);
                    return new ItemStack(coin.getMaterial(), number);
                })
                .toList();*/
    }

    public static void give(Player player, List<ItemStack> items) {
        player.getInventory().addItem(items.toArray(new ItemStack[0])).values()
                .forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
    }

    public static List<ItemStack> getAllCoin(@Nonnull Player player) {
        BValidate.notNull(player);

        return IntStream.range(0, 46)
                .filter(slot -> player.getInventory().getItem(slot) != null)
                .filter(slot -> Coin.getValue(Objects.requireNonNull(player.getInventory().getItem(slot))) > 0)
                .mapToObj(slot -> Objects.requireNonNull(player.getInventory().getItem(slot)))
                .toList();
    }

    public static void removeCoin(@Nonnull Player player, @Nonnull Coin coin) {
        BValidate.notNull(player);
        BValidate.notNull(coin);

        IntStream.range(0, 46)
                .filter(slot -> player.getInventory().getItem(slot) != null)
                .filter(slot -> Objects.requireNonNull(player.getInventory().getItem(slot)).getType() == coin.getMaterial())
                .findFirst().ifPresent(slot -> {
                    ItemStack item =   Objects.requireNonNull(player.getInventory().getItem(slot));
                    item.setAmount(item.getAmount() - 1);
                });


    }

    public static void removeAllCoin(@Nonnull Player player) {
        BValidate.notNull(player);

        IntStream.range(0, 46)
                .filter(slot -> player.getInventory().getItem(slot) != null)
                .filter(slot -> Coin.getValue(Objects.requireNonNull(player.getInventory().getItem(slot))) > 0)
                .forEach(slot -> player.getInventory().setItem(slot, null));
    }

    public static boolean hasGold(@Nonnull Player player) {
        BValidate.notNull(player);

        return getAllCoin(player).stream()
                .anyMatch(coin -> coin.getType() == Coin.GOLD.getMaterial());
    }

    public static boolean hasIron(@Nonnull Player player) {
        BValidate.notNull(player);

        return getAllCoin(player).stream()
                .anyMatch(coin -> coin.getType() == Coin.IRON.getMaterial());
    }

}
