package com.carpet_shadow.interfaces;

import com.carpet_shadow.mixins.fragility.ItemEntityMixin;
import net.minecraft.entity.ItemEntity;

public interface ItemEntitySlot {

    ItemEntity getEntity();
    void setEntity(ItemEntity entity);

}
