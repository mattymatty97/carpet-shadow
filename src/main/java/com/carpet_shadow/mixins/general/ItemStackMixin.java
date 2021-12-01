package com.carpet_shadow.mixins.general;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ShadowItem {
    private String shadow_id = null;

    @Override
    public String getShadowId() {
        return shadow_id;
    }

    @Override
    public void setShadowId(String id) {
        shadow_id = id;
    }

}
