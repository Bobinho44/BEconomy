package fr.bobinho.beconomy;

import co.aikar.commands.PaperCommandManager;
import fr.bobinho.beconomy.api.command.BCommand;
import fr.bobinho.beconomy.api.logger.BLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Core of the plugin
 */
public final class BEconomyCore extends JavaPlugin {

    /**
     * Fields
     */
    private static final BLogger bLogger = new BLogger(BEconomyCore.class.getSimpleName());

    /**
     * Gets the plugin
     *
     * @return the plugin
     */
    public static BEconomyCore getInstance() {
        return JavaPlugin.getPlugin(BEconomyCore.class);
    }

    /**
     * Gets the logger
     *
     * @return the logger
     */
    public static BLogger getBLogger() {
        return bLogger;
    }

    /**
     * Enables and initializes the plugin
     */
    @Override
    public void onEnable() {
        bLogger.info("Loading the plugin...");

        //Registers commands and listeners
        registerCommands();
        registerListeners();
    }

    /**
     * Disables the plugin and save data
     */
    @Override
    public void onDisable() {
        bLogger.info("Unloading the plugin...");
    }

    /**
     * Registers listeners
     */
    private void registerListeners() {
        Reflections reflections = new Reflections("fr.bobinho.beconomy.listeners");
        Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);
        for (@Nonnull Class<? extends Listener> listener : classes) {
            try {
                Bukkit.getServer().getPluginManager().registerEvents(listener.getDeclaredConstructor().newInstance(), this);
            } catch (Exception exception) {
                getBLogger().error("Couldn't register command(" + listener.getName() + ")!", exception);
            }
        }
        bLogger.info("Successfully loaded listeners.");
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
