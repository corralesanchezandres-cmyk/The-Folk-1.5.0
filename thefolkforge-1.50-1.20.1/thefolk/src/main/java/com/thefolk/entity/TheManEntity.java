package com.thefolk.entity;

import com.thefolk.FolkLang;
import com.thefolk.FolkUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

/**
 * Man: te mira 2 segundos y luego corre muy rápido hacia ti.
 * Mientras existe, el director hace que las demás criaturas aparezcan más seguido.
 */
public class TheManEntity extends Monster {

    // ================= CONFIGURACIÓN =================
    public static final int LOOK_TICKS = 40;             // 2 segundos mirando
    public static final int RUN_TICKS = 20 * 30;         // corre hasta 30 segundos
    private static final double SPEED = 0.95D;           // The Thing va a 0.75
    private static final double DAMAGE = 16.0D;          // 16 = 8 corazones
    // =================================================

    private int life;
    private int attackCooldown;
    private boolean directMove;

    public TheManEntity(EntityType<? extends TheManEntity> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.setInvulnerable(true);
        this.xpReward = 0;
        if (this.getNavigation() instanceof GroundPathNavigation ground) {
            ground.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, SPEED)
                .add(Attributes.ATTACK_DAMAGE, DAMAGE)
                .add(Attributes.FOLLOW_RANGE, 80.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        // Sin goals de vanilla: toda la IA está en customServerAiStep().
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.life++;
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        Player target = this.level().getNearestPlayer(this, 96.0D);
        if (target == null) {
            if (this.life > 200) {
                this.vanish(false);
            }
            return;
        }

        this.getLookControl().setLookAt(target, 90.0F, 90.0F);

        // Los primeros 2 segundos solo mira.
        if (this.life <= LOOK_TICKS) {
            this.getNavigation().stop();
            return;
        }

        if (this.life == LOOK_TICKS + 1 && target instanceof ServerPlayer sp) {
            FolkUtil.sendTitle(sp,
                    Component.literal(FolkLang.get(sp, "run")).withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
                    2, 30, 10);
        }

        if (this.life > LOOK_TICKS + RUN_TICKS) {
            this.vanish(true);
            return;
        }

        if (this.life % 5 == 0) {
            FolkUtil.openDoorsNearby(this);
        }

        if (this.life % 10 == 0 || (this.getNavigation().isDone() && this.life % 4 == 0)) {
            Path path = this.getNavigation().createPath(target, 0);
            this.directMove = path == null || !path.canReach();
            if (!this.directMove) {
                this.getNavigation().moveTo(path, 1.0D);
            }
        }
        if (this.directMove) {
            this.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);
        }

        if (this.attackCooldown <= 0
                && this.getBoundingBox().inflate(0.8D).intersects(target.getBoundingBox())) {
            this.swing(InteractionHand.MAIN_HAND);
            this.doHurtTarget(target);
            this.attackCooldown = 8;
        }
    }

    private void vanish(boolean effects) {
        if (effects && this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1.0D, this.getZ(),
                    30, 0.3D, 0.8D, 0.3D, 0.02D);
        }
        this.discard();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
