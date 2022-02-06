package com.carpet_shadow.mixins.general;

import com.carpet_shadow.utility.SlotException;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Slot.class)
public class SlotMixin {
    @Redirect(method="setStack", at = @At(value = "INVOKE",target = "Lnet/minecraft/screen/slot/Slot;markDirty()V"))
    public void catchException(Slot instance){
        try{
            instance.markDirty();
        }catch (Throwable ex){
            throw new SlotException(ex);
        }
    }
}
