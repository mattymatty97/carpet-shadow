package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DropperBlock.class)
public abstract class DropperBlockMixin {

    @Inject(method = "dispense", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/entity/DispenserBlockEntity;setStack(ILnet/minecraft/item/ItemStack;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    void fix_dispense(ServerWorld world, BlockPos blockPos, CallbackInfo ci, BlockPointerImpl blockPointerImpl, DispenserBlockEntity dispenserBlockEntity, int i, ItemStack itemStack1, Direction facing, Inventory inventory, ItemStack itemStack2){
        if(CarpetShadowSettings.shadowItemTransferFragilityFix && ((ShadowItem)(Object)itemStack1).getShadowId() != null && itemStack1 != itemStack2){
            itemStack1.setCount(itemStack2.getCount());
            itemStack2 = itemStack1;
            dispenserBlockEntity.setStack(i, itemStack2);
            ci.cancel();
        }
    }

}
