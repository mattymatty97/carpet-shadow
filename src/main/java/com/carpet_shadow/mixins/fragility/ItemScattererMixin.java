package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemScatterer.class)
public abstract class ItemScattererMixin {

    @ModifyExpressionValue(method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private static boolean exitLoop(boolean empty, @Share("break") LocalBooleanRef quit){
        return empty || quit.get();
    }

    @WrapOperation(method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack modify_split(ItemStack stack, int amount, Operation<ItemStack> original, @Share("break") LocalBooleanRef quit){
        String shadow_id = ((ShadowItem)(Object)stack).carpet_shadow$getShadowId();
        if (CarpetShadowSettings.shadowItemInventoryFragilityFix && shadow_id!=null){
            quit.set(true);
            return stack;
        }
        return original.call(stack, amount);
    }
}
