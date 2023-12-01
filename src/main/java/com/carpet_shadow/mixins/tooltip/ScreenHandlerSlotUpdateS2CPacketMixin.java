package com.carpet_shadow.mixins.tooltip;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenHandlerSlotUpdateS2CPacket.class)
public abstract class ScreenHandlerSlotUpdateS2CPacketMixin {
    @WrapOperation(method = "<init>(IIILnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    public ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        if (CarpetShadowSettings.shadowItemTooltip) {
            return ShadowItem.carpet_shadow$copy_redirect(instance, original);
        }
        return original.call(instance);
    }

    @ModifyReturnValue(method = "getItemStack", at = @At("RETURN"))
    public ItemStack getShadowStack(ItemStack ret){
        if (CarpetShadowSettings.shadowItemTooltip){
            String id = ((ShadowItem)(Object)ret).carpet_shadow$getClientShadowId();
            ((ShadowItem)(Object)ret).carpet_shadow$setShadowId(null);
            ((ShadowItem)(Object)ret).carpet_shadow$setClientShadowId(id);
        }
        return ret;
    }
}
