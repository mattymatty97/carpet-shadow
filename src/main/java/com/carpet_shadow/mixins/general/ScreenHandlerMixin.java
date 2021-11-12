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

    @Redirect(method = "method_30010",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;setStack(Lnet/minecraft/item/ItemStack;)V",ordinal = 2),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/SlotActionType;SWAP:Lnet/minecraft/screen/slot/SlotActionType;")),
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
            throw error;
        }
    }

    @Redirect(method = "method_30010",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;setStack(Lnet/minecraft/item/ItemStack;)V",ordinal = 4),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/SlotActionType;SWAP:Lnet/minecraft/screen/slot/SlotActionType;")),
            require = 0)
    private void handle_shadowing2(Slot instance, ItemStack stack) {
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
            throw error;
        }
    }
//
//    @Shadow
//    protected abstract ItemStack method_30010(int slotIndex, int button, SlotActionType actionType, PlayerEntity player);
//
//    @Redirect(method = "onSlotClick",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;method_30010(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;"),
//            require = 0)
//    private ItemStack suppress_crash(ScreenHandler instance, int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
//        try {
//            return method_30010(slotIndex, button, actionType, player);
//        } catch (ShadowingException ignored) {
//        }
//    }

}
