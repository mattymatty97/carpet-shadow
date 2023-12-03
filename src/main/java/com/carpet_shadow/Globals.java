package com.carpet_shadow;

import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Globals {

    public static final Set<Thread> mergingThreads = new HashSet<>();

    public static final Set<Inventory> toUpdate = new HashSet<>();

    public static ItemStack getByIdOrNull(String shadow_id) {
        if(shadow_id == null)
            return null;
        return CarpetShadow.shadowMap.getIfPresent(shadow_id);
    }

    public static ItemStack getByIdOrAdd(String shadow_id, ItemStack stack) {
        ItemStack reference = CarpetShadow.shadowMap.getIfPresent(shadow_id);
        if (reference != null)
            return reference;
        ((ShadowItem)(Object)stack).carpet_shadow$setShadowId(shadow_id);
        CarpetShadow.shadowMap.put(shadow_id, stack);
        return stack;
    }


    public static boolean shadow_merge_check(ItemStack stack1, ItemStack stack2, boolean ret) {
        if (CarpetShadowSettings.shadowItemInventoryFragilityFix && mergingThreads.contains(Thread.currentThread()) && ret) {
            String shadow1 = ((ShadowItem) (Object) stack1).carpet_shadow$getShadowId();
            String shadow2 = ((ShadowItem) (Object) stack2).carpet_shadow$getShadowId();
            if (CarpetShadowSettings.shadowItemPreventCombine) {
                if (shadow1 != null && shadow2 != null)
                    return false;
            } else {
                if (shadow1 != null && shadow1.equals(shadow2))
                    return false;
            }
        }
        return ret;
    }
}
