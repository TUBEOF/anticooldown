package com.yourgamespace.anticooldown.module.customitemdamage.utils;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.basics.AntiCooldownLogger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ItemDamageManager {

    private static final AntiCooldownLogger antiCooldownLogger = AntiCooldown.getAntiCooldownLogger();
    private static final HashMap<Material, Double> itemDamages = new HashMap<>();

    public static void addCache(String paramMaterial, String paramDamage) {
        Material material;
        try {
            material = Material.valueOf(paramMaterial);
        } catch (IllegalArgumentException exception) {
            antiCooldownLogger.info("§4WARNING: §cMaterial §e" + paramMaterial + " §cfor CustomItemDamage cannot be found or is not supported by §e" + Bukkit.getBukkitVersion() + "§c!");
            return;
        }
        double damage;
        try {
            damage = Double.parseDouble(paramDamage);
        } catch (NumberFormatException exception) {
            antiCooldownLogger.info("§4WARNING: §cDamage for material §e" + paramMaterial + " §c cannot be applied! It must be a double!");
            return;
        }

        itemDamages.put(material, damage);
    }

    public static boolean hasItemCustomDamage(ItemStack itemStack) {
        return itemDamages.containsKey(itemStack.getType());
    }

    public static double getItemDamage(Material material) {
        return itemDamages.get(material);
    }

}
