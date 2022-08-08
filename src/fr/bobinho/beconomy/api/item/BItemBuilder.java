package fr.bobinho.beconomy.api.item;

import fr.bobinho.beconomy.api.validate.BValidate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bobinho item builder library
 */
public final class BItemBuilder {

    /**
     * Fields
     */
    private ItemStack item;
    private final ItemMeta meta;

    /**
     * Creates the item builder
     *
     * @param item the item stack
     */
    public BItemBuilder(@Nonnull ItemStack item) {
        BValidate.notNull(item);

        //Configures the bukkit item stack
        this.item = item;
        this.meta = this.item.getItemMeta();
    }

    /**
     * Creates the item builder
     *
     * @param material the material
     */
    public BItemBuilder(@Nonnull Material material) {
        BValidate.notNull(material);

        //Configures the bukkit item stack
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    /**
     * Creates the item builder
     *
     * @param material the material
     * @param amount   the amount
     */
    public BItemBuilder(@Nonnull Material material, int amount) {
        BValidate.notNull(material);

        //Configure bukkit item stack.
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    /**
     * Gets the current item stack
     *
     * @return the item stack
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Sets the item stack
     *
     * @param item the item stack
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder item(@Nonnull ItemStack item) {
        BValidate.notNull(item);

        this.item = item;
        return this;
    }

    /**
     * Sets the item material
     *
     * @param material the material
     * @return the practice item Builder
     */
    @Nonnull
    public BItemBuilder material(@Nonnull Material material) {
        BValidate.notNull(material);

        item.setType(material);
        return this;
    }

    /**
     * Sets the item durability
     *
     * @param durability the durability
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder durability(int durability) {

        //Checks if the item is not damageable
        if (!(meta instanceof Damageable)) {
            return this;
        }

        //Sets item durability.
        ((Damageable) meta).setDamage(durability);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item amount
     *
     * @param amount the amount
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets the item name
     *
     * @param name the name
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder name(@Nonnull String name) {
        BValidate.notNull(name);

        meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(name));
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item lore
     *
     * @param lore the lore
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder setLore(@Nonnull List<String> lore) {
        BValidate.notNull(lore);

        //Declares the lore
        List<Component> lores = new ArrayList<>();
        for (@Nonnull String text : lore) {
            lores.add(LegacyComponentSerializer.legacyAmpersand().deserialize(text));
        }

        //Sets the item lore
        meta.lore(lores);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Adds new lines to the lore
     *
     * @param lore the lore
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder lore(@Nonnull List<String> lore) {
        BValidate.notNull(lore);

        //Declare the lore array.
        List<Component> lores = meta.lore();

        //If the lore is null or empty, create empty list.
        if (lores == null) {
            lores = new ArrayList<>();
        }

        //Adds new lines to the existed lore
        for (@Nonnull String text : lore) {
            lores.add(LegacyComponentSerializer.legacyAmpersand().deserialize(text));
        }

        //Sets the item lore
        meta.lore(lores);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Adds new lines to the lore
     *
     * @param lore the lore
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder lore(@Nonnull String... lore) {
        BValidate.notNull(lore);

        lore(new ArrayList<>(Arrays.asList(lore)));
        return this;
    }

    /**
     * Adds new lines to the lore
     *
     * @param color the color
     * @param lore  the lore
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder lore(@Nonnull String color, @Nonnull List<String> lore) {
        BValidate.notNull(color);
        BValidate.notNull(lore);

        //Declares the colorized lore
        List<String> colorizedLore = new ArrayList<>();
        lore.forEach(text -> colorizedLore.add(color + text));

        //Sets the item lore
        this.lore(colorizedLore);
        return this;
    }

    /**
     * Adds a new enchantment
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder addEnchantment(@Nonnull Enchantment enchantment, int level) {
        BValidate.notNull(enchantment);

        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Removes an enchantment
     *
     * @param enchantment the enchantment
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder removeEnchantment(@Nonnull Enchantment enchantment) {
        BValidate.notNull(enchantment);

        item.removeEnchantment(enchantment);
        return this;
    }

    /**
     * Sets the item unbreakable
     *
     * @param unbreakable the unbreakable statue
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Adds an item flag
     *
     * @param flag the flag
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder addFlag(@Nonnull ItemFlag... flag) {
        BValidate.notNull(flag);

        meta.addItemFlags(flag);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Removes an item flag
     *
     * @param flag the flag
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder removeFlag(@Nonnull ItemFlag... flag) {
        BValidate.notNull(flag);

        meta.removeItemFlags(flag);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item color
     *
     * @param red   the red
     * @param green the green
     * @param blue  the blue
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder color(int red, int green, int blue) {

        //Checks if item is colorable
        if (!(meta instanceof LeatherArmorMeta)) {
            return this;
        }

        //Colorizes the item
        ((LeatherArmorMeta) meta).setColor(Color.fromRGB(red, green, blue));
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item glow
     *
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder glow() {
        addFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        addEnchantment(Enchantment.DURABILITY, 1);
        return this;
    }

    /**
     * Removes the item glow
     *
     * @return the practice item builder
     */
    @Nonnull
    public BItemBuilder unglow() {
        removeFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        removeEnchantment(Enchantment.DURABILITY);
        return this;
    }

    /**
     * Builds the item
     *
     * @return the item stack
     */
    @Nonnull
    public ItemStack build() {
        return item;
    }

}
