package com.carpet_shadow.mixins.persistence;

import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isEmpty();

    @ModifyReturnValue(at = @At("RETURN"), method = "fromNbt")
    private static ItemStack post_fromNbt(ItemStack stack, NbtCompound nbt) {
        if (nbt.contains("shadow")) {
            if(CarpetShadowSettings.shadowItemMode.shouldResetCount()){
                stack.setCount(0);
            }else if(CarpetShadowSettings.shadowItemMode.shouldLoadItem()) {
                String shadow_id = nbt.getString("shadow");
                stack = Globals.getByIdOrAdd(shadow_id,stack);
                CarpetShadow.LOGGER.debug("Shadowed item loaded from memory");
                CarpetShadow.LOGGER.debug("id: " + shadow_id);
            }
        }
        return stack;
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "writeNbt")
    private NbtCompound post_writeNbt(NbtCompound ret, NbtCompound orig) {
        String shadow_id = ((ShadowItem) this).carpet_shadow$getShadowId();
        if (shadow_id != null) {
            if (this.isEmpty()) {
                CarpetShadow.shadowMap.invalidate(shadow_id);
                ((ShadowItem) this).carpet_shadow$setShadowId(null);
            } else {
                ret.putString("shadow", shadow_id);
                CarpetShadow.LOGGER.debug("Shadowed item saved in memory");
                CarpetShadow.LOGGER.debug("id: " + shadow_id);
            }
        }
        return ret;
    }
}
