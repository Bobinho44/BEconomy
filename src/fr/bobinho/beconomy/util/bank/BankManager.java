package fr.bobinho.beconomy.util.bank;

import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.api.location.BLocation;
import fr.bobinho.beconomy.api.scheduler.BScheduler;
import fr.bobinho.beconomy.api.validate.BValidate;
import fr.bobinho.beconomy.util.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BankManager {

    private static final Collection<Bank> banks = new ArrayList<>();

    public static void initialize() {
        loadBanks();

        BScheduler.asyncScheduler().every(1, TimeUnit.HOURS).run(() -> {
            saveBanks();

            Calendar date = Calendar.getInstance();
            if (date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && date.get(Calendar.HOUR_OF_DAY) - 1 < 0) {
                Arrays.stream(Bukkit.getOfflinePlayers())
                        .forEach(player -> EconomyManager.depositPlayer(player, BEconomyCore.getMainSetting().getConfiguration().getDouble("wage", 100)));
            }
        });
    }

    public static void uninitialize() {
        saveBanks();
    }

    public static Collection<Bank> getBanks() {
        return banks;
    }

    public static Optional<Bank> getBank(@Nonnull String name) {
        BValidate.notNull(name);

        return banks.stream().filter(bank -> bank.getName().equals(name)).findFirst();
    }

    public static Optional<Bank> getBank(@Nonnull Location location) {
        BValidate.notNull(location);

        return banks.stream().filter(bank -> bank.getLocation().equals(location)).findFirst();
    }

    public static boolean isBank(@Nonnull String name) {
        BValidate.notNull(name);

        return getBank(name).isPresent();
    }

    public static boolean isBank(@Nonnull Location location) {
        BValidate.notNull(location);

        return getBank(location).isPresent();
    }

    public static void createBank(@Nonnull String name, @Nonnull Location location) {
        BValidate.notNull(name);
        BValidate.notNull(location);
        BValidate.isFalse(isBank(name));
        BValidate.isFalse(isBank(location));

        banks.add(new Bank(name, location));
    }

    public static void deleteBank(@Nonnull String name) {
        BValidate.notNull(name);
        BValidate.isTrue(isBank(name));

        getBank(name).ifPresent(banks::remove);
    }

    public static boolean isThereNoBankNearby(@Nonnull Player player) {
        BValidate.notNull(player);

        return banks.stream()
                .noneMatch(bank -> bank.getLocation().getWorld().equals(player.getWorld()) && bank.getLocation().distance(player.getLocation()) <= 10);
    }

    public static void loadBanks() {
        BEconomyCore.getBanksSetting().getConfiguration().getKeys(false)
                .forEach(bank -> createBank(bank, BLocation.getAsLocation(BEconomyCore.getBanksSetting().getConfiguration().getString(bank, "0, 0, 0, (" + Bukkit.getWorlds().get(0).getName() + ")"))));
    }

    public static void saveBanks() {
        BEconomyCore.getBanksSetting().clear();
        banks.forEach(bank -> BEconomyCore.getBanksSetting().getConfiguration().set(bank.getName(), BLocation.getAsString(bank.getLocation())));
        BEconomyCore.getBanksSetting().save();
    }
}
