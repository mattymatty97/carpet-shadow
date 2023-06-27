package com.carpet_shadow.mixins.general;

import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ClickType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow public abstract Slot getSlot(int index);

    @Shadow public abstract ItemStack getCursorStack();

    @Shadow protected abstract boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast);

    @WrapOperation(method = "onSlotClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;internalOnSlotClick(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private void handle_shadowing(ScreenHandler instance, int slotIndex, int button, SlotActionType actionType, PlayerEntity player, Operation<Void> original) {
        try {
            original.call(instance, slotIndex, button, actionType, player);
        } catch (Throwable error) {
            if(actionType!=SlotActionType.SWAP && actionType!=SlotActionType.PICKUP && actionType!=SlotActionType.QUICK_CRAFT)
                throw error;
            ItemStack stack1 = this.getSlot(slotIndex).getStack();
            ItemStack stack2 = player.getInventory().getStack(button);
            ItemStack stack3 = this.getCursorStack();
            ItemStack shadow = null;
            if(stack1 == stack2 || stack1 == stack3)
                shadow = stack1;
            else if (stack2 == stack3)
                shadow = stack2;

            if(shadow != null){
                CarpetShadow.LOGGER.warn("New Shadow Item Created");
                String shadow_id = ((ShadowItem) (Object) shadow).getShadowId();
                if (shadow_id == null)
                    shadow_id = CarpetShadow.shadow_id_generator.nextString();
                Globals.getByIdOrAdd(shadow_id,shadow);
                if (CarpetShadowSettings.shadowItemMode == CarpetShadowSettings.Mode.UNLINK) {
                    throw error;
                }
            }
        }
    }

    @Inject(method = "internalOnSlotClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;setStack(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.BEFORE),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onPickupSlotClick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/ClickType;)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;markDirty()V"))
    )
    private void reintroduceSuppressionShadowing1(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci){
        if (CarpetShadowSettings.shadowSuppressionGeneration){
            Slot slot = this.getSlot(slotIndex);
            slot.markDirty();
        }
    }

    @Inject(method = "internalOnSlotClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/SlotActionType;SWAP:Lnet/minecraft/screen/slot/SlotActionType;", opcode = Opcodes.GETSTATIC),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/SlotActionType;CLONE:Lnet/minecraft/screen/slot/SlotActionType;", opcode = Opcodes.GETSTATIC))
    )
    private void reintroduceSuppressionShadowing2(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci){
        if (CarpetShadowSettings.shadowSuppressionGeneration){
            Slot slot = this.getSlot(slotIndex);
            slot.markDirty();
        }
    }

}
