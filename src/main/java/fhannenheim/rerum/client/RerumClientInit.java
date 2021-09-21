package fhannenheim.rerum.client;

import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.client.models.BootModel;
import fhannenheim.rerum.entities.NetherArrowEntity;
import fhannenheim.rerum.mixin.ModelPredicateProviderRegistryMixin;
import fhannenheim.rerum.modules.BetterPickBlock;
import fhannenheim.rerum.modules.BoostBoots;
import fhannenheim.rerum.modules.LavaContainer;
import fhannenheim.rerum.modules.PortableNether;
import fhannenheim.rerum.registries.EntityRegistry;
import fhannenheim.rerum.registries.ItemRegistry;
import fhannenheim.rerum.registries.ModuleRegistry;
import fhannenheim.rerum.render.BoostBootsFeatureRenderer;
import fhannenheim.rerum.render.NetherArrowEntityRenderer;
import fhannenheim.rerum.util.ModuleUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockApplyCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class RerumClientInit {


    private static final Map<String, Runnable> initFunctions = Map.of(
            ModuleUtil.getKey(BetterPickBlock.class), RerumClientInit::initBetterPickBlock,
            ModuleUtil.getKey(PortableNether.class), RerumClientInit::initPortableNether,
            ModuleUtil.getKey(BoostBoots.class), RerumClientInit::initBoostBoots,
            ModuleUtil.getKey(LavaContainer.class), RerumClientInit::initLavaContainer
    );


    public static void init() {
        initFunctions.forEach((key,module) -> {
            if (ModuleUtil.isModuleEnabled(key)){
                module.run();
            }
        });
    }

    public static void initBetterPickBlock() {
        ClientPickBlockApplyCallback.EVENT.register((player, result, stack) -> {
            PlayerInventory inventory = player.getInventory();
            int slot = inventory.getSlotWithStack(stack);
            BetterPickBlock betterPickBlock = (BetterPickBlock) ModuleRegistry.BETTER_PICK_BLOCK;

            if (slot == -1) {
                String itemID = Registry.ITEM.getId(stack.getItem()).toString();
                if (betterPickBlock.itemAlternatives.containsKey(itemID)) {
                    String[] alternativeIDs = betterPickBlock.itemAlternatives.get(itemID);
                    for (String alternative : alternativeIDs) {
                        Identifier id = Identifier.tryParse(alternative);
                        ItemStack _stack = Registry.ITEM.get(id).getDefaultStack();
                        slot = inventory.getSlotWithStack(_stack);
                        if (slot != -1)
                            stack = inventory.getStack(slot);
                    }
                }
            }
            return inventory.getSlotWithStack(stack) == -1 ? ItemStack.EMPTY : inventory.getStack(inventory.getSlotWithStack(stack));
        });
    }

    public static void initPortableNether() {
        EntityRendererRegistry.INSTANCE.register((EntityType<? extends NetherArrowEntity>) EntityRegistry.NETHER_ARROW_ENTITY, NetherArrowEntityRenderer::new);
    }

    public static void initBoostBoots(){
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (client.player == null)
                return;
            ItemStack feet_stack = client.player.getEquippedStack(EquipmentSlot.FEET);
            if (client.options.keyJump.isPressed() && feet_stack.getItem() == ItemRegistry.BOOST_BOOTS) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeUuid(client.player.getUuid());
                ClientPlayNetworking.send(BoostBoots.BOOST_PACKET, buf);
            }
        });
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerEntityRenderer) {
                registrationHelper.register(new BoostBootsFeatureRenderer<PlayerEntity, BootModel>(
                        new FeatureRendererContext<>() {
                            @Override
                            public BootModel getModel() {
                                return new BootModel(context.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR));
                            }

                            @Override
                            public Identifier getTexture(PlayerEntity entity) {
                                return Identifier.tryParse(Rerum.MOD_ID + ":textures/armor/boots.png");
                            }
                        }));
            }
        });
    }

    public static void initLavaContainer() {
        ModelPredicateProviderRegistryMixin.register(ItemRegistry.LAVA_CONTAINER, new Identifier("fill_level"), (stack, world, entity, i) -> {
            NbtCompound tag = stack.getOrCreateTag();
            return tag.getInt("fill_level") / 64f;
        });
    }
}
