package com.carpet_shadow.interfaces;

import net.minecraft.item.ItemStack;

public interface ShadowItem {
    String SHADOW_KEY = "shadow_id";

    static ItemStack copy_redirect(ItemStack instance) {
        ItemStack stack = instance.copy();
        ((ShadowItem) (Object) stack).setShadowId(((ShadowItem) (Object) instance).getShadowId());
        return stack;
    }

    String getShadowId();

    void setShadowId(String id);

}
