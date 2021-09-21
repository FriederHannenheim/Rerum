package fhannenheim.rerum.modules.portable_nether;

import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.util.DataUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NetherTeleporter {
    /**
     * Teleports an originEntity from the overworld to the nether and vice versa
     *
     * @param originEntity The entity to teleport
     */
    public static void switchWorlds(LivingEntity originEntity) {
        if (!originEntity.world.isClient) {

            // If the originEntity is in a vehicle it will get out and if it is being ridden the passenger will be thrown out
            originEntity.detach();
            originEntity.setVelocity(Vec3d.ZERO);
            MinecraftServer server = originEntity.world.getServer();

            // I don't think this is ever going to happen this is just here in case it does happen and to suppress the IDE warnings
            if (server == null)
                return;

            ServerWorld originWorld = (ServerWorld) originEntity.getEntityWorld();
            // Get the world of the destination dimension
            ServerWorld destinationWorld = server.getWorld(originWorld.getRegistryKey() == World.OVERWORLD ? World.NETHER : World.OVERWORLD);

            if (destinationWorld != null && server.isNetherAllowed()) {

                // One block in the nether is 8 blocks in the nether.
                double movementFactor = originWorld.getRegistryKey() == World.OVERWORLD ? 0.125d : 8;
                BlockPos pos = createDestinationSpawn(new BlockPos(originEntity.getPos().getX() * movementFactor, originEntity.getPos().getY(), originEntity.getPos().getZ() * movementFactor), destinationWorld);

                teleport(originEntity, originWorld, destinationWorld, Vec3d.ofCenter(pos, 0));

                // play sound in origin world
                originWorld.playSound(null, originEntity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1, 1);
                // play sound in destination world
                destinationWorld.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1, 1);
            }
        }
    }

    private static void teleport(LivingEntity originEntity, ServerWorld origin, ServerWorld destination, Vec3d position) {
        if (originEntity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) originEntity).teleport(destination, position.x, position.y, position.z, originEntity.getYaw(), originEntity.getPitch());
        } else {
            Entity destinationEntity = originEntity.getType().create(destination);
            if (destinationEntity == null)
                return;
            destinationEntity.copyFrom(originEntity);
            destinationEntity.refreshPositionAndAngles(position.x, position.y, position.z, originEntity.getYaw(), originEntity.getPitch());
            destinationEntity.setVelocity(originEntity.getVelocity());
            destination.onDimensionChanged(destinationEntity);

            originEntity.remove(Entity.RemovalReason.CHANGED_DIMENSION);

            origin.resetIdleTimeout();
            destination.resetIdleTimeout();
        }
    }


    private static BlockPos createDestinationSpawn(BlockPos posIn, World serverWorld) {
        double bestDistance = Double.MAX_VALUE;
        int posX = MathHelper.floor(posIn.getX());//posX
        int posY = MathHelper.floor(posIn.getY());//posY
        int posZ = MathHelper.floor(posIn.getZ());//posZ
        int bestX = posX;
        int bestY = posY;
        int bestZ = posZ;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        // Search for free space in a 16x128x16 box
        for (int xIndex = posX - 16; xIndex <= posX + 16; ++xIndex) {
            double xDistance = (double) xIndex + 0.5D - posX;

            for (int zIndex = posZ - 16; zIndex <= posZ + 16; ++zIndex) {
                double zDistance = (double) zIndex + 0.5D - posZ;

                for (int yIndex = 128 - 1; yIndex >= 0; --yIndex) {
                    if (serverWorld.isAir(mutable.set(xIndex, yIndex, zIndex))) {
                        // Go down till a non-air block is found
                        while (yIndex > 0 && serverWorld.isAir(mutable.set(xIndex, yIndex - 1, zIndex))) {
                            --yIndex;
                        }
                        // If the block the player would spawn on is not a full block continue the search.
                        if (!serverWorld.getBlockState(mutable.set(xIndex, yIndex - 1, zIndex)).isOpaque())
                            continue;
                        // If the space is just one block high continue the search.
                        if (!serverWorld.isAir(mutable.set(xIndex, yIndex + 1, zIndex)))
                            continue;

                        double yDistance = (double) yIndex + 0.5D - posY;
                        double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2) + Math.pow(zDistance, 2));
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            bestX = xIndex;
                            bestY = yIndex;
                            bestZ = zIndex;
                        }
                    }
                }
            }
        }
        // If no suitable spawning space was found, construct a platform to spawn on
        if (bestDistance == Double.MAX_VALUE) {
            for (int xIndex = -1; xIndex <= 1; xIndex++) {
                for (int zIndex = -1; zIndex <= 1; zIndex++) {
                    BlockState bs = serverWorld.getBlockState(mutable.set(posX + xIndex, posY - 1, posZ + zIndex));
                    if (!bs.isOpaque() && bs.getBlock() != Blocks.BEDROCK) {
                        serverWorld.setBlockState(mutable, Blocks.OBSIDIAN.getDefaultState());
                    }
                }
            }
            for (int yIndex = 0; yIndex <= 1; yIndex++) {
                for (int xIndex = -1; xIndex <= 1; xIndex++) {
                    for (int zIndex = -1; zIndex <= 1; zIndex++) {
                        BlockState bs = serverWorld.getBlockState(mutable.set(posX + xIndex, posY + yIndex, posZ + zIndex));
                        if (bs.getBlock() != Blocks.BEDROCK) {
                            serverWorld.setBlockState(mutable, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        return mutable.set(bestX, bestY, bestZ);
    }
}
