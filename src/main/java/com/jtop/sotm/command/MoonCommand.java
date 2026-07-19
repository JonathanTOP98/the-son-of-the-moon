package com.jtop.sotm.command;

import com.jtop.sotm.data.MoonSavedData;
import com.jtop.sotm.moon.MoonPhase;
import com.jtop.sotm.network.MoonPhasePayload;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "sonofthemoon")
public class MoonCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("moon")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("fase")
                                .then(Commands.argument("id", IntegerArgumentType.integer(0, 5))
                                        .executes(context -> {
                                            int phaseId = IntegerArgumentType.getInteger(context, "id");
                                            return setMoonPhase(context.getSource(), MoonPhase.byId(phaseId));
                                        })
                                )
                        )
                        .then(Commands.literal("fase")
                                .then(Commands.argument("nombre", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            for (MoonPhase p : MoonPhase.values()) {
                                                builder.suggest(p.getName());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "nombre");
                                            return setMoonPhase(context.getSource(), MoonPhase.byName(name));
                                        })
                                )
                        )
                        .then(Commands.literal("info")
                                .executes(context -> {
                                    ServerLevel level = context.getSource().getLevel();
                                    MoonPhase phase = MoonSavedData.get(level).getPhase();
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("command.moon.info",
                                                    Component.translatable(phase.getTranslationKey()),
                                                    Component.translatable(phase.getLoreKey())),
                                            false
                                    );
                                    return 1;
                                })
                        )
                        .then(Commands.literal("reset")
                                .executes(context -> setMoonPhase(context.getSource(), MoonPhase.NORMAL))
                        )
        );
    }

    private static int setMoonPhase(CommandSourceStack source, MoonPhase phase) {
        ServerLevel level = source.getLevel();
        MoonSavedData data = MoonSavedData.get(level);
        data.setPhase(phase);
        PacketDistributor.sendToAllPlayers(new MoonPhasePayload(phase));

        source.sendSuccess(
                () -> Component.translatable("command.moon.changed",
                        Component.translatable(phase.getTranslationKey())),
                true
        );
        return 1;
    }
}