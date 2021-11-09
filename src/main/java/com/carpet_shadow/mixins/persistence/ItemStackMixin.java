package com.carpet_shadow.mixins.persistence;

import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(at = @At("HEAD"), method = "fromNbt", cancellable = true)
    private static void pre_fromNbt(NbtCompound nbt, CallbackInfoReturnable<ItemStack> cir){
        if(CarpetShadowSettings.shadowItemPersistence && nbt.contains("shadow")) {
            String shadow_id = nbt.getString("shadow");
            ItemStack reference = Globals.getByIdOrNull(shadow_id);
            if (reference != null) {
                CarpetShadow.LOGGER.debug("Shadowed item restored");
                CarpetShadow.LOGGER.debug("id: " + shadow_id);
                cir.setReturnValue(reference);
                cir.cancel();
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "fromNbt")
    private static void post_fromNbt(NbtCompound nbt, CallbackInfoReturnable<ItemStack> cir){
        if(CarpetShadowSettings.shadowItemPersistence && nbt.contains("shadow")) {
            String shadow_id = nbt.getString("shadow");
            ItemStack reference = Globals.getByIdOrNull(shadow_id);
            if (reference == null) {
                ItemStack stack = cir.getReturnValue();
                CarpetShadow.shadowMap.put(shadow_id,new WeakReference<>(stack));
                ((ShadowItem) (Object) stack).setShadowId(shadow_id);
                CarpetShadow.LOGGER.debug("Shadowed item loaded from memory");
                CarpetShadow.LOGGER.debug("id: " + shadow_id);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "writeNbt", cancellable = true)
    private void post_writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir){
        if(CarpetShadowSettings.shadowItemPersistence) {
            NbtCompound ret = cir.getReturnValue();
            ItemStack stack = ((ItemStack) (Object) this);
            String shadow_id = ((ShadowItem) (Object) stack).getShadowId();
            if (shadow_id != null) {
                Reference<ItemStack> reference = CarpetShadow.shadowMap.get(shadow_id);
                if ( reference!= null && reference.refersTo(stack)){
                    if (stack.isEmpty()) {
                        CarpetShadow.shadowMap.remove(shadow_id);
                    } else {
                        ret.putString("shadow", shadow_id);
                        cir.setReturnValue(ret);
                        CarpetShadow.LOGGER.debug("Shadowed item saved in memory");
                        CarpetShadow.LOGGER.debug("id: " + shadow_id);
                    }
                }else{
                    ((ShadowItem) (Object) stack).setShadowId(null);
                }
            }
        }
    }
}
