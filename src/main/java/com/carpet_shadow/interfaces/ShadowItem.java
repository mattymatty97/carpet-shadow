package com.carpet_shadow.interfaces;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.ItemStack;

public interface ShadowItem {
    String SHADOW_KEY = "shadow_id";

    static ItemStack carpet_shadow$copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        ItemStack stack = original.call(instance);
        ((ShadowItem) (Object) stack).carpet_shadow$setShadowId(((ShadowItem) (Object) instance).carpet_shadow$getShadowId());
        ((ShadowItem) (Object) stack).carpet_shadow$setClientShadowId(((ShadowItem) (Object) instance).carpet_shadow$getClientShadowId());
        return stack;
    }

    static ItemStack carpet_shadow$copy_supplier(ItemStack instance, ItemStack copy) {
        ((ShadowItem) (Object) copy).carpet_shadow$setShadowId(((ShadowItem) (Object) instance).carpet_shadow$getShadowId());
        ((ShadowItem) (Object) copy).carpet_shadow$setClientShadowId(((ShadowItem) (Object) instance).carpet_shadow$getClientShadowId());
        return copy;
    }

    String carpet_shadow$getShadowId();
    String carpet_shadow$getClientShadowId();

    void carpet_shadow$setShadowId(String id);
    void carpet_shadow$setClientShadowId(String id);

}
