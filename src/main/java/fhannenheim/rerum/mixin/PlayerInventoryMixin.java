package fhannenheim.rerum.mixin;

import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.modules.BetterPickBlock;
import fhannenheim.rerum.registries.ModuleRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final public DefaultedList<ItemStack> main;

    @Shadow public int selectedSlot;

    @Inject(method = "getSwappableHotbarSlot",at = @At(value = "RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Integer> ci){
        int hotbarSlot = 0;

        BetterPickBlock betterPickBlock = (BetterPickBlock) ModuleRegistry.BETTER_PICK_BLOCK;
        Rerum.LOGGER.info(betterPickBlock.dontReplaceItemTags);


        int i;
        int hotbarIndex;
        outerLoop:
        for(i = 0; i < 9; ++i) {
            hotbarIndex = (this.selectedSlot + i) % 9;

            // Iterate trough the don't replace tags to see if the item can be replaced
            for (int tagIndex = 0; tagIndex < betterPickBlock.dontReplaceItemTags.size(); tagIndex++){
                Identifier tag = Identifier.tryParse(betterPickBlock.dontReplaceItemTags.get(tagIndex));
                if(TagRegistry.item(tag).contains(this.main.get(hotbarIndex).getItem())){
                    continue outerLoop;
                }
            }
            if (!this.main.get(hotbarIndex).hasEnchantments() &&
                    !betterPickBlock.dontReplaceItemIDs.contains(Registry.ITEM.getId(this.main.get(hotbarIndex).getItem()).toString())
            ) {
                ci.setReturnValue(hotbarIndex);
                return;
            }
        }
        Rerum.LOGGER.info("sag AAAAAAAAAA");
        ci.setReturnValue(hotbarSlot);
    }
}
