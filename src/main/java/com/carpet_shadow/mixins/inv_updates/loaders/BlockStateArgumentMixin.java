package com.carpet_shadow.mixins.inv_updates.loaders;


import com.carpet_shadow.interfaces.InventoryItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockStateArgument.class)
public abstract class BlockStateArgumentMixin {

    @Redirect(method = "setBlockState",at=@At(value = "INVOKE",target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    public void interceptBlockEntityLoad(BlockEntity instance, NbtCompound nbt){
        InventoryItem.readNbt(instance,nbt);
    }


}
