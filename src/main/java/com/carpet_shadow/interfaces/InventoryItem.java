package com.carpet_shadow.interfaces;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Collection;

public interface InventoryItem {

    Collection<Inventory> carpet_shadow$getInventories();

    void carpet_shadow$addSlot(Inventory inventory, int slot);

    void carpet_shadow$removeSlot(Inventory inventory, int slot);

    static void readNbt(BlockEntity instance, NbtCompound nbt) {
        if (instance instanceof Inventory inv) {
            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).carpet_shadow$getShadowId() != null) {
                        ((InventoryItem) (Object) stack).carpet_shadow$removeSlot(inv, index);
                    }
                }
            }catch (Exception ignored){}

            instance.readNbt(nbt);

            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).carpet_shadow$getShadowId() != null) {
                        ((InventoryItem) (Object) stack).carpet_shadow$addSlot(inv, index);
                    }
                }
            }catch (Exception ignored){}
        } else {
            instance.readNbt(nbt);
        }
    }
}
