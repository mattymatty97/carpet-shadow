package com.carpet_shadow.mixins.general;

import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import com.carpet_shadow.utility.ShadowingException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.lang.ref.WeakReference;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Redirect(method = "internalOnSlotClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;setStack(Lnet/minecraft/item/ItemStack;)V", ordinal = 1),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getMaxItemCount(Lnet/minecraft/item/ItemStack;)I", ordinal = 2)),
            require = 0)
    private void handle_shadowing(Slot instance, ItemStack stack) {
        try {
            instance.setStack(stack);
        } catch (Throwable error) {
            CarpetShadow.LOGGER.warn("New Shadow Item Created");
            String shadow_id = ((ShadowItem) (Object) stack).getShadowId();
            if (shadow_id == null) {
                do {
                    shadow_id = CarpetShadow.shadow_id_generator.nextString();
                } while (CarpetShadow.shadowMap.containsKey(shadow_id));
                CarpetShadow.shadowMap.put(shadow_id, new WeakReference<>(stack));
                ((ShadowItem) (Object) stack).setShadowId(shadow_id);
            }
            if (CarpetShadowSettings.shadowItemMode != CarpetShadowSettings.Mode.UNLINK) {
                throw new ShadowingException();
            } else {
                throw error;
            }
        }
    }

    @Shadow
    protected abstract void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player);

    @Redirect(method = "onSlotClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;internalOnSlotClick(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"),
            require = 0)
    private void suppress_crash(ScreenHandler instance, int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        try {
            internalOnSlotClick(slotIndex, button, actionType, player);
        } catch (ShadowingException ignored) {
        }
    }

}
