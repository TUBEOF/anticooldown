package com.yourgamespace.anticooldown.module.customitemdamage.listeners;

import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.module.customitemdamage.utils.ItemDamageHandler;
import com.yourgamespace.anticooldown.module.customitemdamage.utils.ItemDamageManager;
import com.yourgamespace.anticooldown.utils.WorldManager;
import com.yourgamespace.anticooldown.utils.basics.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.module.ModuleListener;
import de.tubeof.tubetils.api.cache.CacheContainer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("ConstantConditions")
public class ApplyDamage extends ModuleListener {

    private final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();
    private final WorldManager worldManager = AntiCooldown.getWorldManager();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // PERMA REMOVED: Removed since module system. (5.0.0)
        // Unnecessary check: Module can be removed or disabled.
        //if (!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ENABLE_CUSTOM_ITEM_DAMAGE"))) return;
        // END PERMA REMOVED
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null) return;
        if (!ItemDamageManager.hasItemCustomDamage(itemStack)) return;
        Entity entity = event.getEntity();
        String world = player.getWorld().getName();

        // Check Bypass and Permissions
        boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
        boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS")) && player.hasPermission("anticooldown.customitemdamage") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMISSIONS"));

        // If not permitted: Return;
        if (!isPermitted) return;

        ItemDamageHandler itemDamageHandler = null;

        // Check if world is disabled
        if (worldManager.isWorldDisabled(world)) {
            // If disabled and not bypassed: Return;
            if (!isBypassed) return;

            // If disabled and is bypassed: Apply damage;
            if (isBypassed) {
                itemDamageHandler = new ItemDamageHandler(player, entity);
                event.setDamage(itemDamageHandler.getFinalDamage());
            }
        } else {
            // If permitted and not bypassed: Apply damage;
            itemDamageHandler = new ItemDamageHandler(player, entity);
            event.setDamage(itemDamageHandler.getFinalDamage());
        }

        if (ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "ENABLE_CUSTOM_ITEM_DAMAGE_ACTIONBAR"))) {
            String message = ObjectTransformer.getString(cacheContainer.get(String.class, "CUSTOM_ITEM_DAMAGE_ACTIONBAR_MESSAGE"));
            message = message.replace("%actionbar_prefix%", ObjectTransformer.getString(cacheContainer.get(String.class, "ACTIONBAR_PREFIX")));
            message = message.replace("%finaldamage%", Double.toString(itemDamageHandler.getFinalDamage()));

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }
}
