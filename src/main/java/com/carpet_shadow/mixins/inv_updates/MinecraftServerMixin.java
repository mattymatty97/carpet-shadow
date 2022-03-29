package com.carpet_shadow.mixins.inv_updates;

import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "tick", at=@At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V", shift = At.Shift.AFTER))
    public void afterWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        try{
            if(CarpetShadowSettings.shadowItemUpdateFix) {
                for (Inventory inv : Globals.toUpdate) {
                    try {
                        inv.markDirty();
                    } catch (Throwable ex) {
                        CarpetShadow.LOGGER.error("Caught Exception while propagating shadow stack updates: ", ex);
                    }
                }
            }
        }catch (Throwable error){
            CarpetShadow.LOGGER.error("Caught Exception while propagating shadow stack updates: ",error);
        }finally {
            Globals.toUpdate.clear();
        }
    }

}
