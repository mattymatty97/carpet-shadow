package com.carpet_shadow.mixins.tooltip;


import com.carpet_shadow.interfaces.ShadowItem;
import com.google.common.base.Suppliers;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Redirect(method = "syncState", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;", ordinal = 0))
    public ItemStack copy_redirect(ItemStack instance) {
        return ShadowItem.copy_redirect(instance);
    }

    @Shadow
    protected abstract void updateTrackedSlot(int slot, ItemStack stack, Supplier<ItemStack> copySupplier);
    @Shadow
    protected abstract void checkSlotUpdates(int slot, ItemStack stack, Supplier<ItemStack> copySupplier);

    @Shadow protected abstract boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast);

    @Redirect(method = "sendContentUpdates", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;updateTrackedSlot(ILnet/minecraft/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void updateTrackedSlot_redirect(ScreenHandler instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(()->ShadowItem.copy_redirect(stack));
        this.updateTrackedSlot(slot, stack, new_supplier);
    }
    @Redirect(method = "sendContentUpdates", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;checkSlotUpdates(ILnet/minecraft/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void checkSlotUpdates_redirect(ScreenHandler instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(()->ShadowItem.copy_redirect(stack));
        this.checkSlotUpdates(slot, stack, new_supplier);
    }
    @Redirect(method = "updateToClient", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;updateTrackedSlot(ILnet/minecraft/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void updateTrackedSlot_redirect2(ScreenHandler instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(()->ShadowItem.copy_redirect(stack));
        this.updateTrackedSlot(slot, stack, new_supplier);
    }

}
