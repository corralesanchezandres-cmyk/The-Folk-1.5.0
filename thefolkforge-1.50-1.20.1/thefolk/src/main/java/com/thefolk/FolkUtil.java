package com.thefolk;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/** Funciones compartidas por las criaturas. */
public class FolkUtil {

    /** Muestra un texto grande en pantalla al jugador. */
    public static void sendTitle(ServerPlayer player, Component text, int fadeIn, int stay, int fadeOut) {
        player.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
        player.connection.send(new ClientboundSetTitleTextPacket(text));
    }

    /** Abre puertas, trampillas y portones que la criatura tenga cerca. */
    public static void openDoorsNearby(Mob mob) {
        Level lvl = mob.level();
        AABB box = mob.getBoundingBox().inflate(1.0D, 0.0D, 1.0D);
        for (BlockPos p : BlockPos.betweenClosed(
                Mth.floor(box.minX), Mth.floor(box.minY + 0.1D), Mth.floor(box.minZ),
                Mth.floor(box.maxX), Mth.floor(box.minY + 1.1D), Mth.floor(box.maxZ))) {
            BlockPos pos = p.immutable();
            BlockState state = lvl.getBlockState(pos);
            if (state.getBlock() instanceof DoorBlock door) {
                if (!door.isOpen(state)) {
                    door.setOpen(mob, lvl, state, pos, true);
                }
            } else if (state.getBlock() instanceof FenceGateBlock) {
                if (!state.getValue(FenceGateBlock.OPEN)) {
                    lvl.setBlock(pos, state.setValue(FenceGateBlock.OPEN, true), 10);
                }
            } else if (state.getBlock() instanceof TrapDoorBlock) {
                if (!state.getValue(TrapDoorBlock.OPEN)) {
                    lvl.setBlock(pos, state.setValue(TrapDoorBlock.OPEN, true), 10);
                }
            }
        }
    }
}
