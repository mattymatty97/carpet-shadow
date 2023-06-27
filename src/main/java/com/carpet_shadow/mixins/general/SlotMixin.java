package com.carpet_shadow.mixins.general;

import com.carpet_shadow.utility.SlotException;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.desktop.OpenFilesEvent;

@Mixin(Slot.class)
public class SlotMixin {
    @WrapOperation(method="setStackNoCallbacks", at = @At(value = "INVOKE",target = "Lnet/minecraft/screen/slot/Slot;markDirty()V"))
    public void catchException(Slot instance, Operation<Void> original){
        try{
            original.call(instance);
        }catch (Throwable ex){
            throw new SlotException(ex);
        }
    }
}
