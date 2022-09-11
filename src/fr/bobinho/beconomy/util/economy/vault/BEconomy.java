package fr.bobinho.beconomy.util.economy.vault;

import com.google.common.collect.ImmutableList;
import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.util.economy.EconomyManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class BEconomy implements Economy {

    @Override
    public boolean isEnabled() {
        return BEconomyCore.getInstance().isEnabled();
    }

    @Override
    public String getName() {
        return "BEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return EconomyManager.fractionalDigits();
    }

    @Override
    public String format(double amount) {
        return EconomyManager.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return EconomyManager.currencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return EconomyManager.currencyNameSingular();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return EconomyManager.hasAccount(offlinePlayer);
    }

    @Override
    public boolean hasAccount(String target) {
        return this.hasAccount(Bukkit.getOfflinePlayer(target));
    }

    @Override
    public boolean hasAccount(String target, String worldName) {
        return this.hasAccount(target);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return this.hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return EconomyManager.getBalance(offlinePlayer);
    }

    @Override
    public double getBalance(String target) {
        return this.getBalance(Bukkit.getOfflinePlayer(target));
    }

    @Override
    public double getBalance(String target, String worldName) {
        return this.getBalance(target);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return this.getBalance(offlinePlayer);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return EconomyManager.has(offlinePlayer, amount);
    }

    @Override
    public boolean has(String target, double amount) {
        return this.has(Bukkit.getOfflinePlayer(target), amount);
    }

    @Override
    public boolean has(String target, String worldName, double amount) {
        return this.has(target, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return this.has(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
       return EconomyManager.withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String target, double amount) {
        return this.withdrawPlayer(Bukkit.getOfflinePlayer(target), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double v) {
        return this.withdrawPlayer(playerName, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return this.withdrawPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return EconomyManager.depositPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse depositPlayer(String target, double amount) {
        return this.depositPlayer(Bukkit.getOfflinePlayer(target), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double v) {
        return this.depositPlayer(playerName, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double v) {
        return this.depositPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return ImmutableList.of();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return true;
    }

}
