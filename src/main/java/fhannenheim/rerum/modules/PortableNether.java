package fhannenheim.rerum.modules;

import fhannenheim.rerum.entities.NetherArrowEntity;
import fhannenheim.rerum.modules.portable_nether.NetherTeleporter;
import fhannenheim.rerum.registries.ItemRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PortableNether extends ModModule {
    public static final String MOD_ID = "portablenether";
    public static final Identifier SPAWN_PACKET_ID = new Identifier(MOD_ID, "spawn_packet");
    public static final List<LivingEntity> to_teleport = new ArrayList<>();

    @Override
    public void onInitialize(String moduleName) {
        // Teleports entities at the end of the tick
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            while (!to_teleport.isEmpty()){
                NetherTeleporter.switchWorlds(to_teleport.get(0));
                to_teleport.remove(0);
            }
        });

        DispenserBlock.registerBehavior(ItemRegistry.NETHER_ARROW, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return new NetherArrowEntity(world, position.getX(), position.getY(), position.getZ());
            }
        });
    }
}
