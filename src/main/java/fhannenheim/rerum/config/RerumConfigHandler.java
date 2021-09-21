package fhannenheim.rerum.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;

import blue.endless.jankson.api.SyntaxError;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RerumConfigHandler {

    public RerumConfigObject values;

    public RerumConfigObject loadConfig() {
        Jankson jankson = Jankson.builder().build();

        File configFile = new File(FabricLoader.getInstance().getConfigDir() + "/rerumConfig.json");
        try {
            JsonObject configJson = jankson.load(configFile);
            values = new RerumConfigObject(configJson);
        } catch (IOException | SyntaxError ignored) {
            values = new RerumConfigObject();
        }
        save();
        return values;
    }

    public void save(){
        File configFile = new File(FabricLoader.getInstance().getConfigDir() + "/rerumConfig.json");
        Jankson jankson = Jankson.builder().build();
        String result = jankson
                .toJson(values) //The first call makes a JsonObject
                .toJson(JsonGrammar.builder().withComments(true).printWhitespace(true).build(), 0);     //The second turns the JsonObject into a String -
        //in this case, preserving comments and pretty-printing with newlines
        try {
            assert configFile.exists() || configFile.createNewFile();
            FileOutputStream out = new FileOutputStream(configFile, false);

            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
