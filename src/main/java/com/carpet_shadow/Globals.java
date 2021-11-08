package com.carpet_shadow;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class Globals {
    public static void shadow_merge_check(ItemStack stack1, ItemStack stack2, CallbackInfoReturnable<Boolean> cir){
        if(CarpetShadowSettings.shadowItemFragilityFixes && cir.getReturnValue()){
            String shadow1 = ((ShadowItem)(Object)stack1).getShadowId();
            String shadow2 = ((ShadowItem)(Object)stack2).getShadowId();
            if(CarpetShadowSettings.shadowItemPreventCombine){
                if(shadow1!=null && shadow2!=null)
                    cir.setReturnValue(false);
            }else{
                if (shadow1 != null && shadow1.equals(shadow2))
                    cir.setReturnValue(false);
            }
        }
    }
}
