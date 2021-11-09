package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ItemEntitySlot;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow
    @Final
    public PlayerEntity player;
    @Shadow
    @Final
    public DefaultedList<ItemStack> main;

    @Shadow
    protected abstract int addStack(ItemStack stack);

    @Shadow
    public abstract int getEmptySlot();

    @Shadow
    public abstract void setStack(int slot, ItemStack stack);

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void redirect_insertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem) (Object) stack).getShadowId() != null) {
            int slot;
            if (stack.isEmpty()) {
                cir.setReturnValue(false);
                return;
            }
            try {
                if (!stack.isDamaged()) {
                    int i;
                    int count;
                    do {
                        i = stack.getCount();
                        count = this.addStack(stack);
                        if (count > 0)
                            stack.setCount(count);
                    } while (!stack.isEmpty() && stack.getCount() < i && count >= 0);
                    if (count < 0) {
                        cir.setReturnValue(true);
                        return;
                    }
                    if (stack.getCount() == i && this.player.getAbilities().creativeMode) {
                        ItemEntity entity = ((ItemEntitySlot) (Object) stack).getEntity();
                        if (entity != null)
                            entity.setDespawnImmediately();
                        else
                            stack.setCount(0);
                        cir.setReturnValue(true);
                        return;
                    }
                    cir.setReturnValue(stack.getCount() < i);
                    return;
                }
                slot = this.getEmptySlot();
                if (slot >= 0) {
                    this.main.set(slot, stack);
                    this.main.get(slot).setCooldown(5);
                    ItemEntity entity = ((ItemEntitySlot) (Object) stack).getEntity();
                    if (entity != null)
                        entity.setDespawnImmediately();
                    cir.setReturnValue(true);
                    return;
                }
                if (this.player.getAbilities().creativeMode) {
                    ItemEntity entity = ((ItemEntitySlot) (Object) stack).getEntity();
                    if (entity != null)
                        entity.setDespawnImmediately();
                    else
                        stack.setCount(0);
                    cir.setReturnValue(true);
                    return;
                }
                cir.setReturnValue(false);
            } catch (Throwable i) {
                CrashReport crashReport = CrashReport.create(i, "Adding item to inventory");
                CrashReportSection crashReportSection = crashReport.addElement("Item being added");
                crashReportSection.add("Item ID", Item.getRawId(stack.getItem()));
                crashReportSection.add("Item data", stack.getDamage());
                crashReportSection.add("Item name", () -> stack.getName().getString());
                throw new CrashException(crashReport);
            }
        }
    }

    @Inject(method = "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), cancellable = true)
    public void add_shadow_item(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (CarpetShadowSettings.shadowItemFragilityFixes && ((ShadowItem) (Object) stack).getShadowId() != null) {
            this.setStack(slot, stack);
            ItemEntity entity = ((ItemEntitySlot) (Object) stack).getEntity();
            if (entity != null) {
                entity.setDespawnImmediately();
            }
            cir.setReturnValue(-1);
        }
    }

}
