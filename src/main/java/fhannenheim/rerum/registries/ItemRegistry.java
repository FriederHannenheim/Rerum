package fhannenheim.rerum.registries;

import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.items.BoostBootsItem;
import fhannenheim.rerum.items.LavaContainerItem;
import fhannenheim.rerum.items.NetherArrowItem;
import fhannenheim.rerum.items.NetherTeleporterItem;
import fhannenheim.rerum.util.DataUtil;
import fhannenheim.rerum.util.ModuleUtil;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class ItemRegistry {
    public static Map<Identifier, Item> ITEMS = new DataUtil.CustomHashMap<>();

    public static Item NETHER_TELEPORTER = ITEMS.put(new Identifier("portable_nether", "nether_teleporter"), new NetherTeleporterItem((new FabricItemSettings().group(ItemGroup.MISC))));
    public static Item NETHER_ARROW = ITEMS.put(new Identifier("portable_nether", "nether_arrow"), new NetherArrowItem((new FabricItemSettings().group(ItemGroup.MISC))));
    public static final Item LAVA_CONTAINER = ITEMS.put(new Identifier("lava_container","lava_container"),new LavaContainerItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof()));
    public static Item BOOST_BOOTS = ITEMS.put(new Identifier("boost_boots", "boost_boots"), new BoostBootsItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).maxDamage(128).equipmentSlot(stack -> EquipmentSlot.FEET)));

    public static void register() {
        ITEMS.forEach((key, item) -> {
            if (ModuleUtil.isModuleEnabled(key.getNamespace()))
            Registry.register(Registry.ITEM, new Identifier(Rerum.MOD_ID, key.getPath()), item);
        });
    }
}
