package com.carpet_shadow.mixins.general;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ShadowItem {
    private String shadow_id = null;
    @Override
    public void setShadowId(String id){
        shadow_id=id;
    }
    @Override
    public String getShadowId(){
        return shadow_id;
    }

    @Inject(method = "canCombine", at=@At("RETURN"), cancellable = true)
    private static void check_combine(ItemStack stack, ItemStack otherStack, CallbackInfoReturnable<Boolean> cir){
        String shadow1 = ((ShadowItem) (Object) stack).getShadowId();
        String shadow2 = ((ShadowItem) (Object) otherStack).getShadowId();
        if(shadow1 != null && shadow1.equals(shadow2)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isItemEqualIgnoreDamage", at=@At("RETURN"), cancellable = true)
    private void check_EqualIgnoreDamage(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        String shadow1 = ((ShadowItem) (Object) stack).getShadowId();
        String shadow2 = ((ShadowItem) (Object) this).getShadowId();
        if(shadow1 != null && shadow1.equals(shadow2)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isEqual", at=@At("RETURN"), cancellable = true)
    private void check_Equal(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        String shadow1 = ((ShadowItem) (Object) stack).getShadowId();
        String shadow2 = ((ShadowItem) (Object) this).getShadowId();
        if(cir.getReturnValue()){
            if(!Objects.equals(shadow1,shadow2)){
                cir.setReturnValue(false);
            }
        }
    }

    /*
    @Inject(method="split", at=@At("HEAD"), cancellable = true)
    public void pre_split(int amount, CallbackInfoReturnable<ItemStack> cir){
        if(CarpetShadowSettings.shadowItemFragilityFix && this.getShadowId() != null && this.getCount() == amount){
            cir.setReturnValue((ItemStack)(Object)this);
        }
    }*/

/*
    @Inject(at = @At("RETURN"), method = "copy")
    private void post_copy(CallbackInfoReturnable<ItemStack> cir) {
        if(traces.size() > 1 || getShadowId() != null){
            ((ShadowItem)(Object)cir.getReturnValue()).addAllTrace(traces);
            ((ShadowItem)(Object)cir.getReturnValue()).addTrace(Thread.currentThread().getStackTrace());
        }
    }*/
}
