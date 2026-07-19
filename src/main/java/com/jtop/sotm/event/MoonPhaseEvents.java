package com.jtop.sotm.event;

import com.jtop.sotm.data.MoonSavedData;
import com.jtop.sotm.moon.MoonPhase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = "sonofthemoon")
public class MoonPhaseEvents {

    private static MoonPhase currentPhase = MoonPhase.NORMAL;
    private static int tickCounter = 0;

    public static void register() {
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        MoonPhase newPhase = MoonSavedData.get(level).getPhase();
        if (newPhase != currentPhase) {
            currentPhase = newPhase;
            applyPhaseEffects(level, currentPhase);
        }

        tickCounter++;
        if (tickCounter >= 20) {
            tickCounter = 0;
            if (currentPhase == MoonPhase.BLOOD_MOON) {
                for (Entity entity : level.getAllEntities()) {
                    if (entity instanceof Monster monster && monster.isOnFire()) {
                        monster.clearFire();
                        monster.setRemainingFireTicks(0);
                    }
                }
            }
        }
    }

    private static void applyPhaseEffects(ServerLevel level, MoonPhase phase) {
        switch (phase) {
            case NORMAL -> applyNormalEffects(level);
            case BLUE_MOON -> applyBlueMoonEffects(level);
            case CALM -> applyCalmEffects(level);
            case SAD -> applySadEffects(level);
            case BLOOD_MOON -> applyBloodMoonEffects(level);
            case MISSING -> applyMissingEffects(level);
        }
    }

    private static void applyNormalEffects(ServerLevel level) {
        level.setWeatherParameters(6000, 0, false, false);
        clearAllMoonEffects(level);
    }

    private static void applyBlueMoonEffects(ServerLevel level) {
        level.setWeatherParameters(6000, 0, false, false);
        for (Player player : level.players()) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 6000, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 6000, 1, false, false));
        }
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof Monster monster) {
                monster.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 6000, 1, false, false));
            }
        }
    }

    private static void applyCalmEffects(ServerLevel level) {
        level.setWeatherParameters(6000, 0, false, false);
        clearAllMoonEffects(level);
    }

    private static void applySadEffects(ServerLevel level) {
        level.setWeatherParameters(0, 6000, true, false);
        for (Player player : level.players()) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 0, false, false));
        }
    }

    private static void applyBloodMoonEffects(ServerLevel level) {
        level.setWeatherParameters(6000, 0, false, false);
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof Monster monster) {
                monster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 1, false, false));
                monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0, false, false));
                monster.clearFire();
                monster.setRemainingFireTicks(0);
            }
        }
    }

    private static void applyMissingEffects(ServerLevel level) {
        level.setWeatherParameters(6000, 0, false, false);
        clearAllMoonEffects(level);
    }

    private static void clearAllMoonEffects(ServerLevel level) {
        for (Player player : level.players()) {
            player.removeEffect(MobEffects.REGENERATION);
            player.removeEffect(MobEffects.LUCK);
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            player.removeEffect(MobEffects.WEAKNESS);
            player.removeEffect(MobEffects.HUNGER);
            player.removeEffect(MobEffects.BLINDNESS);
        }
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof Monster monster) {
                monster.removeEffect(MobEffects.DAMAGE_BOOST);
                monster.removeEffect(MobEffects.MOVEMENT_SPEED);
                monster.removeEffect(MobEffects.WEAKNESS);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        MoonPhase phase = MoonSavedData.get(level).getPhase();

        if (event.getEntity() instanceof Monster monster) {
            switch (phase) {
                case BLUE_MOON -> monster.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, -1, 1, false, false));
                case BLOOD_MOON -> {
                    monster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false));
                    monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 0, false, false));
                    monster.setPersistenceRequired();
                    monster.clearFire();
                    monster.setRemainingFireTicks(0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getSource().getEntity() instanceof Monster)) return;

        MoonPhase phase = MoonSavedData.get((ServerLevel) player.level()).getPhase();
        if (phase == MoonPhase.BLOOD_MOON) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
        }
    }
}