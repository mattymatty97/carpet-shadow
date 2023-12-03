package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ItemEntitySlot;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
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
        Globals.mergingThreads.add(Thread.currentThread());
        boolean ret = Globals.shadow_merge_check(stack1, stack2, original);
        Globals.mergingThreads.remove(Thread.currentThread());
        return ret;
    }

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;getStack()Lnet/minecraft/item/ItemStack;", shift = At.Shift.BY, by = 2))
    public void setEntityForStack(PlayerEntity player, CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        ((ItemEntitySlot) (Object) stack).carpet_shadow$setEntity((ItemEntity)(Object)this);
    }

    @Inject(method = "onPlayerCollision", at = @At(value = "RETURN"))
    public void resetEntityForStack(PlayerEntity player, CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        ((ItemEntitySlot) (Object) stack).carpet_shadow$setEntity(null);
    }

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void merging_start(PlayerEntity player, CallbackInfo ci){
        Globals.mergingThreads.add(Thread.currentThread());
    }
    @Inject(method = "onPlayerCollision", at = @At("RETURN"))
    private void merging_end(PlayerEntity player, CallbackInfo ci){
        Globals.mergingThreads.remove(Thread.currentThread());
    }

}
