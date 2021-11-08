package com.carpet_shadow.interfaces;

import net.minecraft.item.ItemStack;

public interface ShadowItem {

    void setShadowId(String id);

    String getShadowId();
    static ItemStack copy_redirect(ItemStack instance) {
        ItemStack stack = instance.copy();
        ((ShadowItem) (Object) stack).setShadowId(((ShadowItem) (Object) instance).getShadowId());
        return stack;
    }

}
