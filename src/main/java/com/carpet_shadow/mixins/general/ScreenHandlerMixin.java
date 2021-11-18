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
import net.minecraft.util.crash.CrashException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.lang.ref.WeakReference;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow
    protected abstract void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player);

    @Shadow public abstract Slot getSlot(int index);

    @Redirect(method = "onSlotClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;internalOnSlotClick(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"),
            require = 0)
    private void handle_shadowing(ScreenHandler instance, int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        try {
            internalOnSlotClick(slotIndex, button, actionType, player);
        } catch (Throwable error) {
            if(actionType!=SlotActionType.SWAP)
                throw error;

            ItemStack stack1 = this.getSlot(slotIndex).getStack();
            ItemStack stack2 = player.getInventory().getStack(button);

            if(stack1 == stack2){
                CarpetShadow.LOGGER.warn("New Shadow Item Created");
                String shadow_id = ((ShadowItem) (Object) stack1).getShadowId();
                if (shadow_id == null) {
                    do {
                        shadow_id = CarpetShadow.shadow_id_generator.nextString();
                    } while (CarpetShadow.shadowMap.containsKey(shadow_id));
                    CarpetShadow.shadowMap.put(shadow_id, new WeakReference<>(stack1));
                    ((ShadowItem) (Object) stack1).setShadowId(shadow_id);
                }
                if (CarpetShadowSettings.shadowItemMode == CarpetShadowSettings.Mode.UNLINK) {
                    throw error;
                }
            }
        }
    }

}
