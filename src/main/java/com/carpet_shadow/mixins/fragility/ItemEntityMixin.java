package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ItemEntitySlot;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @WrapOperation(method = "merge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copyWithCount(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect_copy(ItemStack stack, int count, Operation<ItemStack> original) {
        if (CarpetShadowSettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) stack).carpet_shadow$getShadowId() != null) {
            return stack;
        }
        return original.call(stack, count);
    }

    @ModifyReturnValue(method = "canMerge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"))
    private static boolean canMerge(boolean original, ItemStack stack1, ItemStack stack2) {
        return Globals.shadow_merge_check(stack1, stack2, original);
    }

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;getStack()Lnet/minecraft/item/ItemStack;", shift = At.Shift.BY, by = 2))
    public void setEntityForStack(PlayerEntity player, CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        ((ItemEntitySlot) (Object) stack).carpet_shadow$setEntity((ItemEntity)(Object)this);
    }

    @Inject(method = "onPlayerCollision", at = @At(value = "RETURN"))
    public void resetEntityForStack(PlayerEntity player, CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        ((ItemEntitySlot) (Object) stack).carpet_shadow$setEntity(null);
    }

}
