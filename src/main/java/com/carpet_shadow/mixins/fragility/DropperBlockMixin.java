package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DropperBlock.class)
public abstract class DropperBlockMixin {

    @WrapOperation(method = "dispense", at=@At(value = "INVOKE", target = "Lnet/minecraft/block/entity/DispenserBlockEntity;setStack(ILnet/minecraft/item/ItemStack;)V"))
    void fix_dispense(DispenserBlockEntity dispenserBlockEntity, int slot, ItemStack stack, Operation<Void> original, @Local(ordinal = 0) ItemStack itemStack1, @Local(ordinal = 1) ItemStack itemStack2){
        if(CarpetShadowSettings.shadowItemTransferFragilityFix && ((ShadowItem)(Object)itemStack1).getShadowId() != null && itemStack1 != itemStack2){
            itemStack1.setCount(itemStack2.getCount());
            itemStack2 = itemStack1;
            original.call(dispenserBlockEntity, slot, itemStack2);
        }else{
            original.call(dispenserBlockEntity, slot, stack);
        }
    }

}
