package com.carpet_shadow.interfaces;

import com.carpet_shadow.CarpetShadowSettings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Collection;

public interface InventoryItem {

    Collection<Inventory> getInventories();

    void addSlot(Inventory inventory, int slot);

    void removeSlot(Inventory inventory, int slot);

    static void readNbt(BlockEntity instance, NbtCompound nbt) {

        if (instance instanceof Inventory inv) {

            for (int index = 0; index < inv.size(); index++) {
                ItemStack stack = inv.getStack(index);
                if (((ShadowItem) (Object) stack).getShadowId() != null) {
                    ((InventoryItem) (Object) stack).removeSlot(inv, index);
                }
            }

            instance.readNbt(nbt);

            for (int index = 0; index < inv.size(); index++) {
                ItemStack stack = inv.getStack(index);
                if (((ShadowItem) (Object) stack).getShadowId() != null) {
                    ((InventoryItem) (Object) stack).addSlot(inv, index);
                }
            }

        } else {
            instance.readNbt(nbt);
        }
    }
}
