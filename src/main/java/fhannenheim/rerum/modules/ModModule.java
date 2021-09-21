package fhannenheim.rerum.modules;

import fhannenheim.rerum.Rerum;

import java.util.Map;

public class ModModule {
    public Map<String, String> configTemplate = null;

    public Map<String, String> config = Map.of();

    public void onInitialize(String moduleName){
        config = Rerum.CONFIG.moduleConfig.get(moduleName);
    }
}
