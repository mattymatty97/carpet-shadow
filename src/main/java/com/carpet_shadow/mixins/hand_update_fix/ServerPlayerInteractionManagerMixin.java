package com.carpet_shadow.mixins.hand_update_fix;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow public abstract boolean isCreative();

    @Inject(method = "interactBlock", at = @At(value = "RETURN",shift = At.Shift.BEFORE))
    private void inject_on_block_use(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir){
        if(CarpetShadowSettings.shadowItemUseFix && ((ShadowItem)(Object)stack).getShadowId() != null && !isCreative()) {
            ActionResult result = cir.getReturnValue();
            if (result==ActionResult.SUCCESS || result == ActionResult.CONSUME) {
                int index = (hand == Hand.OFF_HAND) ? PlayerInventory.OFF_HAND_SLOT : player.getInventory().selectedSlot;
                player.currentScreenHandler.getSlotIndex(player.getInventory(), index).ifPresent(i -> player.currentScreenHandler.setPreviousTrackedSlot(i, new ItemStack(Blocks.AIR)));
            }
        }
    }
}
