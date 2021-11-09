package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {

    @Redirect(method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    private static ItemStack extract_fix_entity(ItemStack instance) {
        if (CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem) (Object) instance).getShadowId() != null) {
            return instance;
        }
        return instance.copy();
    }

    @Redirect(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    private static ItemStack extract_fix_storage1(ItemStack instance) {
        if (CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem) (Object) instance).getShadowId() != null) {
            return instance;
        }
        return instance.copy();
    }

    @Redirect(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private static void extract_fix_storage2(Inventory instance, int i, ItemStack itemStack) {
        if (CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem) (Object) itemStack).getShadowId() != null) {
            itemStack.setCount(itemStack.getCount() + 1);
        }
        instance.setStack(i, itemStack);
    }

    @Redirect(method = "insert",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    private static ItemStack insert_fix_storage1(ItemStack instance) {
        if (CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem) (Object) instance).getShadowId() != null) {
            return instance;
        }
        return instance.copy();
    }

    @Redirect(method = "insert",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private static void insert_fix_storage2(Inventory instance, int i, ItemStack itemStack) {
        if (CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem) (Object) itemStack).getShadowId() != null) {
            itemStack.setCount(itemStack.getCount() + 1);
        }
        instance.setStack(i, itemStack);
    }

}
