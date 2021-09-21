package fhannenheim.rerum.modules;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.components.BoolComponent;
import fhannenheim.rerum.components.IntComponent;
import fhannenheim.rerum.registries.ItemRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class BoostBoots extends ModModule implements EntityComponentInitializer {
    public static final Identifier BOOST_PACKET = new Identifier(Rerum.MOD_ID, "boost");
    public static final ComponentKey<IntComponent> TICKS_IN_AIR = ComponentRegistry.getOrCreate(new Identifier(Rerum.MOD_ID, "airborne_last_tick"), IntComponent.class);
    public static final ComponentKey<BoolComponent> BOOST_USED = ComponentRegistry.getOrCreate(new Identifier(Rerum.MOD_ID, "boost_used"), BoolComponent.class);


    @Override
    public void onInitialize(String moduleName) {
        super.onInitialize(moduleName);


        ServerPlayNetworking.registerGlobalReceiver(BOOST_PACKET, (server, player, handler, buf, responseSender) -> {
            IntComponent ticks_in_air = TICKS_IN_AIR.get(player);
            BoolComponent boost_used = BOOST_USED.get(player);
            if (ticks_in_air.get() > 4 && !boost_used.get() && player.getEquippedStack(EquipmentSlot.FEET).getItem() == ItemRegistry.BOOST_BOOTS) {
                float f1 = player.headYaw * ((float) Math.PI / 180F);
                player.setVelocity((-MathHelper.sin(f1) * 0.2F), 0.6f, (MathHelper.cos(f1) * 0.2F));
                player.velocityModified = true;
                player.getAbilities().allowFlying = true;
                player.stopFallFlying();
                boost_used.set(true);
                if (!player.isSneaking())
                    player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1, 1);

                player.getEquippedStack(EquipmentSlot.FEET).damage(1, player, (playerEntity) -> playerEntity.playSound(SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1, 1));

                player.getServerWorld().spawnParticles(ParticleTypes.FIREWORK, player.getX(), player.getY(), player.getZ(), 20, 0.5F, 0.5F, 0.5F, 0.01f);
            } else if (ticks_in_air.get() > 4 && player.isFallFlying() && player.getEquippedStack(EquipmentSlot.FEET).getItem() == ItemRegistry.BOOST_BOOTS) {
                World world = player.world;

                // Checks if the player already has a FireworkRocketEntity
                List<FireworkRocketEntity> rockets = world.getEntitiesByType(TypeFilter.instanceOf(FireworkRocketEntity.class), new Box(player.getBlockPos()).expand(5), fireworkRocketEntity -> fireworkRocketEntity.getOwner() == player);
                if(!rockets.isEmpty())
                    return;

                FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, new ItemStack(Items.FIREWORK_ROCKET), player);
                world.spawnEntity(fireworkRocketEntity);

                player.getEquippedStack(EquipmentSlot.FEET).damage(1, player, (playerEntity) -> playerEntity.playSound(SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1, 1));
            }
        });
        ServerTickEvents.END_SERVER_TICK.register((server) -> server.getPlayerManager().getPlayerList().forEach((player) -> {
            IntComponent ticks_in_air = TICKS_IN_AIR.get(player);
            ticks_in_air.set(ticks_in_air.get() + 1);
            if (player.isOnGround()) {
                player.getAbilities().allowFlying = false;
                BOOST_USED.get(player).set(false);
                ticks_in_air.set(0);
            }
        }));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TICKS_IN_AIR, player -> new IntComponent(0), RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(BOOST_USED, player -> new BoolComponent(true), RespawnCopyStrategy.NEVER_COPY);
    }
}
