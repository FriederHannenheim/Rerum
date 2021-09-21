package fhannenheim.rerum.registries;

import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.util.ModuleUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
    public static Map<Pair<String, String>, Block> BLOCKS = new HashMap<>();

    public static Block test = BLOCKS.put(new Pair<>("test", "test"), new Block(FabricBlockSettings.copyOf(Blocks.GOLD_BLOCK)));

    public static void register() {
        BLOCKS.forEach((key, item) -> {
            if (ModuleUtil.isModuleEnabled(key.getLeft()))
            Registry.register(Registry.BLOCK, new Identifier(Rerum.MOD_ID, key.getRight()), test);
        });
    }
}
