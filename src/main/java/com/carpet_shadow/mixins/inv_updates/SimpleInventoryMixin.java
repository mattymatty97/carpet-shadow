package com.carpet_shadow.mixins.inv_updates;

import com.carpet_shadow.interfaces.InventoryItem;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleInventory.class)
public abstract class SimpleInventoryMixin {

    @Shadow
    public abstract ItemStack getStack(int slot);

    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
    public void track_remove(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack curr = getStack(slot);
        if (((ShadowItem) (Object) curr).carpet_shadow$getShadowId() != null) {
            ((InventoryItem) (Object) curr).carpet_shadow$removeSlot((Inventory) this, slot);
        }
    }

    @Inject(method = "setStack", at = @At("HEAD"))
    public void track_set(int slot, ItemStack next, CallbackInfo ci) {
        ItemStack curr = getStack(slot);
        if (((ShadowItem) (Object) curr).carpet_shadow$getShadowId() != null) {
            ((InventoryItem) (Object) curr).carpet_shadow$removeSlot((Inventory) this, slot);
        }
        if (((ShadowItem) (Object) next).carpet_shadow$getShadowId() != null) {
            ((InventoryItem) (Object) next).carpet_shadow$addSlot((Inventory) this, slot);
        }
    }

}
