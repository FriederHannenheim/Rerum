package fhannenheim.rerum.util;

import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.modules.ModModule;
import fhannenheim.rerum.registries.ModuleRegistry;

import java.util.concurrent.atomic.AtomicReference;

public class ModuleUtil {
    public static boolean isModuleEnabled(String module){
        return Rerum.CONFIG.modules.get(module) == null || Rerum.CONFIG.modules.get(module);
    }
    public static String getModuleKeyByInstance(ModModule module){
        AtomicReference<String> moduleKey = new AtomicReference<>("");

        ModuleRegistry.MODULES.forEach((key,item)->{
            if (item == module)
                moduleKey.set(key);
        });
        return moduleKey.get();
    }
    public static String getKey(Class<? extends ModModule> moduleClass){
        return ModuleRegistry.MODULE_KEYS.get(moduleClass);
    }
}
