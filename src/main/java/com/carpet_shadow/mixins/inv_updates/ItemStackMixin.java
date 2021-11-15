package com.carpet_shadow.mixins.inv_updates;


import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.InventoryItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements InventoryItem {

    Set<Pair<Inventory,Integer>> slots = new HashSet<>();

    @Override
    public Collection<Inventory> getInventories() {
        return slots.stream().map(Pair::getLeft).toList();
    }

    @Override
    public void addSlot(Inventory inventory, int slot) {
        slots.add(new ImmutablePair<>(inventory,slot));
    }

    @Override
    public void removeSlot(Inventory inventory, int slot) {
        slots.remove(new ImmutablePair<>(inventory, slot));
    }

    @Inject(method = "setCount", at=@At("RETURN"))
    public void propagate_update(int count, CallbackInfo ci){
        if (CarpetShadowSettings.shadowItemUpdateFix) {
            Globals.toUpdate.addAll(getInventories());
        }
    }


}
