package fr.bobinho.beconomy.util.economy;

import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.api.event.BEvent;
import fr.bobinho.beconomy.api.notification.BPlaceHolder;
import fr.bobinho.beconomy.api.scheduler.BScheduler;
import fr.bobinho.beconomy.api.validate.BValidate;
import fr.bobinho.beconomy.util.economy.vault.VaultHook;
import fr.bobinho.beconomy.util.notification.BankNotification;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EconomyManager {

    private static final VaultHook vaultHook = new VaultHook();
    private static Map<UUID, Double> bank = new HashMap<>();

    public static void initialize() {
        loadBank();
        vaultHook.hook();

        BEvent.registerEvent(PlayerJoinEvent.class, EventPriority.MONITOR)
                .filter(event -> !hasAccount(event.getPlayer()))
                .consume(event -> createAccount(event.getPlayer()));

        BScheduler.asyncScheduler().every(1, TimeUnit.HOURS).run(EconomyManager::saveBank);
    }

    public static void uninitialize() {
        saveBank();
        vaultHook.unhook();
    }

    public static int fractionalDigits() {
        return BEconomyCore.getMainSetting().getConfiguration().getInt("decimal", 0);
    }

    public static String format(double amount) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(fractionalDigits());

        return BankNotification.MONEY_FORMAT.getNotification(
                new BPlaceHolder("%currency%", amount > 1 ? currencyNamePlural() : currencyNameSingular()),
                new BPlaceHolder("%amount%", "" + df.format(amount)));
    }

    public static String currencyNamePlural() {
        return BEconomyCore.getMainSetting().getConfiguration().getString("currencyNamePlural", "dollars");
    }

    public static String currencyNameSingular() {
        return BEconomyCore.getMainSetting().getConfiguration().getString("currencyNameSingular", "dollar");
    }

    public static boolean hasAccount(@Nonnull OfflinePlayer offlinePlayer) {
        BValidate.notNull(offlinePlayer);

        return bank.entrySet().stream()
                .anyMatch(account -> account.getKey().equals(offlinePlayer.getUniqueId()));
    }

    public static void createAccount(@Nonnull OfflinePlayer offlinePlayer) {
        BValidate.notNull(offlinePlayer);

        bank.put(offlinePlayer.getUniqueId(), BEconomyCore.getMainSetting().getConfiguration().getDouble("startingSum", 500));
    }

    public static double getBalance(@Nonnull OfflinePlayer offlinePlayer) {
        BValidate.notNull(offlinePlayer);

        return bank.entrySet().stream()
                .filter(account -> account.getKey().equals(offlinePlayer.getUniqueId()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(0.0D);
    }

    public static boolean has(@Nonnull OfflinePlayer offlinePlayer, double amount) {
        BValidate.notNull(offlinePlayer);

        return bank.entrySet().stream()
                .anyMatch(account -> account.getKey().equals(offlinePlayer.getUniqueId()) && account.getValue() >= amount);
    }

    public static EconomyResponse withdrawPlayer(@Nonnull OfflinePlayer offlinePlayer, double amount) {
        BValidate.notNull(offlinePlayer);

        return bank.entrySet().stream()
                .filter(account -> account.getKey().equals(offlinePlayer.getUniqueId()) && account.getValue() >= amount)
                .findFirst()
                .map(account -> {
                    bank.put(account.getKey(), account.getValue() - amount);
                    return new EconomyResponse(amount, account.getValue() - amount, EconomyResponse.ResponseType.SUCCESS, null);
                })
                .orElse(new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null));
    }

    public static EconomyResponse depositPlayer(@Nonnull OfflinePlayer offlinePlayer, double amount) {
        BValidate.notNull(offlinePlayer);

        return bank.entrySet().stream()
                .filter(account -> account.getKey().equals(offlinePlayer.getUniqueId()))
                .findFirst()
                .map(account -> {
                    bank.put(account.getKey(), account.getValue() + amount);
                    return new EconomyResponse(amount, account.getValue() + amount, EconomyResponse.ResponseType.SUCCESS, null);
                })
                .orElse(new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null));
    }

    public static void loadBank() {
        bank = BEconomyCore.getMoneySetting().getConfiguration().getKeys(false).stream()
                .map(UUID::fromString)
                .collect(Collectors.toMap(uuid -> uuid, uuid -> BEconomyCore.getMoneySetting().getConfiguration().getDouble(uuid.toString())));
    }

    public static void saveBank() {
        BEconomyCore.getMoneySetting().clear();
        bank.forEach((key, value) -> BEconomyCore.getMoneySetting().getConfiguration().set(key.toString(), value));
        BEconomyCore.getMoneySetting().save();
    }

}
