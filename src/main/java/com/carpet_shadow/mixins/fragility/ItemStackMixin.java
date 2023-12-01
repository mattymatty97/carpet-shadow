package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ItemEntitySlot;
import com.carpet_shadow.interfaces.ShadowItem;
import com.carpet_shadow.interfaces.ShifingItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemEntitySlot, ShifingItem {

    @Unique
    boolean shiftMoving = false;
    @Unique
    private ItemEntity entity = null;

    @Override
    public boolean carpet_shadow$isShiftMoving() {
        return shiftMoving;
    }

    @Override
    public void carpet_shadow$setShiftMoving(boolean shiftMoving) {
        this.shiftMoving = shiftMoving;
    }

    @ModifyReturnValue(method = "canCombine", at = @At("RETURN"))
    private static boolean check_combine(boolean original, ItemStack stack, ItemStack otherStack) {
        return Globals.shadow_merge_check(stack, otherStack, original);
    }

    @Override
    public ItemEntity carpet_shadow$getEntity() {
        return entity;
    }

    @Override
    public void carpet_shadow$setEntity(ItemEntity entity) {
        this.entity = entity;
    }

    @ModifyReturnValue(method = "areItemsEqual", at = @At("RETURN"))
    private static boolean check_EqualIgnoreDamage(boolean original, ItemStack left, ItemStack right) {
        return Globals.shadow_merge_check(left, right, original);
    }

    @ModifyReturnValue(method = "areEqual", at = @At("RETURN"))
    private static boolean check_Equal(boolean original, ItemStack left, ItemStack right) {
        if (CarpetShadowSettings.shadowItemInventoryFragilityFix && original) {
            String shadow1 = ((ShadowItem) (Object) left).carpet_shadow$getShadowId();
            String shadow2 = ((ShadowItem) (Object) right).carpet_shadow$getShadowId();
            if (!Objects.equals(shadow1, shadow2)) {
                return false;
            }
        }
        return original;
    }
}
