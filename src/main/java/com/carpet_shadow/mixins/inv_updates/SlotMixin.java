package com.carpet_shadow.mixins.inv_updates;

import com.carpet_shadow.interfaces.InventoryItem;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getStack();

    @Shadow @Final public Inventory inventory;

    @Shadow public abstract int getIndex();

    @Inject(method = "setStack",
            at = @At(value = "HEAD"))
    public void remember_inventory(ItemStack next, CallbackInfo ci) {
            ItemStack curr = getStack();
            if(((ShadowItem)(Object)curr).carpet_shadow$getShadowId() != null){
                ((InventoryItem)(Object)curr).carpet_shadow$removeSlot(this.inventory, getIndex());
            }
            if(((ShadowItem)(Object)next).carpet_shadow$getShadowId() != null){
                ((InventoryItem)(Object)next).carpet_shadow$addSlot(this.inventory, getIndex());
            }
    }
}
