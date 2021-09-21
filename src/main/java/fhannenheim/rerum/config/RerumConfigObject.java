package fhannenheim.rerum.config;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonObject;
import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.registries.ModuleRegistry;

import java.util.HashMap;
import java.util.Map;

public class RerumConfigObject {

    // Casting to a hashmap because Map.ofEntries() returns a immutable map
    @Comment("Disable all modules you don't want by setting their value to false.")
    public Map<String, Boolean> modules = new HashMap<>();
    @Comment("Config for the individual modules")
    public Map<String, Map<String, String>> moduleConfig = new HashMap<>();


    public RerumConfigObject(JsonObject jsonObject) {
        setUp();
        try {
            JsonObject _modules = jsonObject.getObject("modules");
            modules.forEach((key, object) -> modules.put(key, _modules.getBoolean(key, object)));

            JsonObject _moduleConfig = jsonObject.getObject("moduleConfig");
            if (_moduleConfig == null)
                return;
            moduleConfig.forEach((key, object) -> object.forEach((innerKey, innerObject) -> {
                // This is horrible. Should've used GSON, maybe that would be better
                String configObject = _moduleConfig.getObject(key).get(String.class, innerKey);
                if (configObject != null)
                    moduleConfig.get(key).put(innerKey, configObject);
            }));
        } catch (Exception e) {
            Rerum.LOGGER.error("Error in Rerum config");
            Rerum.LOGGER.info(jsonObject.toString());
        }
    }

    public RerumConfigObject() {
        setUp();
    }

    private void setUp() {
        ModuleRegistry.MODULES.forEach((name, module) -> {
            modules.put(name,true);

            if (module.configTemplate != null) {
                Rerum.LOGGER.info(module.configTemplate);
                moduleConfig.put(name, module.configTemplate);
            }
        });
    }
}
