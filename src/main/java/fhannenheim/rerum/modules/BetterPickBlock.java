package fhannenheim.rerum.modules;

import fhannenheim.rerum.Rerum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetterPickBlock extends ModModule {
    public List<String> dontReplaceItemIDs = new ArrayList<>();
    public List<String> dontReplaceItemTags = new ArrayList<>();
    public HashMap<String, String[]> itemAlternatives = new HashMap<>();

    public BetterPickBlock(){
        configTemplate = new HashMap<>(Map.ofEntries(
                Map.entry("dont replace", "#minecraft:boats #rerum:tools #rerum:buckets minecraft:flint_and_steel minecraft:shears"),
                Map.entry("block alternatives", "minecraft:stone->minecraft:cobblestone minecraft:farmland->minecraft:dirt|minecraft:grass_block minecraft:grass_block->minecraft:dirt")
        ));
    }

    @Override
    public void onInitialize(String moduleName) {
        super.onInitialize(moduleName);
        Map<String, String> moduleConfig = Rerum.CONFIG.moduleConfig.get(moduleName);

        String[] dont_replace = moduleConfig.get("dont replace").split(" +");
        String[] alternatives = moduleConfig.get("block alternatives").split(" +");

        for (String s : dont_replace) {
            if (s.startsWith("#"))
                dontReplaceItemTags.add(s.replace("#", ""));
            else
                dontReplaceItemIDs.add(s);
        }
        for (String alternative : alternatives) {
            try {
                // Splits the string into the block that is clicked and its alternatives
                String[] split = alternative.split("->");
                // Splits the alternatives into block ids
                String[] block_alts = split[1].split("\\|");
                itemAlternatives.put(split[0], block_alts);
            } catch (IndexOutOfBoundsException e){
                Rerum.LOGGER.fatal("Error parsing string: " + alternative);
                Rerum.LOGGER.info(alternatives);
                throw e;
            }

        }
    }
}
