package com.carpet_shadow;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class Globals {

    public static ItemStack getByIdOrNull(String shadow_id) {
        if(shadow_id == null)
            return null;
        Reference<ItemStack> reference = CarpetShadow.shadowMap.get(shadow_id);
        if (reference != null && !reference.refersTo(null)) {
            return reference.get();
        }
        return null;
    }

}
