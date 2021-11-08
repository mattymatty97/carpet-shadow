package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getStack();

    @Shadow public abstract ItemStack takeStack(int amount);

    @Shadow public abstract void setStack(ItemStack stack);

    @Redirect(method = "tryTakeStackRange", at=@At(value = "INVOKE",target = "Lnet/minecraft/screen/slot/Slot;takeStack(I)Lnet/minecraft/item/ItemStack;"))
    public ItemStack fixFragility_tryTakeStackRange(Slot instance, int amount){
        if(CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem)(Object)this.getStack()).getShadowId()!=null &&
                amount == this.getStack().getCount()){
            ItemStack ret = this.getStack();
            ItemStack res = ret.copy();
            res.setCount(0);
            this.setStack(res);
            return ret;
        }
        return takeStack(amount);
    }

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/item/ItemStack;",
            at=@At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    public void fixFragility_insertStack(ItemStack stack, int count, CallbackInfoReturnable<ItemStack> cir){
        if(CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem)(Object)stack).getShadowId()!=null &&
                count == stack.getCount()){
            this.setStack(stack);
            ItemStack ret = stack.copy();
            ret.setCount(0);
            cir.setReturnValue(ret);
        }
    }
}
