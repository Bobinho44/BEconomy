package fr.bobinho.beconomy.util.economy.vault;

import fr.bobinho.beconomy.BEconomyCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

/**
 * Created by AppleDash on 6/14/2016.
 * Blackjack is still best pony.
 */
public class VaultHook {
    private final Economy provider = new BEconomy();

    public void hook() {
        Bukkit.getServicesManager().register(Economy.class, provider, BEconomyCore.getInstance(), ServicePriority.Normal);
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, provider);
    }

}
