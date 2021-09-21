package fhannenheim.rerum.registries;

import fhannenheim.rerum.modules.*;
import fhannenheim.rerum.util.DataUtil;
import fhannenheim.rerum.util.ModuleUtil;

import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {
    public static Map<String, ModModule> MODULES = new DataUtil.CustomHashMap<>();
    public static Map<Class<? extends ModModule>, String> MODULE_KEYS = new HashMap<>();

    public static ModModule BETTER_PICK_BLOCK = MODULES.put("better_pick_block", new BetterPickBlock());
    public static ModModule LAVA_CONTAINER = MODULES.put("lava_container", new LavaContainer());
    public static ModModule PORTABLE_NETHER = MODULES.put("portable_nether", new PortableNether());
    public static ModModule BOOST_BOOTS = MODULES.put("boost_boots", new BoostBoots());

    public static void initModules() {
        MODULES.forEach((key, module) -> {
            if(ModuleUtil.isModuleEnabled(key))
                module.onInitialize(key);
            MODULE_KEYS.put(module.getClass(),key);
        });
    }
}
