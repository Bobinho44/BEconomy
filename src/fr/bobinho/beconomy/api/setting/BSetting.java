package fr.bobinho.beconomy.api.setting;

import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.api.validate.BValidate;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Bobinho setting library
 */
public final class BSetting {

    /**
     * Fields
     */
    private final String fileName;
    private YamlConfiguration configuration;

    /**
     * Creates a new setting
     *
     * @param fileName the setting file name
     */
    public BSetting(@Nonnull String fileName) {
        BValidate.notNull(fileName);

        this.fileName = fileName;
        initialize();
    }

    /**
     * Gets the file name
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Initializes settings file
     */
    public void initialize() {
        File file = new File(BEconomyCore.getInstance().getDataFolder(), getFileName() + ".yml");

        if (!file.exists()) {
            try {
                Files.createDirectories(file.getParentFile().toPath());
                InputStream input = BSetting.class.getResourceAsStream("/" + getFileName() + ".yml");
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    Files.createFile(file.toPath());
                }
            } catch (Exception e) {
                BEconomyCore.getBLogger().error("Could not load the " + getFileName() + ".yml file!", e);
                return;
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);

        BEconomyCore.getBLogger().info("Successfully loaded " + getFileName() + " data.");
    }

    /**
     * Gets configuration
     *
     * @return the configuration
     */
    @Nonnull
    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Clears configuration
     */
    public void clear() {
        for (String key : getConfiguration().getKeys(false)) {
            getConfiguration().set(key, null);
        }
    }

    /**
     * Saves configuration
     */
    public void save() {
        try {
            getConfiguration().save(BEconomyCore.getInstance().getDataFolder() + "/" + getFileName() + ".yml");
        } catch (IOException e) {
            BEconomyCore.getBLogger().error("Could not save the " + getFileName() + ".yml file!", e);
        }
    }

}
