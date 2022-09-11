package fr.bobinho.beconomy.util.coin;

import fr.bobinho.beconomy.api.validate.BValidate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public enum Coin {
    GOLD(Material.GOLD_NUGGET, 10000),
    IRON(Material.IRON_NUGGET, 100),
    COPPER(Material.RAW_COPPER, 1);

    private final Material material;
    private final int value;

    Coin(@Nonnull Material material, int value) {
        BValidate.notNull(material);

        this.material = material;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Material getMaterial() {
        return material;
    }

    public static int getValue(@Nonnull ItemStack testedCoin) {
        BValidate.notNull(testedCoin);

        return Arrays.stream(values())
                .filter(coin -> testedCoin.getType() == coin.material)
                .map(Coin::getValue)
                .findFirst()
                .orElse(0);
    }

}
