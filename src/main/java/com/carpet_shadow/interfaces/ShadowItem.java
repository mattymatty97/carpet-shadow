package com.carpet_shadow.interfaces;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.ItemStack;

public interface ShadowItem {
    String SHADOW_KEY = "shadow_id";

    static ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        ItemStack stack = original.call(instance);
        ((ShadowItem) (Object) stack).setShadowId(((ShadowItem) (Object) instance).getShadowId());
        return stack;
    }

    static ItemStack copy_supplier(ItemStack instance, ItemStack copy) {
        ((ShadowItem) (Object) copy).setShadowId(((ShadowItem) (Object) instance).getShadowId());
        return copy;
    }

    String getShadowId();
    String getClientShadowId();

    void setShadowId(String id);
    void setClientShadowId(String id);

}
