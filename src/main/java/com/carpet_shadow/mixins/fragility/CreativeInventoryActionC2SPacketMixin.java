package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeInventoryActionC2SPacket.class)
public class CreativeInventoryActionC2SPacketMixin {

    @Redirect(method = "<init>(ILnet/minecraft/item/ItemStack;)V", at=@At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    public ItemStack copy_redirect(ItemStack stack){
        if(CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem)(Object)stack).getShadowId()!=null){
            ItemStack reference = CarpetShadow.shadowMap.get(((ShadowItem)(Object)stack).getShadowId()).get();
            if (reference!=null)
                return reference;
        }
        return stack.copy();
    }
}
