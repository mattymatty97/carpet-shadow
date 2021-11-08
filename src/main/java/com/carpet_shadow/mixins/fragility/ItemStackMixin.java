package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ItemEntitySlot;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemEntitySlot {

    private ItemEntity entity = null;

    @Override
    public ItemEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(ItemEntity entity) {
        this.entity = entity;
    }

    @Inject(method = "canCombine", at=@At("RETURN"), cancellable = true)
    private static void check_combine(ItemStack stack, ItemStack otherStack, CallbackInfoReturnable<Boolean> cir){
        Globals.shadow_merge_check(stack, otherStack, cir);
    }

    @Inject(method = "isItemEqualIgnoreDamage", at=@At("RETURN"), cancellable = true)
    private void check_EqualIgnoreDamage(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        Globals.shadow_merge_check(stack, (ItemStack) (Object) this, cir);
    }

    @Inject(method = "isEqual", at=@At("RETURN"), cancellable = true)
    private void check_Equal(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if(CarpetShadowSettings.shadowItemFragilityFixes && cir.getReturnValue()) {
            String shadow1 = ((ShadowItem) (Object) stack).getShadowId();
            String shadow2 = ((ShadowItem) (Object) this).getShadowId();
            if (!Objects.equals(shadow1, shadow2)) {
                cir.setReturnValue(false);
            }
        }
    }
}
