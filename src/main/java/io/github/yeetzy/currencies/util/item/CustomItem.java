package io.github.yeetzy.currencies.util.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.github.yeetzy.currencies.util.Helper;
import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
public class CustomItem {
    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    @Getter
    private Consumer<InventoryClickEvent> inventoryClickEventConsumer;

    private Boolean editable = true, cancelled = false;


    public CustomItem(Material material) {
        this.itemStack = new ItemStack(material);
    }

    private CustomItem(ItemStack itemStack, Consumer<InventoryClickEvent> inventoryClickEventConsumer, Boolean editable, Boolean cancelled) {
        this.itemStack = itemStack;
        this.inventoryClickEventConsumer = inventoryClickEventConsumer;
        this.editable = editable;
        this.cancelled = cancelled;
    }

    public CustomItem type(Material material) {
        this.itemStack.setType(material);

        return this;
    }

    public CustomItem name(String name) {
        ItemMeta itemMeta = this.meta();
        itemMeta.setDisplayName(name);

        this.setItemMeta(itemMeta);

        return this;
    }

    public CustomItem lore(String... lore) {
        return this.lore(false, lore);
    }

    public CustomItem lore(List<String> lore) {
        return this.lore(false, lore);
    }

    public CustomItem lore(Boolean keepLore, String... lore) {
        List<String> finalLore = Arrays.asList(lore);

        finalLore.replaceAll(Helper::colorize);

        return this.lore(keepLore, finalLore);
    }

    public CustomItem lore(Boolean keepLore, List<String> lore) {
        ItemMeta itemMeta = this.meta();

        lore.replaceAll(Helper::colorize);

        List<String> finalLore = keepLore ? itemMeta.getLore() : lore;

        if (keepLore) finalLore.addAll(lore);

        itemMeta.setLore(finalLore);

        this.setItemMeta(itemMeta);

        return this;
    }

    public CustomItem addFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public CustomItem hideAttributes() {
        ItemMeta itemMeta = this.meta();

        for (ItemFlag itemFlag : ItemFlag.values())
            itemMeta.addItemFlags(itemFlag);

        this.setItemMeta(itemMeta);

        this.setItemMeta(itemMeta);

        return this;
    }

    public CustomItem data(Integer data) {
        return this.data(data.shortValue());
    }

    public CustomItem data(Short data) {
        this.itemStack.setDurability(data);

        return this;
    }

    public CustomItem amount(Integer amount) {
        this.itemStack.setAmount(amount);

        return this;
    }

    public CustomItem enchant(Enchantment enchantment, Integer level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);

        return this;
    }

    public CustomItem owner(String owner) {
        if (owner.length() <= 16) {
            SkullMeta skullMeta = (SkullMeta) this.meta();

            skullMeta.setOwner(owner);

            this.setItemMeta(skullMeta);
        } else {
            GameProfile profile = createGameProfile(owner, UUID.randomUUID());

            ItemMeta itemMeta = this.meta();

            Class<?> headMetaClass = itemMeta.getClass();

            if (!RefSet(headMetaClass, itemMeta, "profile", profile)) return this;
            else this.setItemMeta(itemMeta);

            SkullMeta skullMeta = (SkullMeta) this.meta();

            this.setItemMeta(skullMeta);
        }

        return this;
    }

    private GameProfile createGameProfile(String texture, UUID uniqueId) {
        GameProfile profile = new GameProfile(uniqueId, null);
        PropertyMap propertyMap = profile.getProperties();

        propertyMap.put("textures", new Property("textures", texture));

        return profile;
    }

    private boolean RefSet(Class<?> sourceClass, Object instance, String fieldName, Object value) {
        try {
            Field field = sourceClass.getDeclaredField(fieldName);
            Field modifiersField = Field.class.getDeclaredField("modifiers");

            int modifiers = modifiersField.getModifiers();

            if (!field.isAccessible()) field.setAccessible(true);

            if ((modifiers & 16) == 16) {
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, modifiers & -17);
            }

            try {
                field.set(instance, value);
            } finally {
                if ((modifiers & 16) == 16) {
                    modifiersField.setInt(field, modifiers | 16);
                }

                if (!field.isAccessible()) field.setAccessible(false);
            }

            return true;
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            return false;
        }
    }

    public CustomItem editable(Boolean editable) {
        this.editable = editable;

        return this;
    }

    public CustomItem cancelled(Boolean cancelled) {
        this.cancelled = cancelled;

        return this;
    }

    public CustomItem banner(String letter, DyeColor baseColor, DyeColor letterColor){
        ItemStack banner = this.itemStack;

        if (!banner.getType().equals(Material.BANNER)) return this;

        letter = ChatColor.stripColor(letter.toUpperCase()).substring(0, 1);

        BannerMeta bannerMeta = (BannerMeta) this.meta();
        bannerMeta.setBaseColor(baseColor);

        switch (letter) {
            case "A":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "B":
            case "8":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "C":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "D":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "E":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "F":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "G":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "H":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "I":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "J":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "K":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "L":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "M":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.TRIANGLE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "N":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "O":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "P":
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Q":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_RIGHT));
                break;
            case "R":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "S":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                break;
            case "T":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "U":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "V":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "W":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.TRIANGLE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "X":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Y":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Z":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "1":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_TOP_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "2":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "3":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "4":
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "5":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "6":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "7":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "9":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "0":
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
        }
        banner.setItemMeta(bannerMeta);
        return this;
    }

    public CustomItem onClick(Consumer<InventoryClickEvent> inventoryClickEventConsumer) {
        this.inventoryClickEventConsumer = inventoryClickEventConsumer;

        return this;
    }

    public CustomItem clone() {
        return new CustomItem(
                this.itemStack,
                this.inventoryClickEventConsumer,
                this.editable,
                this.cancelled
        );
    }

    public ItemStack build() {
        return this.itemStack;
    }

    public Boolean isEditable() {
        return this.editable;
    }

    public Boolean isCancelled() {
        return this.cancelled;
    }

    private ItemMeta meta() {
        return this.itemStack.getItemMeta();
    }

    private boolean setItemMeta(ItemMeta itemMeta) {
        return this.itemStack.setItemMeta(itemMeta);
    }
}