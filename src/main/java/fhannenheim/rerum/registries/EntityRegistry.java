package fhannenheim.rerum.registries;

import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.entities.NetherArrowEntity;
import fhannenheim.rerum.util.DataUtil;
import fhannenheim.rerum.util.ModuleUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistry {
    public static Map<Pair<String, String>, EntityType<? extends Entity>> ENTITIES = new DataUtil.CustomHashMap<>();

    // left is module and right is entity name

    public static EntityType<? extends Entity> NETHER_ARROW_ENTITY = ENTITIES.put(new Pair<>("portablenether","nether_arrow_entity"),
            FabricEntityTypeBuilder.<NetherArrowEntity>create(SpawnGroup.MISC, NetherArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.2f,0.2f))
            .trackRangeBlocks(4).trackedUpdateRate(10)
            .build());

    public static void register() {
        ENTITIES.forEach((key, item) -> {
            if (ModuleUtil.isModuleEnabled(key.getLeft()))
                Registry.register(Registry.ENTITY_TYPE, new Identifier(Rerum.MOD_ID, key.getRight()), item);
        });
    }
}
