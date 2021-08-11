package com.yourgamespace.anticooldown.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.yourgamespace.anticooldown.main.AntiCooldown;
import com.yourgamespace.anticooldown.utils.ObjectTransformer;
import com.yourgamespace.anticooldown.utils.WorldManager;
import de.tubeof.tubetils.api.cache.CacheContainer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CombatSounds {

    private static final CacheContainer cacheContainer = AntiCooldown.getCacheContainer();

    public static class PacketHandler {

        public PacketHandler() {
            onSweepAttackSound();
        }

        private void onSweepAttackSound() {
            if(!ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "DISABLE_NEW_COMBAT_SOUNDS"))) return;

            AntiCooldown.getProtocolManager().addPacketListener(new PacketAdapter(AntiCooldown.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    // Check if valid sound
                    boolean valid = false;
                    Sound sound = event.getPacket().getSoundEffects().read(0);
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_SWEEP)) valid = true;
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_CRIT)) valid = true;
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK)) valid = true;
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_STRONG)) valid = true;
                    if(sound.equals(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE)) valid = true;

                    // If not valid: Return;
                    if(!valid) return;

                    Player player = event.getPlayer();
                    String world = player.getWorld().getName();

                    // Check Bypass and Permissions
                    boolean isBypassed = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_BYPASS_PERMISSION")) && player.hasPermission("anticooldown.bypass");
                    boolean isPermitted = ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS")) && player.hasPermission("anticooldown.combatsounds") || !ObjectTransformer.getBoolean(cacheContainer.get(Boolean.class, "USE_PERMSSIONS"));

                    // If not permitted: Return;
                    if(!isPermitted) return;

                    // Check if world is disabled
                    if (WorldManager.isWorldDisabled(world) && isBypassed) {
                        // If disabled and is bypassed: disable sounds;
                        event.setCancelled(true);
                    } else {
                        // If permitted and not bypassed: disable sounds;
                        event.setCancelled(true);
                    }
                }
            });
        }
    }
}