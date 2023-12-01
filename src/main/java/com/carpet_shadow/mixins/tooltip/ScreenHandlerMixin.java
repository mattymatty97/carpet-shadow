package com.carpet_shadow.mixins.tooltip;


import com.carpet_shadow.interfaces.ShadowItem;
import com.google.common.base.Suppliers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @WrapOperation(method = "syncState", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;", ordinal = 0))
    public ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        return ShadowItem.carpet_shadow$copy_redirect(instance, original);
    }

    @WrapOperation(method = "sendContentUpdates", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;updateTrackedSlot(ILnet/minecraft/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void updateTrackedSlot_redirect(ScreenHandler instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier, Operation<Void> original) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(() -> ShadowItem.carpet_shadow$copy_supplier(stack, copySupplier.get()));
        original.call(instance, slot, stack, new_supplier);
    }

    @WrapOperation(method = "sendContentUpdates", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;checkSlotUpdates(ILnet/minecraft/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void checkSlotUpdates_redirect(ScreenHandler instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier, Operation<Void> original) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(() -> ShadowItem.carpet_shadow$copy_supplier(stack, copySupplier.get()));
        original.call(instance, slot, stack, new_supplier);
    }

    @WrapOperation(method = "updateToClient", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;updateTrackedSlot(ILnet/minecraft/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void updateTrackedSlot_redirect2(ScreenHandler instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier, Operation<Void> original) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(() -> ShadowItem.carpet_shadow$copy_supplier(stack, copySupplier.get()));
        original.call(instance, slot, stack, new_supplier);
    }

}
