package fhannenheim.rerum.items;

import fhannenheim.rerum.entities.NetherArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NetherArrowItem extends ArrowItem {

    public NetherArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new NetherArrowEntity(world, shooter);
    }

}
