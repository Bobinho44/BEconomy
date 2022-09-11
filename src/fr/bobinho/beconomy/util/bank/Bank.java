package fr.bobinho.beconomy.util.bank;

import fr.bobinho.beconomy.api.validate.BValidate;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public class Bank {

    private final String name;
    private final Location location;

    public Bank(@Nonnull String name, @Nonnull Location location) {
        BValidate.notNull(name);
        BValidate.notNull(location);

        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

}
