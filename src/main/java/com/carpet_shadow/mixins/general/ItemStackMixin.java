package com.carpet_shadow.mixins.general;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(ItemStack.class)
public class ItemStackMixin implements ShadowItem {
    private String shadow_id = null;
    private String client_shadow_id = null;

    @Override
    public String getShadowId() {
        return shadow_id;
    }

    @Override
    public void setShadowId(String id) {
        shadow_id = id;
        setClientShadowId(id);
    }

    @Override
    public String getClientShadowId() {
        return client_shadow_id;
    }

    @Override
    public void setClientShadowId(String client_shadow_id) {
        this.client_shadow_id = client_shadow_id;
    }
}
