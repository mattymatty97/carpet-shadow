package com.carpet_shadow.mixins.general;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(ItemStack.class)
public class ItemStackMixin implements ShadowItem {
    @Unique
    private String shadow_id = null;
    @Unique
    private String client_shadow_id = null;

    @Override
    public String carpet_shadow$getShadowId() {
        return shadow_id;
    }

    @Override
    public void carpet_shadow$setShadowId(String id) {
        shadow_id = id;
        carpet_shadow$setClientShadowId(id);
    }

    @Override
    public String carpet_shadow$getClientShadowId() {
        return client_shadow_id;
    }

    @Override
    public void carpet_shadow$setClientShadowId(String client_shadow_id) {
        this.client_shadow_id = client_shadow_id;
    }
}
