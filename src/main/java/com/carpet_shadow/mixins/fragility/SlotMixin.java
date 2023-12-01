package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow
    public abstract void setStack(ItemStack stack);

    @WrapOperation(method = "tryTakeStackRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;takeStack(I)Lnet/minecraft/item/ItemStack;"))
    public ItemStack fixFragility_tryTakeStackRange(Slot instance, int amount, Operation<ItemStack> original) {
        if (CarpetShadowSettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) instance.getStack()).carpet_shadow$getShadowId() != null &&
                amount == instance.getStack().getCount()) {
            ItemStack ret = instance.getStack();
            instance.setStack(ItemStack.EMPTY);
            return ret;
        }
        return original.call(instance, amount);
    }

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    public void fixFragility_insertStack(ItemStack stack, int count, CallbackInfoReturnable<ItemStack> cir) {
        if (CarpetShadowSettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) stack).carpet_shadow$getShadowId() != null &&
                count == stack.getCount()) {
            this.setStack(stack);
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
