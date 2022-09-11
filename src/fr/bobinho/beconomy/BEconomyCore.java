package fr.bobinho.beconomy;

import co.aikar.commands.PaperCommandManager;
import fr.bobinho.beconomy.api.color.BColor;
import fr.bobinho.beconomy.api.command.BCommand;
import fr.bobinho.beconomy.api.logger.BLogger;
import fr.bobinho.beconomy.api.scheduler.BScheduler;
import fr.bobinho.beconomy.api.setting.BSetting;
import fr.bobinho.beconomy.util.economy.EconomyManager;
import fr.bobinho.beconomy.util.bank.BankManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Core of the plugin
 */
public class BEconomyCore extends JavaPlugin {

    /**
     * Fields
     */
    private static final BLogger bLogger = new BLogger(BEconomyCore.class.getSimpleName());
    private static BSetting messagesSetting;
    private static BSetting mainSetting;
    private static BSetting banksSetting;
    private static BSetting moneySetting;

    /**
     * Gets the plugin
     *
     * @return the plugin
     */
    @Nonnull
    public static BEconomyCore getInstance() {
        return JavaPlugin.getPlugin(BEconomyCore.class);
    }

    /**
     * Gets the logger
     *
     * @return the logger
     */
    @Nonnull
    public static BLogger getBLogger() {
        return bLogger;
    }

    /**
     * Gets the messages setting
     *
     * @return the messages setting
     */
    @Nonnull
    public static BSetting getMessagesSetting() {
        return messagesSetting;
    }

    /**
     * Gets the main setting
     *
     * @return the main setting
     */
    @Nonnull
    public static BSetting getMainSetting() {
        return mainSetting;
    }

    /**
     * Gets the banks setting
     *
     * @return the banks setting
     */
    @Nonnull
    public static BSetting getBanksSetting() {
        return banksSetting;
    }

    /**
     * Gets the money setting
     *
     * @return the money setting
     */
    @Nonnull
    public static BSetting getMoneySetting() {
        return moneySetting;
    }

    /**
     * Enables and initializes the plugin
     */
    @Override
    public void onEnable() {
        bLogger.info("Loading the plugin...");

        messagesSetting = new BSetting("messages");
        mainSetting = new BSetting("settings");
        banksSetting = new BSetting("banks");
        moneySetting = new BSetting("money");

        //Registers commands
        registerCommands();

        //Initialize class manager
        EconomyManager.initialize();
        BankManager.initialize();
    }

    /**
     * Disables the plugin and save data
     */
    @Override
    public void onDisable() {
        bLogger.info("Unloading the plugin...");

        //Uninitialize class manager
        EconomyManager.uninitialize();
        BankManager.uninitialize();
    }

    /**
     * Registers commands
     */
    private void registerCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        Reflections reflections = new Reflections("fr.bobinho.beconomy.commands");
        Set<Class<? extends BCommand>> classes = reflections.getSubTypesOf(BCommand.class);
        for (@Nonnull Class<? extends BCommand> command : classes) {
            try {
                commandManager.registerCommand(command.getDeclaredConstructor().newInstance());
            } catch (Exception exception) {
                getBLogger().error("Couldn't register command(" + command.getName() + ")!", exception);
            }
        }
        bLogger.info("Successfully loaded commands.");
    }

}
