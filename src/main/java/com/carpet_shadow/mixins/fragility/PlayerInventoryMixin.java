package com.carpet_shadow.mixins.fragility;

import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ItemEntitySlot;
import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
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

    @Redirect(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setCount(I)V"), slice = @Slice(from = @At(value = "FIELD",target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z")))
    private void modify_count(ItemStack instance, int count){
        if(count==0 && CarpetShadowSettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) instance).getShadowId() != null){
            ItemEntity entity = ((ItemEntitySlot) (Object) instance).getEntity();
            if (entity != null)
                entity.setDespawnImmediately();
            else
                instance.setCount(0);
        }else{
            instance.setCount(count);
        }
    }

    @Inject(method = "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), cancellable = true)
    public void add_shadow_item(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (CarpetShadowSettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) stack).getShadowId() != null) {
            this.setStack(slot, stack);
            ItemEntity entity = ((ItemEntitySlot) (Object) stack).getEntity();
            if (entity != null) {
                entity.setDespawnImmediately();
            }
            cir.setReturnValue(-1);
        }
    }

}
