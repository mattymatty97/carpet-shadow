package com.carpet_shadow.mixins.general;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;


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
