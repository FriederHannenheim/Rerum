package fhannenheim.rerum;

import fhannenheim.rerum.client.RerumClientInit;
import fhannenheim.rerum.config.RerumConfigHandler;
import fhannenheim.rerum.config.RerumConfigObject;
import fhannenheim.rerum.registries.EntityRegistry;
import fhannenheim.rerum.registries.ItemRegistry;
import fhannenheim.rerum.registries.ModuleRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Rerum implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "rerum";
	public static RerumConfigObject CONFIG;
	public static RerumConfigHandler CONFIG_HANDLER;
	public static Logger LOGGER;
	@Override
	public void onInitialize() {
		LOGGER = LogManager.getLogger(MOD_ID);
		CONFIG_HANDLER = new RerumConfigHandler();
		CONFIG = CONFIG_HANDLER.loadConfig();

		ItemRegistry.register();
		EntityRegistry.register();

		ModuleRegistry.initModules();


	}

	@Override
	public void onInitializeClient() {
		RerumClientInit.init();
	}
}
